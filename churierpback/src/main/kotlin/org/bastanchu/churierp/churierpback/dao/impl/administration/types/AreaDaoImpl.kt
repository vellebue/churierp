package org.bastanchu.churierp.churierpback.dao.impl.administration.types

import org.bastanchu.churierp.churierpback.dao.administration.types.AreaDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.types.AreaDto
import org.bastanchu.churierp.churierpback.entity.administration.types.Area
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class AreaDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
                : BaseDtoDaoImpl<Integer, Area, AreaDto>(entityManager)
                , AreaDao {
}