package org.bastanchu.churierp.churierpback

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.jdbc.datasource.DriverManagerDataSource
import javax.sql.DataSource

@Configuration
@PropertySource("classpath:test-application.properties")
open class ApplicationContextConfiguration {

    @Autowired
    private val env: Environment? = null

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName(env?.getProperty("jdbc.churierpweb.driver"))
        dataSource.url = env?.getProperty("jdbc.churierpweb.url")
        dataSource.username = env?.getProperty("jdbc.churierpweb.username")
        dataSource.password = env?.getProperty("jdbc.churierpweb.password")
        return dataSource
    }
}