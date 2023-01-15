package org.bastanchu.churierp.churierpback

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.HostConfig
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
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
                      value = "classpath:test-context.xml")
@Testcontainers
abstract class BaseContainerDBITCase {

    val logger = LoggerFactory.getLogger(BaseContainerDBITCase::class.java)

    class MyPostgresqlContainer(dockerImageName : String) : PostgreSQLContainer<MyPostgresqlContainer>(dockerImageName)

    //@Lazy
    @Autowired
    protected var dataSource : DataSource? = null

    var postgresqlContainerLoaded = false;

    companion object {

        val logger = LoggerFactory.getLogger(Companion::class.java)
        var  currentExposedPort = 40000

        @Container
        @JvmField
        val postgreSQLContainer = MyPostgresqlContainer("postgres:11.1").apply {
            val innerPort = 5432
            val outerPort = getExposedPort()
            val targetSQLFileBaseName = "target/sql/target-file"
            val testDelcaringClass = this::class.java.declaringClass as Class<in BaseContainerDBITCase>
            val targetSQLFile = concatenateFiles(
                arrayOf(File("sql/create-tables.sql"), File("sql/insert-data.sql")),
                targetSQLFileBaseName, testDelcaringClass
            )
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
            waitingFor(Wait.defaultWaitStrategy())
                .withCopyFileToContainer(
                    MountableFile.forHostPath(targetSQLFile),
                    "/docker-entrypoint-initdb.d/init.sql"
                )
        }

        @Synchronized private fun getExposedPort() : Int {
            return currentExposedPort++
        }

        class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

            override fun initialize(applicationContext: ConfigurableApplicationContext) {
                TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
                ).applyTo(applicationContext.getEnvironment());
            }
        }

        private fun concatenateFiles(
            inFiles: Array<File>,
            outFilePrefixName: String,
            testClass: Class<in BaseContainerDBITCase>
        ): String {
            val timestamp = Date().time
            val outFile = outFilePrefixName + "_" + testClass.simpleName + "_" + timestamp + ".sql"
            logger.info("Database initialization script for class ${testClass.simpleName} is ${outFile} ")
            createSqlDirIfNotExists()
            val outputWriter = FileWriter(outFile)
            outputWriter.use {
                val outWriter = it
                for (inputFile in inFiles) {
                    val inputBuffer = BufferedReader(FileReader(inputFile))
                    inputBuffer.use {
                        val inBuffer = it
                        var line: String? = inBuffer.readLine()
                        while (line != null) {
                            outWriter.write(line)
                            outWriter.write("\n")
                            line = inBuffer.readLine()
                        }
                    }
                }
                outWriter.flush()
            }
            return outFile
        }

        private fun createSqlDirIfNotExists() {
            val fileDir = File("target/sql")
            if (!(fileDir.exists() && fileDir.isDirectory)) {
                fileDir.mkdir()
            }
        }

    }

    @BeforeEach
    public fun loadPostgresqlContainer() {
        if (!postgresqlContainerLoaded) {
            if (!getScriptContent().trim().equals("")) {
                logger.info("Executing script content")
                executePostgresqlScript(getScriptContent())
                logger.info("Script content executed")
            }
            postgresqlContainerLoaded = true;
        }
    }

    private fun executePostgresqlScript(script : String) {
        val conn = dataSource!!.connection
        conn.use {
            val callableStatement = it.prepareCall(script)
            logger. info("Executing test script content")
            callableStatement.use {
                it.execute()
            }
        }
    }

    protected open fun getScriptContent() : String{
        return "";
    }
}