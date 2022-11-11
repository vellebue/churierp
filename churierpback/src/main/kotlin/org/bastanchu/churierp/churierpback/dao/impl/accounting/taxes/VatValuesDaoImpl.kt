package org.bastanchu.churierp.churierpback.dao.impl.accounting.taxes

import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatValueDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValue
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValuePk
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

class VatValuesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
      : BaseDtoDaoImpl<VatValuePk, VatValue, VatValueDto>(entityManager) ,
        VatValueDao {
}