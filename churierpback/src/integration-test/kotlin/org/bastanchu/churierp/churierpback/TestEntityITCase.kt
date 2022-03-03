package org.bastanchu.churierp.churierpback

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource

@ExtendWith(value = [SpringExtension::class])
@ContextConfiguration(classes = [ApplicationContextConfiguration::class])
@Tag("integrationTest")
class TestEntityITCase {

    @Autowired
    val dataSource:DataSource? = null

    @Test
    fun shouldPerformAnEntityTestProperly() {
        assertTrue(true)
    }

    @Test
    fun shouldPerformAQueryAgainstTestingDatabaseProperly() {
        assertNotNull(dataSource, "DataSource must not be null")
        val connection = dataSource?.connection
        assertNotNull(connection, "Connection must not be null")
        connection.use { it ->
            val query = "select * from TEST_ENTITIES"
            val statement = it?.prepareStatement(query)
            statement.use {
                val resultSet = it?.executeQuery()
                assertNotNull(resultSet,"Result set must not be null")
                if (resultSet != null) {
                    while (resultSet.next()) {
                        val id = resultSet.getInt("ID") as Integer;
                        assertEquals(1 as Integer, id)
                        val text = resultSet.getString("TEXT")
                        assertEquals("This is an entity", text)
                    }
                }
            }
        }
    }
}