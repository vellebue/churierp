package org.bastanchu.churierp.churierpback.service.accounting.taxes

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import org.springframework.beans.factory.annotation.Autowired

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class VatServiceUpdatesITCase(@Autowired val vatService :VatService) : BaseContainerDBITCase() {

    private val dateFormat = SimpleDateFormat("yyyy/MM/dd")

    override fun getScriptContent(): String {
        return """
            INSERT INTO c_countries (country_id, name, key) VALUES ('ES', 'Spain', 'churierpweb.country.ES');
            --VAT types
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','NR','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Normal Updateable');
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','DL','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Normal Deleteable');
            -- VAT Values
            -- To Update
            INSERT INTO vat_values (country_id, vat_id, valid_from, valid_to, 
                                    creation_user, creation_time, update_user, update_time,
                                    percentage, upcharge)
                   VALUES ('ES', 'NR', '1995/01/01', '2010/06/30', 
                           'angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746',
                           '16','2.4');
            -- To Delete
            INSERT INTO vat_values (country_id, vat_id, valid_from, valid_to, 
                                    creation_user, creation_time, update_user, update_time,
                                    percentage, upcharge)
                   VALUES ('ES', 'NR', '2010/07/01', '2012/08/31', 
                           'angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746',
                           '18','3.2');
        """.trimIndent()
    }

    @Test
    fun shouldCreateVatTypePerformProperly() {
        val vatTypeToCreate = VatTypeDto()
        vatTypeToCreate.countryId = "ES"
        vatTypeToCreate.vatId = "NW"
        vatTypeToCreate.description = "New VAT Type"
        vatService.createVatType(vatTypeToCreate)
        val vatTypeDB = retrieveVatTypeFromDB(vatTypeToCreate.countryId!!, vatTypeToCreate.vatId!!)
        assertNotNull(vatTypeDB)
        assertEquals(vatTypeToCreate.countryId, vatTypeDB!!.countryId)
        assertEquals(vatTypeToCreate.vatId, vatTypeDB!!.vatId)
        assertEquals(vatTypeToCreate.description, vatTypeDB!!.description)
    }

    @Test
    fun shouldUpdateVatTypePerformProperly() {
        val vatTypeToUpdate = VatTypeDto()
        vatTypeToUpdate.countryId = "ES"
        vatTypeToUpdate.vatId = "NR"
        vatTypeToUpdate.description = "Updated Type"
        vatService.updateVatType(vatTypeToUpdate)
        val vatTypeDB = retrieveVatTypeFromDB(vatTypeToUpdate.countryId!!, vatTypeToUpdate.vatId!!)
        assertNotNull(vatTypeDB)
        assertEquals(vatTypeToUpdate.countryId, vatTypeDB!!.countryId)
        assertEquals(vatTypeToUpdate.vatId, vatTypeDB!!.vatId)
        assertEquals(vatTypeToUpdate.description, vatTypeDB!!.description)
    }

    @Test
    fun shouldDeleteVatTypePerformProperly() {
        val vatTypeToDelete = VatTypeDto()
        vatTypeToDelete.countryId = "ES"
        vatTypeToDelete.vatId = "DL"
        vatService.deleteVatType(vatTypeToDelete.countryId!!, vatTypeToDelete.vatId!!)
        val vatTypeDB = retrieveVatTypeFromDB(vatTypeToDelete.countryId!!, vatTypeToDelete.vatId!!)
        assertNull(vatTypeDB)
    }

    @Test
    fun shouldCreateVatValuePerformProperly() {
        val vatValueToCreate = VatValueDto()
        vatValueToCreate.countryId = "ES"
        vatValueToCreate.vatId = "NR"
        vatValueToCreate.validFrom = dateFormat.parse("1985/01/01")
        vatValueToCreate.validTo = dateFormat.parse("2010/06/30")
        vatValueToCreate.percentage = BigDecimal(14.0)
        vatValueToCreate.upcharge = BigDecimal(2.7)
        vatService.createVatValue(vatValueToCreate)
        val vatValueDB = retrieveVatValueFromDB(vatValueToCreate.countryId!!, vatValueToCreate.vatId!!,
            vatValueToCreate.validFrom!!)
        assertNotNull(vatValueDB)
        assertEquals(vatValueToCreate.countryId, vatValueDB?.countryId)
        assertEquals(vatValueToCreate.vatId, vatValueDB?.vatId)
        assertEquals(vatValueToCreate.validFrom, vatValueDB?.validFrom)
        assertEquals(vatValueToCreate.validTo, vatValueDB?.validTo)
        assertEquals(vatValueToCreate.percentage!!.toDouble(), vatValueDB?.percentage!!.toDouble())
        assertEquals(vatValueToCreate.upcharge!!.toDouble(), vatValueDB?.upcharge!!.toDouble())
    }

    @Test
    fun shouldUpdateVatValuePerformProperly() {
        val vatValueToUpdate = VatValueDto()
        vatValueToUpdate.countryId = "ES"
        vatValueToUpdate.vatId = "NR"
        vatValueToUpdate.validFrom = dateFormat.parse("1995/01/01")
        vatValueToUpdate.validTo = dateFormat.parse("1994/12/31")
        vatValueToUpdate.percentage = BigDecimal(17.0)
        vatValueToUpdate.upcharge = BigDecimal(2.8)
        vatService.updateVatValue(vatValueToUpdate)
        val vatValueDB = retrieveVatValueFromDB(vatValueToUpdate.countryId!!, vatValueToUpdate.vatId!!,
            vatValueToUpdate.validFrom!!)
        assertNotNull(vatValueDB)
        assertEquals(vatValueToUpdate.countryId, vatValueDB?.countryId)
        assertEquals(vatValueToUpdate.vatId, vatValueDB?.vatId)
        assertEquals(vatValueToUpdate.validFrom, vatValueDB?.validFrom)
        assertEquals(vatValueToUpdate.validTo, vatValueDB?.validTo)
        assertEquals(vatValueToUpdate.percentage!!.toDouble(), vatValueDB?.percentage!!.toDouble())
        assertEquals(vatValueToUpdate.upcharge!!.toDouble(), vatValueDB?.upcharge!!.toDouble())
    }

    @Test
    fun shouldDeleteVatValuePerformProperly() {
        val vatValueToDelete = VatValueDto()
        vatValueToDelete.countryId = "ES"
        vatValueToDelete.vatId = "NR"
        vatValueToDelete.validFrom = dateFormat.parse("2010/07/01")
        vatService.deleteVatValue(vatValueToDelete)
        val vatValueDB = retrieveVatValueFromDB(vatValueToDelete.countryId!!, vatValueToDelete.vatId!!,
            vatValueToDelete.validFrom!!)
        assertNull(vatValueDB)
    }

    private fun retrieveVatTypeFromDB(countryId : String, vatId :String) : VatTypeDto? {
        val sql = """
            select country_id,vat_id,description
            from vat_types
            where country_id = ? and vat_id = ?
        """.trimIndent()
        var vatTypeDto : VatTypeDto? = null
        val conn = dataSource!!.connection
        conn.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, countryId)
            statement.setString(2, vatId)
            statement.use {
                val resultSet = it.executeQuery()
                resultSet.use {
                    if (it.next()) {
                        vatTypeDto = VatTypeDto()
                        vatTypeDto!!.countryId = it.getString("country_id")
                        vatTypeDto!!.vatId = it.getString("vat_id")
                        vatTypeDto!!.description = it.getString("description")
                    }
                }
            }
        }
        return vatTypeDto
    }

    private fun retrieveVatValueFromDB(countryId : String, vatId :String, validFrom : Date) : VatValueDto? {
        val sql = """
            select country_id, vat_id, valid_from, valid_to, percentage, upcharge
            from vat_values
            where country_id = ? and vat_id = ? and valid_from = ?
        """.trimIndent()
        var vatValueDto : VatValueDto? = null
        val conn = dataSource!!.connection
        conn.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, countryId)
            statement.setString(2, vatId)
            statement.setTimestamp(3, Timestamp(validFrom.time))
            statement.use {
                val resultSet = it.executeQuery()
                resultSet.use {
                    if (it.next()) {
                        vatValueDto = VatValueDto()
                        vatValueDto!!.countryId = it.getString("country_id")
                        vatValueDto!!.vatId = it.getString("vat_id")
                        vatValueDto!!.validFrom = it.getTimestamp("valid_from")
                        vatValueDto!!.validTo = it.getTimestamp("valid_to")
                        vatValueDto!!.percentage = it.getBigDecimal("percentage")
                        vatValueDto!!.upcharge = it.getBigDecimal("upcharge")
                    }
                }
            }
        }
        return vatValueDto
    }
}