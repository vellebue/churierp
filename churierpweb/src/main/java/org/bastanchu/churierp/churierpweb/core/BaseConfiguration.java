package org.bastanchu.churierp.churierpweb.core;

import org.hibernate.validator.HibernateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.*;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.DefaultPersistenceUnitManager;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitManager;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring5.ISpringTemplateEngine;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.beans.PropertyVetoException;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {
        "org.bastanchu.churierp.churierpweb.delegate.impl",
        "org.bastanchu.churierp.churierpweb.view",
        "org.bastanchu.churierp.churierpback.dao.impl",
        "org.bastanchu.churierp.churierpback.service.impl",
        "org.bastanchu.churierp.churierpweb.controller"
})
@EnableJpaRepositories
@EnableWebMvc
@PropertySource("classpath:application.properties")
@EnableTransactionManagement(mode = AdviceMode.PROXY)
public class BaseConfiguration {

    @Autowired
    private Environment env;
    @Autowired
    private ApplicationContext applicationContext;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("jdbc.churierpweb.driver"));
        dataSource.setUrl(env.getProperty("jdbc.churierpweb.url"));
        dataSource.setUsername(env.getProperty("jdbc.churierpweb.username"));
        dataSource.setPassword(env.getProperty("jdbc.churierpweb.password"));
        return dataSource;
    }

    @Bean(name="entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean  entityManagerFactoryBean(@Autowired DataSource dataSource) throws PropertyVetoException {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource);
        em.setPackagesToScan(new String[]{"org.bastanchu.churierp.churierpback.entity"});
        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", env.getProperty("hibernate.churierpweb.dialect"));
        properties.setProperty("hibernate.temp.use_jdbc_metadata_defaults","false");
        em.setJpaProperties(properties);
        return em;
    }

    @Bean(name="transactionManager")
    public PlatformTransactionManager transactionManager(@Autowired LocalContainerEntityManagerFactoryBean entityManagerFactory) throws PropertyVetoException{
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("languages/messages");
        messageSource.setDefaultEncoding("ISO-8859-1");
        return messageSource;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver();
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    @Bean
    public ISpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(applicationContext);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setTemplateMode(TemplateMode.HTML);
        return resolver;
    }

    /*
    @Bean
    public Validator validator(@Autowired AutowireCapableBeanFactory autowireCapableBeanFactory) {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class).configure().buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        return validator;
    }
    */
}
