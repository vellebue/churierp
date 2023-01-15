package org.bastanchu.churierp.churierpback

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.sql.DataSource

//@ExtendWith(value = [SpringExtension::class])
//@ContextConfiguration(classes = [ApplicationContextConfiguration::class])
//@Tag("integrationTest")
@Deprecated("New Test structure developed")
class TestEntityITCase {

    //@Autowired
    val dataSource:DataSource? = null

    //@Test
    fun shouldPerformAnEntityTestProperly() {
        assertTrue(true)
    }
}