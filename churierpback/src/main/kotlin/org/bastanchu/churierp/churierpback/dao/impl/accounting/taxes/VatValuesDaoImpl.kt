package org.bastanchu.churierp.churierpback.dao.impl.accounting.taxes

import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatValueDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValue
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValuePk
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.collections.ArrayList

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class VatValuesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
      : BaseDtoDaoImpl<VatValuePk, VatValue, VatValueDto>(entityManager) ,
        VatValueDao {

    override fun getVatValueByCountryTypeAndDate(countryId: String, vatType: String, vatDate: Date) : VatValue {
        val sql = """
                    select v from VatValue v
                    where v.countryId = :countryId and v.vatId = :vatType 
                      and v.validFrom <= :vatDate 
                      and ((v.validTo is null) or (v.validTo >= :vatDate))
                  """.trimIndent()
        val query = entityManager.createQuery(sql);
        query.setParameter("countryId", countryId)
             .setParameter("vatType", vatType)
             .setParameter("vatDate", vatDate)
        val result = query.singleResult as VatValue
        return result
    }

    override fun getVatValuesInOverlapping(vatValue: VatValue): List<VatValue> {
        // Three overlaping cases considered (three unions)
        // 1.- Overlapping due to validFrom into an existing interval
        // 2.- Overlapping due to validTo into an existing interval
        // 3.- Overlapping due to not quoted end interval and there is an interval starting after validFrom
        val result = ArrayList<VatValue>()
        val partialResult1 = getVatValuesInOverlappingDueToValidFrom(vatValue)
        result.addAll(partialResult1)
        if (vatValue.validTo != null) {
            val partialResult2 = getVatValuesInOverlappingDueToValidTo(vatValue)
            result.addAll(partialResult2)
        } else {
            val partialResult3 = getVatValuesInOverlappingDueToNonQuotedInterval(vatValue)
            result.addAll(partialResult3)
        }
        return result
    }

    private fun getVatValuesInOverlappingDueToValidFrom(vatValue: VatValue): List<VatValue> {
        // 1.- Overlapping due to validFrom into an existing interval
        val sql = """                     
                     select v from VatValue v
                     where v.countryId = :countryId and v.vatId = :vatType
                       and v.validFrom <= :validFrom and ((v.validTo is null) or (:validFrom <= v.validTo))
                  """.trimIndent()
        val query = entityManager.createQuery(sql);
        query.setParameter("countryId", vatValue.countryId)
            .setParameter("vatType", vatValue.vatId)
            .setParameter("validFrom", vatValue.validFrom)
        val result = query.resultList as List<VatValue>
        return result
    }

    private fun getVatValuesInOverlappingDueToValidTo(vatValue: VatValue): List<VatValue> {
        // 2.- Overlapping due to validTo into an existing interval
        val sql = """                     
                     select v from VatValue v
                     where v.countryId = :countryId and v.vatId = :vatType
                       and v.validFrom <= :validTo and ((v.validTo is null) or (:validTo <= v.validTo))
                  """.trimIndent()
        val query = entityManager.createQuery(sql);
        query.setParameter("countryId", vatValue.countryId)
            .setParameter("vatType", vatValue.vatId)
            //.setParameter("validFrom", vatValue.validFrom)
            .setParameter("validTo", vatValue.validTo)
        val result = query.resultList as List<VatValue>
        return result
    }

    private fun getVatValuesInOverlappingDueToNonQuotedInterval(vatValue: VatValue): List<VatValue> {
        // 3.- Overlapping due to not quoted end interval and there is an interval starting after validFrom
        val sql = """
            select v from VatValue v
                     where v.countryId = :countryId and v.vatId = :vatType
                       and :validFrom <= v.validFrom
        """.trimIndent()
        val query = entityManager.createQuery(sql);
        query.setParameter("countryId", vatValue.countryId)
            .setParameter("vatType", vatValue.vatId)
            .setParameter("validFrom", vatValue.validFrom)
        val result = query.resultList as List<VatValue>
        return result
    }

}