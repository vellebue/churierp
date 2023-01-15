package org.bastanchu.churierp.churierpback.dao

import org.bastanchu.churierp.churierpback.ApplicationContextConfiguration
import org.bastanchu.churierp.churierpback.dto.TestEntityDto
import org.bastanchu.churierp.churierpback.dto.TestEntityFilterDto
import org.bastanchu.churierp.churierpback.entity.TestEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.Commit
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionManager
import org.springframework.transaction.support.TransactionTemplate
import java.lang.RuntimeException
import java.text.SimpleDateFormat
import javax.persistence.EntityManager
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

//@ExtendWith(value = [SpringExtension::class])
//@ContextConfiguration(classes = [ApplicationContextConfiguration::class])
//@Tag("integrationTest")
@Deprecated("New Test structure developed")
class TestEntityDaoITCase {

    @Autowired
    val testEntityDao:TestEntityDao? = null
    @Autowired
    val dataSource:DataSource? = null
    @Autowired
    val transactionManager:TransactionManager? = null
    @Autowired
    val entityManagerFactory:EntityManagerFactory? = null
    @Autowired
    val entityManager:EntityManager? = null

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd")

    // QUERY TESTS

    //@Test
    fun shouldGetAnExistingEntityProperly() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntity = testEntityDao.getById(0 as Integer)
            assertNotNull(testEntity, "Test entity must be retrieved")
            if (testEntity != null) {
                assertEquals(0 as Integer, testEntity.id)
                assertEquals("This is an entity", testEntity.text)
                assertEquals("2022/02/03", dateFormat.format(testEntity.eventDate))
            }
        }
    }

    //@Test
    fun shouldPerformAFilterQueryProperly() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val filterEntity = TestEntity()
            filterEntity.eventDate = dateFormat.parse("2022/03/01")
            val resultList = testEntityDao.filter(filterEntity);
            assertEquals(1, resultList.size, "Resulting list size must be 1")
            val resultEntity = resultList[0]
            assertEquals(1, resultEntity.id, "Result entity id must be 1")
            assertEquals("This is the second entity", resultEntity.text,
                "Result entity text must be 'This is the second entity'")
            assertEquals("2022/03/01", dateFormat.format(resultEntity.eventDate),
                "Result entity event date must be 2022/03/01")

        }
    }

    //@Test
    fun shouldPerformListAllEntitiesProperly() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val resultEntities = testEntityDao.listAll();
            assertEquals(3, resultEntities.size, "listAll must return 3 entities")
            val sortedResultEntities = resultEntities.sortedBy { it.id as Int}
            //Review only first two entities
            val firstEntity = sortedResultEntities[0]
            val secondEntity = sortedResultEntities[1]
            assertEquals(0, firstEntity.id, "First entity id must be 0")
            assertEquals("This is an entity", firstEntity.text, "Fisrt entity text must be 'This is an entity'")
            assertEquals("2022/02/03", dateFormat.format(firstEntity.eventDate),
                "First entity event date must be '2022/02/03'")
            assertEquals(1, secondEntity.id, "Second entity id must be 1")
            assertEquals("This is the second entity", secondEntity.text, "Second entity text must be 'This is the second entity'")
            assertEquals("2022/03/01", dateFormat.format(secondEntity.eventDate),
                "Second entity event date must be '2022/02/03'")
        }
    }

    //@Test
    fun shouldPerformAGenericFilterProperly() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val entityFilterDto = TestEntityFilterDto()
            entityFilterDto.id = 1 as Integer
            entityFilterDto.text = "*second*"
            entityFilterDto.eventDateFrom = dateFormat.parse("2022/02/28")
            entityFilterDto.eventDateTo = dateFormat.parse("2022/03/10")
            val resultList = testEntityDao.genericFilter(entityFilterDto);
            assertEquals(1, resultList.size, "Resulting list after filter must be size 1")
            val entity = resultList[0]
            assertEquals(1, entity.id, "Entity id must be 1")
            assertEquals("This is the second entity", entity.text, "Entity text must be 'This is the second entity'")
            assertEquals("2022/03/01", dateFormat.format(entity.eventDate),
                "Entity event date must be '2022/03/01'")
        }
    }

    // CONVERSION DTO ENTITY

    //@Test
    fun shouldConvertFromEntityToDtoSingleArgument() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntity = TestEntity()
            testEntity.id = 0 as Integer
            testEntity.text = "This is an entity"
            testEntity.eventDate = dateFormat.parse("2022/02/03")
            val testDto = testEntityDao.toDataTransferObject(testEntity)
            assertEquals(0, testDto.id, "Dto id must be 0")
            assertEquals("This is an entity", testDto.text, "Dto text must be 'This is an entity'")
            assertEquals("2022/02/03", dateFormat.format(testDto.eventDate), "Dto event date must be '2022/02/03'")
        }
    }

    //@Test
    fun shouldConvertFromEntityToDtoTwoArguments() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntity = TestEntity()
            testEntity.id = 0 as Integer
            testEntity.text = "This is an entity"
            testEntity.eventDate = dateFormat.parse("2022/02/03")
            val testDto = TestEntityDto()
            testEntityDao.toDataTransferObject(testEntity, testDto)
            assertEquals(0, testDto.id, "Target dto id must be 0")
            assertEquals("This is an entity", testDto.text, "Target dto text must be 'This is an entity'")
            assertEquals("2022/02/03", dateFormat.format(testDto.eventDate), "Target dto event date must be '2022/02/03'")
        }
    }

    //@Test
    fun shouldPerformDtoToEntitySingleArgumentPersitedEntity() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntityDto = TestEntityDto()
            testEntityDto.id = 2 as Integer
            testEntityDto.text = "This is a changed text entity"
            testEntityDto.eventDate = dateFormat.parse("2022/03/15")
            val transactionTemplate = TransactionTemplate(transactionManager as PlatformTransactionManager)
            // Execute dto to entity on isolated transaction
            transactionTemplate.execute {
                val testEntity = testEntityDao.fromDtoToEntity(testEntityDto)
                //val identifier = entityManagerFactory?.persistenceUnitUtil?.getIdentifier(testEntity)
                val attached = entityManager?.contains(testEntity) as Boolean
                assertTrue(attached, "Test entity must be attached to persistence unit")
            }
            // Retrieve entity form DB
            val connection = dataSource?.connection
            connection.use {
                val query = "select ID, TEXT, EVENT_DATE from TEST_ENTITIES t where t.ID = 2"
                val statement = it?.prepareStatement(query)
                statement.use {
                    val resultSet = it?.executeQuery()
                    resultSet.use {
                        if (it != null) {
                            if (it.next()) {
                                val id = it.getInt("ID")
                                assertEquals(2, id, "Retrieved persited id must be 2")
                                val text = it.getString("TEXT")
                                assertEquals("This is a changed text entity", text,
                                    "Retrieved persited text must be 'This is a changed text entity'")
                                val eventDate = it.getTimestamp("EVENT_DATE")
                                assertEquals("2022/03/15", dateFormat.format(eventDate),
                                    "Retrieved persited event date must be '2022/03/15'")
                            } else {
                                throw RuntimeException("Entity with id 0 not found")
                            }
                        } else {
                            throw RuntimeException("Result Set not initiated")
                        }
                    }
                }
            }
        }
    }

    //@Test
    fun shouldPerformDtoToEntitySingleArgumentNonPersitedEntity() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntityDto = TestEntityDto()
            testEntityDto.id = 20000 as Integer
            testEntityDto.text = "This is a non persisted text entity"
            testEntityDto.eventDate = dateFormat.parse("2022/03/12")
            val testEntity = testEntityDao.fromDtoToEntity(testEntityDto)
            //val identifier = entityManagerFactory?.persistenceUnitUtil?.getIdentifier(testEntity)
            val attached = entityManager?.contains(testEntity) as Boolean
            assertFalse(attached, "Test entity must NOT be attached to persistence unit")
            assertEquals(20000, testEntity.id, "Test entity id must be 20000")
            assertEquals("This is a non persisted text entity", testEntity.text,
                "Test entity text must be 'This is a non persisted text entity'")
            assertEquals("2022/03/12", dateFormat.format(testEntity.eventDate), "Test entity enent date must be '2022/03/12'")
        }
    }

    //@Test
    fun shouldPerformDtoToEntitySingleArgumentNonIdentifiedEntity() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntityDto = TestEntityDto()
            testEntityDto.text = "This is a non identified text entity"
            testEntityDto.eventDate = dateFormat.parse("2022/03/18")
            val testEntity = testEntityDao.fromDtoToEntity(testEntityDto)
            assertEquals(
                "This is a non identified text entity", testEntity.text,
                "Test entity text must be 'This is a non identified text entity'"
            )
            assertEquals(
                "2022/03/18",
                dateFormat.format(testEntity.eventDate),
                "Test entity enent date must be '2022/03/18'"
            )
        }
    }

    //@Test
    fun shouldPerformDtoToEntityDoubleArgumentCorrectly() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val testEntityDto = TestEntityDto()
            testEntityDto.id = 2 as Integer
            testEntityDto.text = "This is a new text entity"
            testEntityDto.eventDate = dateFormat.parse("2022/03/23")
            val testEntity = TestEntity()
            testEntityDao.fromDtoToEntity(testEntityDto, testEntity)
            assertEquals(2, testEntity.id, "Created entity id must be 2")
            assertEquals("This is a new text entity", testEntity.text,
                "Created entity text must be 'This is a new text entity'")
            assertEquals("2022/03/23", dateFormat.format(testEntity.eventDate),
                "Created entity event date must be '2022/03/23'")
        }
    }

    // DATA MANIPULATION TESTS

    //@Test
    fun shouldPerformAnInsertProperly() {
        assertNotNull(testEntityDao, "TestEntityDao must be created and injected")
        if (testEntityDao != null) {
            val entity = TestEntity()
            entity.text = "This is the third entity"
            entity.eventDate = dateFormat.parse("2022/03/08")
            testEntityDao.create(entity)
            assertEquals(3, entity.id, "Entity id must be asigned at creation to value 3")
            // Retrieve entity form DB
            val connection = dataSource?.connection
            connection.use { connection ->
                val query = "select ID, TEXT, EVENT_DATE from TEST_ENTITIES t where t.ID = 3"
                val statement = connection?.prepareStatement(query)
                statement.use { statement ->
                    val resultSet = statement?.executeQuery()
                    resultSet.use { resultSet ->
                        if (resultSet != null) {
                            if (resultSet.next()) {
                                val id = resultSet.getInt("ID")
                                assertEquals(3, id, "Retrieved id must be 3")
                                val text = resultSet.getString("TEXT")
                                assertEquals(
                                    "This is the third entity", text,
                                    "Retrieved text must be 'This is the third entity'"
                                )
                                val eventDate = resultSet.getTimestamp("EVENT_DATE")
                                assertEquals(
                                    "2022/03/08", dateFormat.format(eventDate),
                                    "Retrieved event date must be '2022/03/08'"
                                )
                            } else {
                                throw RuntimeException("Entity with id 3 not found")
                            }
                        } else {
                            throw RuntimeException("Result Set not initiated")
                        }
                    }
                }
            }
        }
    }

}