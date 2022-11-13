package org.bastanchu.churierp.churierpback.service.accounting.taxes.impl

import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatTypeDao
import org.bastanchu.churierp.churierpback.dao.accounting.taxes.VatValueDao
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatTypePk
import org.bastanchu.churierp.churierpback.service.accounting.taxes.VatService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class VatServiceImpl(@Autowired val vatTypeDao: VatTypeDao,
                     @Autowired val vatValueDao: VatValueDao) : VatService {

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
}