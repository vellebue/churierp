package org.bastanchu.churierp.churierpback.service.accounting.taxes

import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import java.util.*

interface VatService {

    fun getVatTypes() : List<VatTypeDto>

    fun getVatType(countryId : String, vatType : String) : VatTypeDto?

    fun createVatType(vatTypeDto : VatTypeDto)

    fun updateVatType(vatTypeDto :VatTypeDto)

    fun deleteVatType(countryId : String, vatType : String)

    /**
     * Gets VAT types organized by country ISO id.
     *
     * @return A map whose key (main map key) is a country ISO id. Each country key maps
     *         a surrogate map that contains VAT type -> VAT Description key maps
     *         for that country.
     */
    fun getVatTypesMap() : Map<String, Map<String, String>>

    fun getVatValues() : List<VatValueDto>

    /**
     * Gets VAT Value appliable for a country, VAT type and a reference date (e.g. invoice date)
     *
     * @param countryId Country ID for vat.
     * @param vatType VAT type to query for.
     * @param vatDate VAT date to query which is the active VAT Value at this date.
     *
     * @return An active VAT value under given parameters or null if there is no active VAT value
     *         for that given country, VAT ID and date.
     */
    fun getVatValueByCountryTypeAndDate(countryId : String, vatType : String, vatDate : Date) : VatValueDto

    /**
     * Gets VAT Values with internal overlapping against indicated VAT value.
     * @param vatValueDto VAT value interval whose overlapping ins considered
     * @return A <code>List</code> of <code>VatValueDto</code> registered with overlapping.
     *
     */
    fun getVatValueOverlapping(vatValueDto : VatValueDto) : List<VatValueDto>

    fun getVatValue(countryId: String, vatType: String, validFrom : Date) : VatValueDto?

    fun createVatValue(vatValueDto : VatValueDto)

    fun updateVatValue(vatValueDto: VatValueDto)

    fun deleteVatValue(vatValueDto: VatValueDto)

}