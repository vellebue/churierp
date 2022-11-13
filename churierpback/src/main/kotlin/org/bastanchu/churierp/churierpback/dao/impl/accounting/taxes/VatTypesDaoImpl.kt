package org.bastanchu.churierp.churierpback.dao.impl.accounting.taxes

import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatTypeDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatType
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatTypePk
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class VatTypesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
    : BaseDtoDaoImpl<VatTypePk, VatType, VatTypeDto>(entityManager) ,
      VatTypeDao {
}