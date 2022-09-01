package org.bastanchu.churierp.churierpback.`dao,impl`.administration.companies

import org.bastanchu.churierp.churierpback.dao.impl.administration.companies.CompaniesDaoImpl
import org.bastanchu.churierp.churierpback.entity.Address
import org.bastanchu.churierp.churierpback.entity.companies.Company

import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals

import org.easymock.EasyMock.createMock

import javax.persistence.*
import javax.persistence.criteria.*
import javax.persistence.metamodel.*



@Tag("unitTest")
class CompaniesDaoImplTest {

    @Test
    fun shouldConvertFromEntityToDtoProperly() {
        val dao = CompaniesDaoImpl(createMock(EntityManager::class.java))
        val companyEntity = Company()
        companyEntity.companyId = 0 as Integer
        companyEntity.name = "John Doe Solutions"
        companyEntity.socialName = "John Doe Solutions GmbH"
        companyEntity.vatNumber = "H8625635"
        companyEntity.address = Address()
        companyEntity.address!!.addressId = 0 as Integer
        companyEntity.address!!.address  = "204 Howard St San Francisco CA"
        companyEntity.address!!.city = "San Francisco"
        companyEntity.address!!.postalCode = "94105"
        companyEntity.address!!.countryId = "US"
        companyEntity.address!!.regionId = "CA"
        companyEntity.address!!.type = Address.AddressType.ADDRESS_TYPE_COMPANY.toString()
        val companyDto = dao.toDataTransferObject(companyEntity)
        assertEquals(0 as Integer, companyDto.companyId)
        assertEquals("John Doe Solutions", companyDto.name)
        assertEquals("John Doe Solutions GmbH", companyDto.socialName)
        assertEquals("H8625635", companyDto.vatNumber)
        assertEquals(0 as Integer, companyDto.addressId)
        assertEquals("204 Howard St San Francisco CA", companyDto.address)
        assertEquals("San Francisco", companyDto.city)
        assertEquals("94105", companyDto.postalCode)
        assertEquals("US", companyDto.countryId)
        assertEquals("CA", companyDto.regionId)
        assertEquals(Address.AddressType.ADDRESS_TYPE_COMPANY.toString(), companyDto.type)
    }
}