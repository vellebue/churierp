package org.bastanchu.churierp.churierpback

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.configuration.ClassicConfiguration
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.MountableFile
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*
import javax.sql.DataSource

@RunWith(SpringRunner::class)
@ExtendWith(value = [SpringExtension::class])
@SpringBootTest
@ContextConfiguration(classes = [ApplicationContextConfiguration::class],
    initializers = [BaseContainerDBITCase.Companion.Initializer::class],
                      value = arrayOf("classpath:test-context.xml"))
@Testcontainers
/*
 * Recipe to ensure something is executed before all tests in this class
 * https://stackoverflow.com/questions/43282798/in-junit-5-how-to-run-code-before-all-tests
 */
abstract class BaseContainerDBITCase {

    //val logger = LoggerFactory.getLogger(BaseContainerDBITCase::class.java)

    class MyPostgresqlContainer(dockerImageName : String) : PostgreSQLContainer<MyPostgresqlContainer>(dockerImageName)

    //@Lazy
    @Autowired
    protected var dataSource : DataSource? = null

    companion object {

        val logger = LoggerFactory.getLogger(Companion::class.java)
        var  currentExposedPort = 40000
        //var postgresqlContainerLoaded = false;
        var postgresqlContainerClassLoadedVector = Vector<Class<in BaseContainerDBITCase>>()

        @Container
        @JvmField
        val postgreSQLContainer = MyPostgresqlContainer("postgres:11.1").apply {
            val innerPort = 5432
            val outerPort = getExposedPort()
            withDatabaseName("integration-tests-db")
            withUsername("sa")
            withPassword("sa")
            withReuse(true)
            withExposedPorts(innerPort)
            withCreateContainerCmdModifier { cmd ->
                cmd.withHostConfig(HostConfig().withPortBindings(PortBinding(Ports.Binding.bindPort(outerPort),
                                                                 ExposedPort(innerPort)
                )))
            }
            withLogConsumer(Slf4jLogConsumer(logger))
        }

        @Synchronized private fun getExposedPort() : Int {
            return currentExposedPort++
        }

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword(),
                    "testcontainers.reuse.enable=true"
                ).applyTo(applicationContext.getEnvironment());
            }
        }
    }

    @BeforeEach
    public fun loadPostgresqlContainer() {

        //println("Attemp to load script content")
        synchronized(postgresqlContainerClassLoadedVector) {
            if (!postgresqlContainerClassLoadedVector.contains(this.javaClass)) {
                logger.info("Performing DB load")
                val flyway = buildFlyway()
                flyway?.migrate()
                logger.info("DB load completed")
                //val logConsumer = Slf4jLogConsumer(logger)
                //postgreSQLContainer.followOutput(logConsumer)
                logger.warn("Attemp to load script content")
                logger.info("Executing script content")
                if (!getScriptContent().trim().equals("")) {
                    logger.warn("Executing script content")
                    executePostgresqlScript(getScriptContent())
                    logger.warn("Script content executed")
                } else {
                    logger.info("This is an empty script")
                }
                postgresqlContainerClassLoadedVector.add(this.javaClass)
                logger.debug("Added class to vector")
            } else {
                logger.info("DB Script already loaded")
            }
        }
    }

    private fun buildFlyway() : Flyway {
        val flywayConfiguration = ClassicConfiguration()
        flywayConfiguration.dataSource = dataSource
        flywayConfiguration.setLocationsAsStrings("db/migrations")
        val flyway = Flyway(flywayConfiguration)
        return flyway
    }

    private fun executePostgresqlScript(script : String) {
        val conn = dataSource!!.connection
        conn.use {
            val callableStatement = it.prepareCall(script)
            logger.info("Executing script\n ${script}\n")
            callableStatement.use {
                it.execute()
            }
            logger.info("Script executed")
        }
    }

    protected open fun getScriptContent() : String{
        return "";
    }
}