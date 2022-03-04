package org.bastanchu.churierp.churierpback

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.*
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

@Configuration
@ComponentScan(basePackages = ["org.bastanchu.churierp.churierpback.dao.impl",
                               "org.bastanchu.churierp.churierpback.dao.impl.administration.users",
                               "org.bastanchu.churierp.churierpback.service.impl"])
@EnableJpaRepositories
@PropertySource("classpath:test-application.properties")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
open class ApplicationContextConfiguration {

    @Autowired
    private val env: Environment? = null

    @Bean(name = ["dataSource"])
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env?.getProperty("jdbc.churierpweb.driver"))
        dataSource.url = env?.getProperty("jdbc.churierpweb.url")
        dataSource.username = env?.getProperty("jdbc.churierpweb.username")
        dataSource.password = env?.getProperty("jdbc.churierpweb.password")
        return dataSource
    }

    @Bean(name = ["entityManagerFactory"])
    @Primary
    @Throws(PropertyVetoException::class)
    open fun entityManagerFactoryBean(@Autowired dataSource: DataSource?): LocalContainerEntityManagerFactoryBean? {
        val em = LocalContainerEntityManagerFactoryBean()
        em.dataSource = dataSource
        em.setPackagesToScan(*arrayOf("org.bastanchu.churierp.churierpback.entity"))
        val vendorAdapter: JpaVendorAdapter = HibernateJpaVendorAdapter()
        em.jpaVendorAdapter = vendorAdapter
        val properties = Properties()
        properties.setProperty("hibernate.dialect", env!!.getProperty("hibernate.churierpweb.dialect"))
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults", "false")
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
}