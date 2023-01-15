package org.bastanchu.churierp.churierpback

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class MainDBITCase(@Autowired val applicationContext : ApplicationContext) : BaseContainerDBITCase() {

    @Test
    fun shouldPostgresqlContainerBeRunning() {
        logger.info("Testing Postgresql container")
        Assertions.assertTrue(postgreSQLContainer.isRunning, "Postgresql container must be running")
        logger.info("Loaded Spring Beans: ")
        val allBeanNames = applicationContext.getBeanDefinitionNames()
        for (beanName in allBeanNames) {
            logger.info("Registered bean ${beanName}")
        }
        logger.info("Spring Beans List completed")
    }

    @Test
    fun shouldDatasourceBeAvailable() {
        Assertions.assertNotNull(dataSource, "DataSource must not be null")
        val connection = dataSource!!.connection
        Assertions.assertNotNull(connection, "Datasource connection must not be null")
        connection.close()
    }

    @Test
    fun shouldTestQueryWorkCorrectly() {
        val testLogin = "angel";
        val sql = "select u.NAME, u.SURNAME from USERS u " +
                " where u.LOGIN = ? "
        val connection = dataSource!!.connection
        connection.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, testLogin)
            statement.use {
                val resultSet = it.executeQuery()
                resultSet.use {
                    while (it.next()) {
                        val name = it.getString("NAME")
                        val surname = it.getString("SURNAME")
                        Assertions.assertEquals("Ángel", name)
                        Assertions.assertEquals("García Bastanchuri", surname)
                    }
                }
            }

        }
    }

}