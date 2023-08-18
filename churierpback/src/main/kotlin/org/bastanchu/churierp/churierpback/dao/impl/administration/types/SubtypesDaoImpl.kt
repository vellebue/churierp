package org.bastanchu.churierp.churierpback.dao.impl.administration.types

import org.bastanchu.churierp.churierpback.dao.administration.types.SubtypesDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.types.SubtypeDto
import org.bastanchu.churierp.churierpback.entity.administration.types.Subtype
import org.bastanchu.churierp.churierpback.entity.administration.types.SubtypePK
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class SubtypesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
    : BaseDtoDaoImpl<SubtypePK, Subtype, SubtypeDto>(entityManager) ,
      SubtypesDao {
}