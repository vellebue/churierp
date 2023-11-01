package org.bastanchu.churierp.churierpback.service.accounting.taxes

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

import org.junit.jupiter.api.Assertions.*
import java.text.SimpleDateFormat
import java.util.stream.Collectors

class VatServiceITCase(@Autowired val vatService :VatService) : BaseContainerDBITCase() {

    override fun getScriptContent(): String {
        return """
            --VAT Types
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','NR','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Normal');  
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','RD','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Reducido');
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','SR','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Superreducido');
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('ES','EX','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','Exento');
            INSERT INTO vat_types (country_id,vat_id,creation_user,creation_time,update_user,update_time,description) VALUES
            	 ('GE','NR','angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746','German Standard Type');
            --VAT Values
            INSERT INTO vat_values (country_id, vat_id, valid_from, valid_to, 
                                    creation_user, creation_time, update_user, update_time,
                                    percentage, upcharge)
                   VALUES ('ES', 'NR', '1995/01/01', '2010/06/30', 
                           'angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746',
                           '16','2.4');
            INSERT INTO vat_values (country_id, vat_id, valid_from, valid_to, 
                                    creation_user, creation_time, update_user, update_time,
                                    percentage, upcharge)
                   VALUES ('ES', 'NR', '2010/07/01', '2012/08/31', 
                           'angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746',
                           '18','3.2');
            INSERT INTO vat_values (country_id, vat_id, valid_from, valid_to, 
                                    creation_user, creation_time, update_user, update_time,
                                    percentage, upcharge)
                   VALUES ('ES', 'NR', '2012/09/01', null, 
                           'angel','2022-11-13 23:10:07.746','angel','2022-11-13 23:10:07.746',
                           '21','5.2');
            COMMIT;
        """.trimIndent()
    }

    @Test
    fun shouldGetVatTypeReturnARegisteredVatTypeProperly() {
        val vatType = vatService.getVatType("ES", "NR")
        assertEquals("ES", vatType?.countryId)
        assertEquals("NR", vatType?.vatId)
        assertEquals("Normal", vatType!!.description)
    }

    @Test
    fun shouldGetVatTypeReturnNullWhenNotRegisteredVatTypeProperly() {
        val vatType = vatService.getVatType("PT", "NR")
        assertNull(vatType)
    }

    @Test
    fun shouldGetVatTypesMapProperly() {
        val vatTypesMap = vatService.getVatTypesMap()
        val countriesKeySet = vatTypesMap.keys
        assertEquals(2, countriesKeySet.size)
        assertTrue(countriesKeySet.contains("ES"))
        assertTrue(countriesKeySet.contains("GE"))
        val spainMap = vatTypesMap.get("ES")!!
        val germanyMap = vatTypesMap.get("GE")!!
        assertEquals("Normal", spainMap.get("NR"))
        assertEquals("Reducido", spainMap.get("RD"))
        assertEquals("Superreducido", spainMap.get("SR"))
        assertEquals("Exento", spainMap.get("EX"))
        assertEquals("German Standard Type", germanyMap.get("NR"))
    }

