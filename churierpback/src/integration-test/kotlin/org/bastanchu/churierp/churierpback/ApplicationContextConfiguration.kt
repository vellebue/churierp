package org.bastanchu.churierp.churierpback

import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.annotation.*
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.context.support.ReloadableResourceBundleMessageSource
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.beans.PropertyVetoException
import java.util.*
import javax.sql.DataSource


/**
 * Mirar las siguientes URLs:
 * https://www.baeldung.com/spring-boot-testcontainers-integration-test
 * https://www.testcontainers.org/test_framework_integration/junit_5/
 */
@Configuration
@ComponentScan( basePackages = ["org.bastanchu.churierp.churierpback.**"])
@EnableJpaRepositories()
@PropertySource("classpath:test-application.properties")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
open class ApplicationContextConfiguration(@Autowired val environment: Environment) {

    val logger = LoggerFactory.getLogger(ApplicationContextConfiguration::class.java)

    init {
        logger.info("Starting ApplicationContextConfiguration")
    }

    @Bean(name = ["dataSource"])
    open fun dataSource() : DataSource {
        logger.info("Starting Test Datasource")
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.postgresql.Driver")
        dataSource.url =  environment.getProperty("spring.datasource.url")// containerInstance.jdbcUrl
        dataSource.username = environment.getProperty("spring.datasource.username") //containerInstance.username
        dataSource.password = environment.getProperty("spring.datasource.password")//containerInstance.password
        return dataSource
    }

    @Bean(name = ["flyway"])
    open fun flywayInitializer(@Autowired dataSource: DataSource): Flyway {
        val flywayConfiguration = ClassicConfiguration()
        flywayConfiguration.dataSource = dataSource
        flywayConfiguration.setLocationsAsStrings("filesystem:/src/main/resources/db/migrations")
        return Flyway(flywayConfiguration)
    }

    @Bean(name = ["entityManagerFactory"])
    @Primary
    @Throws(PropertyVetoException::class)
    open fun entityManagerFactoryBean(@Autowired dataSource: DataSource, @Autowired flyway:Flyway): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        //em.setPackagesToScan(*packagesToScanForEntities)
        em.setPersistenceXmlLocation("test-persistence.xml")
        val vendorAdapter: JpaVendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties = Properties()
        properties.setProperty("hibernate.dialect", environment.getProperty("hibernate.churierpweb.dialect"))
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false")
        properties.setProperty("hibernate.show_sql", "false")
        properties.setProperty("hibernate.format_sql", "false")
        em.setJpaProperties(properties)
        return em
    }

    @Bean(name = ["transactionManager"])
    @Throws(PropertyVetoException::class)
    open fun transactionManager(@Autowired entityManagerFactory: LocalContainerEntityManagerFactoryBean): PlatformTransactionManager? {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = entityManagerFactory.getObject()
        return transactionManager
    }

    @Bean(name = ["messageSource"])
    @Throws(PropertyVetoException::class)
    open fun messageSource() : MessageSource {
        val messageSource = ReloadableResourceBundleMessageSource()
        messageSource.setCacheSeconds(5)
        messageSource.setDefaultEncoding("UTF-8")
        messageSource.setFallbackToSystemLocale(true)
        messageSource.setUseCodeAsDefaultMessage(true)
        messageSource.setBasenames("file:../churierpweb/src/main/resources/languages/messages")
        LocaleContextHolder.setLocale(Locale("en"))
        return messageSource
    }


}