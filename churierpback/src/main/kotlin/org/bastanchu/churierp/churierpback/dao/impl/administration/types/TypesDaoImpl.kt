package org.bastanchu.churierp.churierpback.dao.impl.administration.types

import org.bastanchu.churierp.churierpback.dao.administration.types.TypesDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.types.TypeDto
import org.bastanchu.churierp.churierpback.entity.administration.types.Type
import org.bastanchu.churierp.churierpback.entity.administration.types.TypePK
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class TypesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
    : BaseDtoDaoImpl<TypePK, Type, TypeDto>(entityManager) ,
    TypesDao {
}