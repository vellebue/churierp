package org.bastanchu.churierp.churierpback.dao.impl

import org.bastanchu.churierp.churierpback.dao.TestEntityDao
import org.bastanchu.churierp.churierpback.dto.TestEntityDto
import org.bastanchu.churierp.churierpback.entity.TestEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Deprecated("New Test structure developed")
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class TestEntityDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
    : BaseDtoDaoImpl<Integer, TestEntity, TestEntityDto>(entityManager) , TestEntityDao {
}