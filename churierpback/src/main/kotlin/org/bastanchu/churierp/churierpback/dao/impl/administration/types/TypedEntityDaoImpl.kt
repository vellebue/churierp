package org.bastanchu.churierp.churierpback.dao.impl.administration.types

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dao.administration.types.TypedEntityDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.types.TypedEntityDto
import org.bastanchu.churierp.churierpback.entity.administration.types.TypedEntity
import org.bastanchu.churierp.churierpback.entity.administration.types.TypedEntityPK
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class TypedEntityDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
                       : BaseDtoDaoImpl<TypedEntityPK, TypedEntity, TypedEntityDto>(entityManager)
                       , TypedEntityDao {
}