package org.bastanchu.churierp.churierpback.dao.accounting.taxes

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.accounting.taxes.VatValueDto
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValue
import org.bastanchu.churierp.churierpback.entity.accounting.taxes.VatValuePk
import java.util.*

interface VatValueDao : BaseDtoDao<VatValuePk, VatValue, VatValueDto> {

    fun getVatValueByCountryTypeAndDate(countryId : String, vatType : String, vatDate : Date) :VatValue

    fun getVatValuesInOverlapping(valtValue : VatValue) : List<VatValue>

}