package org.bastanchu.churierp.churierpback.dao

import org.bastanchu.churierp.churierpback.ApplicationContextConfiguration
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(value = [SpringExtension::class])
@ContextConfiguration(classes = [ApplicationContextConfiguration::class])
@Tag("integrationTest")
class TestEntityDaoITCase {

    @Autowired
    val testEntityDao:TestEntityDao? = null

    //@Test
    fun shouldGetAnExistingEntityProperly() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntity = testEntityDao.getById(0 as Integer)
            assertNotNull(testEntity, "Test entity must be retrieved")
            if (testEntity != null) {
                assertEquals(0 as Integer, testEntity.id)
                assertEquals("This is an entity", testEntity.text)
            }
        }
    }
}