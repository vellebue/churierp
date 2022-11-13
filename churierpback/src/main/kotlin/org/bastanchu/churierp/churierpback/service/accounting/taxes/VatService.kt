package org.bastanchu.churierp.churierpback.service.accounting.taxes

import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatTypeDto

interface VatService {

    fun getVatTypes() : List<VatTypeDto>

    fun getVatType(countryId : String, vatType : String) : VatTypeDto?

    fun createVatType(vatTypeDto : VatTypeDto)

    fun updateVatType(vatTypeDto :VatTypeDto)

    fun deleteVatType(countryId : String, vatType : String)

}