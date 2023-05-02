package org.bastanchu.churierp.churierpback.dao.accounting.taxes

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValue
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValuePk
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import java.math.BigDecimal
import java.util.*
import javax.sql.DataSource
import org.junit.jupiter.api.Assertions.*
import java.text.SimpleDateFormat

//@DataJpaTest
class VatValueDaoITCase(@Autowired val vatValueDao: VatValueDao) : BaseContainerDBITCase() {

    override fun getScriptContent(): String {
        return """            
            INSERT INTO c_countries (country_id, name, key) VALUES ('ES', 'Spain', 'churierpweb.country.ES');
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','NR','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Normal');  
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','EX','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Exento');
            INSERT INTO vat_values (country_id, vat_id, valid_from, valid_to, 
                                    creation_user, creation_time, update_user, update_time,
                                    percentage, upcharge)
                   VALUES ('ES', 'EX', '2020/01/01', null, 
                           'angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746',
                           '0.1','0.0');
            COMMIT;
        """.trimIndent()
    }

    @Test
    fun shouldPerformVatValueInsertProperly() {
        println("Performing shouldPerformVatValueInsertProperly")
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val referenceDate = dateFormat.parse(dateFormat.format(Date()))
        val vatValue = VatValue()
        vatValue.countryId = "ES"
        vatValue.vatId = "NR"
        vatValue.validFrom = referenceDate
        vatValue.validTo = referenceDate
        vatValue.percentage = BigDecimal(21.5)
        vatValue.upcharge = BigDecimal(5.2)
        vatValueDao.create(vatValue)
        vatValueDao.flush()
        val bdVatValue = retrieveVatValue(vatValue!!.countryId!!, vatValue!!.vatId!!)
        assertNotNull(bdVatValue)
        assertEquals(vatValue.countryId, bdVatValue!!.countryId)
        assertEquals(vatValue.vatId, bdVatValue!!.vatId)
        assertEquals(vatValue.validFrom, bdVatValue.validFrom)
        assertEquals(vatValue.validTo, bdVatValue.validTo)
        assertEquals(vatValue.percentage!!.toDouble(), bdVatValue.percentage!!.toDouble())
        val expectedUpcharge = vatValue.upcharge!!.toDouble()
        assertNotNull(bdVatValue.upcharge)
        val retrievedUpcharge = bdVatValue.upcharge!!.toDouble()
        assertEquals(expectedUpcharge, retrievedUpcharge)
    }

    @Test
    fun shouldPerformVatValueDeleteProperly() {
        println("Performing shouldPerformVatValueDeleteProperly")
        val previousVATRegistry = retrieveVatValue("ES", "EX")
        assertNotNull(previousVATRegistry)
        vatValueDao.delete(previousVATRegistry!!)
        val laterNonExistingVATRegistry = retrieveVatValue("ES", "EX")
        assertTrue(laterNonExistingVATRegistry == null)
    }

    private fun retrieveVatValue(countryId : String, vatId : String) : VatValue?{
        val sql = """
            select country_id, vat_id, valid_from, valid_to, creation_user, creation_time, update_user, update_time, percentage, upcharge
              from vat_values
             where country_id = ? and vat_id = ? 
        """.trimIndent()
        val conn = dataSource!!.connection
        conn.use {
            val statement = it.prepareStatement(sql)
            statement.setString(1, countryId)
            statement.setString(2, vatId)
            statement.use {
                val resultSet = it.executeQuery()
                resultSet.use {
                    if (it.next()) {
                        val vatValue = VatValue()
                        vatValue.countryId = countryId
                        vatValue.vatId = vatId
                        vatValue.validFrom = it.getTimestamp("valid_from")
                        vatValue.validTo = it.getTimestamp("valid_to")
                        vatValue.creationUser = it.getString("creation_user")
                        vatValue.creationTime = it.getTimestamp("creation_time")
                        vatValue.updateUser = it.getString("update_user")
                        vatValue.updateTime = it.getTimestamp("update_time")
                        vatValue.percentage = it.getBigDecimal("percentage")
                        vatValue.upcharge = it.getBigDecimal("upcharge")
                        return vatValue
                    } else {
                        return null
                    }
                }
            }

        }
    }
}