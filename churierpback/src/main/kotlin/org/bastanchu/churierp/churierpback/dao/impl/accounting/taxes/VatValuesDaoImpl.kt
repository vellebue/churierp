package org.bastanchu.churierp.churierpback.dao.impl.accounting.taxes

import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatValueDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValue
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValuePk
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class VatValuesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
      : BaseDtoDaoImpl<VatValuePk, VatValue, VatValueDto>(entityManager) ,
        VatValueDao {
}