package org.bastanchu.churierp.churierpback.service.accounting.taxes.impl

import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatTypeDao
import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatValueDao
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatTypePk
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValuePk
import org.bastanchu.churierp.churierpback.service.accounting.taxes.VatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.collections.HashMap

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class VatServiceImpl(@Autowired private val vatTypeDao: VatTypeDao,
                     @Autowired private val vatValueDao: VatValueDao) : VatService {

    override fun getVatTypes(): List<VatTypeDto> {
        return  vatTypeDao.toDataTransferObjectList(vatTypeDao.listAll());
    }

    override fun getVatType(countryId: String, vatType: String):VatTypeDto? {
        val pk = VatTypePk(countryId, vatType)
        val vatType = vatTypeDao.getById(pk)
        if (vatType != null) {
            return vatTypeDao.toDataTransferObject(vatType)
        } else {
            return null
        }
    }

    override fun createVatType(vatTypeDto: VatTypeDto) {
        val vatType = vatTypeDao.fromDtoToEntity(vatTypeDto)
        vatTypeDao.create(vatType)
    }

    override fun updateVatType(vatTypeDto: VatTypeDto) {
        val vatType = vatTypeDao.fromDtoToEntity(vatTypeDto)
        vatTypeDao.flush()
    }

    override fun deleteVatType(countryId: String, vatType: String) {
        val pk = VatTypePk(countryId, vatType)
        vatTypeDao.deleteById(pk)
    }

    override fun getVatTypesMap(): Map<String, Map<String, String>> {
        val vatTypesMapByCountry = HashMap<String, MutableMap<String, String>>()
        val vatTypesList = getVatTypes()
        vatTypesList.forEach {
            val countryId = it.countryId!!
            var countryMap = vatTypesMapByCountry.get(countryId)
            if (countryMap == null) {
                countryMap = HashMap<String, String>()
                vatTypesMapByCountry.put(countryId, countryMap)
            }
            countryMap.put(it.vatId!!, it.description!!)
        }
        return vatTypesMapByCountry
    }

    override fun getVatValues(): List<VatValueDto> {
        return vatValueDao.toDataTransferObjectList(vatValueDao.listAll());
    }

    override fun getVatValueByCountryTypeAndDate(countryId: String, vatType: String, vatDate: Date): VatValueDto {
        val vatValue = vatValueDao.getVatValueByCountryTypeAndDate(countryId, vatType, vatDate)
        return vatValueDao.toDataTransferObject(vatValue)
    }

    override fun getVatValueOverlapping(vatValueDto: VatValueDto): List<VatValueDto> {
        return vatValueDao.toDataTransferObjectList(
                              vatValueDao.getVatValuesInOverlapping(
                                  vatValueDao.fromDtoToEntity(vatValueDto)))
    }

    override fun getVatValue(countryId: String, vatType: String, validFrom: Date): VatValueDto? {
        val pk = VatValuePk(countryId, vatType, validFrom)
        val vatValue = vatValueDao.getById(pk)
        if (vatValue != null) {
            return vatValueDao.toDataTransferObject(vatValue)
        } else {
            return null
        }
    }

    override fun createVatValue(vatValueDto: VatValueDto) {
        val vatType = vatValueDao.fromDtoToEntity(vatValueDto)
        vatValueDao.create(vatType)
    }

    override fun updateVatValue(vatValueDto: VatValueDto) {
        val vatType = vatValueDao.fromDtoToEntity(vatValueDto)
        vatValueDao.flush()
    }

    override fun deleteVatValue(vatValueDto: VatValueDto) {
        val vatType = vatValueDao.fromDtoToEntity(vatValueDto)
        vatValueDao.delete(vatType)
    }
}