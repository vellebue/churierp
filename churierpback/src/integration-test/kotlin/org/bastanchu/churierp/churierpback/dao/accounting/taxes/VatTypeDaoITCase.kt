package org.bastanchu.churierp.churierpback.dao.accounting.taxes

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatType
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatTypePk
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

import org.junit.jupiter.api.Assertions.*
import java.text.SimpleDateFormat
import java.util.*

class VatTypeDaoITCase(@Autowired val vatTypeDao: VatTypeDao) :BaseContainerDBITCase() {

    companion object {

        class VatTypeFilter(
            @FormField(groupId = 1, indexInGroup = 1)
            val countryId : String? = null,
            @FormField(groupId = 1, indexInGroup = 2)
            val creationUser : String? = null,
            @FormField(groupId = 1, indexInGroup = 3)
            val vatId : String? = null,
            @FormField(groupId = 1, indexInGroup = 4, field = "updateTime", from = true)
            val updateTimeFrom : Date? = null,
            @FormField(groupId = 1, indexInGroup = 4, field = "updateTime", to = true)
            val updateTimeTo : Date? = null
        )
    }

    override fun getScriptContent(): String {
        return """
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','NR','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Normal');  
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','RD','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Reducido');
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','SR','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Superreducido');
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','EX','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Exento');
        """.trimIndent()
    }

    @Test
    fun shouldListAllItemsProperly() {
        val vatTypes = vatTypeDao.listAll()
        assertEquals(4, vatTypes.size)
        val vatTypesMap = fromListToMap(vatTypes)
        assertAll(vatTypesMap)
    }

    @Test
    fun shouldFilterAllItemsProperly() {
        var vatTypeFilter = VatType()
        vatTypeFilter.countryId = "ES"
        vatTypeFilter.creationUser = "angel"
        val vatTypes = vatTypeDao.filter(vatTypeFilter)
        val vatTypesMap = fromListToMap(vatTypes)
        assertAll(vatTypesMap)
    }

    @Test
    fun shouldFilterGenericSelectedItemsProperly() {
        var vatTypeFilter = VatTypeFilter("ES", "angel", "*R")
        val vatTypes = vatTypeDao.genericFilter(vatTypeFilter)
        assertEquals(2, vatTypes.size)
    }

    @Test
    fun shouldFilterGenericSelectedItemsFromDateProperly() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        var vatTypeFilterWithData = VatTypeFilter(updateTimeFrom = dateFormat.parse("2022/11/11"))
        val vatTypesWithData = vatTypeDao.genericFilter(vatTypeFilterWithData)
        assertEquals(4, vatTypesWithData.size)
        var vatTypeFilterWithoutData = VatTypeFilter(updateTimeFrom = dateFormat.parse("2022/11/14"))
        val vatTypesWithoutData = vatTypeDao.genericFilter(vatTypeFilterWithoutData)
        assertEquals(0, vatTypesWithoutData.size)
    }

    @Test
    fun shouldFilterGenericSelectedItemsToDateProperly() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        var vatTypeFilterWithData = VatTypeFilter(updateTimeTo = dateFormat.parse("2022/11/11"))
        val vatTypesWithData = vatTypeDao.genericFilter(vatTypeFilterWithData)
        assertEquals(0, vatTypesWithData.size)
        var vatTypeFilterWithoutData = VatTypeFilter(updateTimeTo = dateFormat.parse("2022/11/14"))
        val vatTypesWithoutData = vatTypeDao.genericFilter(vatTypeFilterWithoutData)
        assertEquals(4, vatTypesWithoutData.size)
    }

    @Test
    fun shouldGetByIdReturnVatTypeProperly() {
        val pk = VatTypePk()
        pk.countryId = "ES"
        pk.vatId = "NR"
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val vatValue = vatTypeDao.getById(pk)
        assertEquals("ES", vatValue.countryId)
        assertEquals("NR", vatValue.vatId)
        assertEquals("angel", vatValue.creationUser)
        assertEquals("2022-11-13", dateFormat.format(vatValue.creationTime))
        assertEquals("angel", vatValue.updateUser)
        assertEquals("2022-11-13", dateFormat.format(vatValue.updateTime))
        assertEquals("Normal", vatValue.description)
    }

    fun assertAll(vatTypesMap : Map<String, VatType>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        vatTypesMap.values.forEach {
            when (it.vatId) {
                "NR" -> {
                    assertEquals("ES", it.countryId)
                    assertEquals("NR", it.vatId)
                    assertEquals("angel", it.creationUser)
                    assertEquals("2022-11-13", dateFormat.format(it.creationTime))
                    assertEquals("angel", it.updateUser)
                    assertEquals("2022-11-13", dateFormat.format(it.updateTime))
                    assertEquals("Normal", it.description)
                }
                "RD" -> {
                    assertEquals("ES", it.countryId)
                    assertEquals("RD", it.vatId)
                    assertEquals("angel", it.creationUser)
                    assertEquals("2022-11-13", dateFormat.format(it.creationTime))
                    assertEquals("angel", it.updateUser)
                    assertEquals("2022-11-13", dateFormat.format(it.updateTime))
                    assertEquals("Reducido", it.description)
                }
                "SR" -> {
                    assertEquals("ES", it.countryId)
                    assertEquals("SR", it.vatId)
                    assertEquals("angel", it.creationUser)
                    assertEquals("2022-11-13", dateFormat.format(it.creationTime))
                    assertEquals("angel", it.updateUser)
                    assertEquals("2022-11-13", dateFormat.format(it.updateTime))
                    assertEquals("Superreducido", it.description)
                }
                "EX" -> {
                    assertEquals("ES", it.countryId)
                    assertEquals("EX", it.vatId)
                    assertEquals("angel", it.creationUser)
                    assertEquals("2022-11-13", dateFormat.format(it.creationTime))
                    assertEquals("angel", it.updateUser)
                    assertEquals("2022-11-13", dateFormat.format(it.updateTime))
                    assertEquals("Exento", it.description)
                }
            }
        }
    }

    fun fromListToMap(vatTypes : List<VatType>) : Map<String, VatType> {
        return vatTypes.associate { it.vatId!! to it };
    }
}