    @Test
    fun shouldGetAllVatValuesProperly() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val allVatValues = vatService.getVatValues()
        val sortedVatValues = allVatValues.stream().sorted( { a : VatValueDto, b : VatValueDto->
            (b.validFrom!!.time - a!!.validFrom!!.time).toInt()
        }).collect(Collectors.toList())
        val first = sortedVatValues[0]
        val second = sortedVatValues[1]
        val third = sortedVatValues[2]
        assertEquals("ES", first.countryId)
        assertEquals("NR", first.vatId)
        assertEquals("1995/01/01", dateFormat.format(first.validFrom))
        assertEquals("2010/06/30", dateFormat.format(first.validTo))
        assertEquals(16.0, first.percentage?.toDouble())
        assertEquals(2.4, first.upcharge?.toDouble())
        assertEquals("ES", second.countryId)
        assertEquals("NR", second.vatId)
        assertEquals("2010/07/01", dateFormat.format(second.validFrom))
        assertEquals("2012/08/31", dateFormat.format(second.validTo))
        assertEquals(18.0, second.percentage?.toDouble())
        assertEquals(3.2, second.upcharge?.toDouble())
        assertEquals("ES", third.countryId)
        assertEquals("NR", third.vatId)
        assertEquals("2012/09/01", dateFormat.format(third.validFrom))
        assertNull(third.validTo)
        assertEquals(21.0, third.percentage?.toDouble())
        assertEquals(5.2, third.upcharge?.toDouble())
    }

    @Test
    fun shouldGetVatValueByCountryTypeAndDateProperly() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val vatValue = vatService.getVatValue("ES", "NR", dateFormat.parse("2010/07/01"))
                as VatValueDto
        assertEquals("ES", vatValue.countryId)
        assertEquals("NR", vatValue.vatId)
        assertEquals("2010/07/01", dateFormat.format(vatValue.validFrom))
        assertEquals("2012/08/31", dateFormat.format(vatValue.validTo))
        assertEquals(18.0, vatValue.percentage?.toDouble())
        assertEquals(3.2, vatValue.upcharge?.toDouble())
    }

    @Test
    fun shouldNotGetVatValueByCountryTypeAndDateWhenNotRegistered() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val vatValue = vatService.getVatValue("ES", "NR", dateFormat.parse("2014/07/01"))
        assertNull(vatValue)
    }

    @Test
    fun shouldGetVatValueByCountryTypeAndDateForAClosedPeriod() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val vatValue = vatService.getVatValueByCountryTypeAndDate("ES", "NR", dateFormat.parse("2000/07/01"))
            as VatValueDto
        assertEquals(16.0, vatValue.percentage?.toDouble())
        assertEquals(2.4, vatValue.upcharge?.toDouble())
    }

    @Test
    fun shouldGetVatValueByCountryTypeAndDateForAnOpenPeriod() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val vatValue = vatService.getVatValueByCountryTypeAndDate("ES", "NR", dateFormat.parse("2040/07/01"))
                as VatValueDto
        assertEquals(21.0, vatValue.percentage?.toDouble())
        assertEquals(5.2, vatValue.upcharge?.toDouble())
    }

    @Test
    fun getVatValueOverlappingsForAnOpenInterval() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val vatValueTestOverlapping = VatValueDto()
        vatValueTestOverlapping.countryId = "ES"
        vatValueTestOverlapping.vatId = "NR"
        vatValueTestOverlapping.validFrom = dateFormat.parse("1985/01/01")
        val vayValuesOverlapping = vatService.getVatValueOverlapping(vatValueTestOverlapping)
        assertEquals(3, vayValuesOverlapping.size)
        val first = vayValuesOverlapping[0]
        val second = vayValuesOverlapping[1]
        val third = vayValuesOverlapping[2]
        assertEquals(16.0, first.percentage?.toDouble())
        assertEquals(18.0, second.percentage?.toDouble())
        assertEquals(21.0, third.percentage?.toDouble())
    }

    @Test
    fun getVatValueOverlappingsForAClosedInterval() {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd")
        val vatValueTestOverlapping = VatValueDto()
        vatValueTestOverlapping.countryId = "ES"
        vatValueTestOverlapping.vatId = "NR"
        vatValueTestOverlapping.validFrom = dateFormat.parse("1985/01/01")
        vatValueTestOverlapping.validTo = dateFormat.parse("2011/01/01")
        val vatValuesOverlapping = vatService.getVatValueOverlapping(vatValueTestOverlapping)
        assertEquals(2, vatValuesOverlapping.size)
        val first = vatValuesOverlapping[0]
        val second = vatValuesOverlapping[1]
        assertEquals(16.0, first.percentage?.toDouble())
        assertEquals(18.0, second.percentage?.toDouble())
    }
}