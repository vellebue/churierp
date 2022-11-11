package org.bastanchu.churierp.churierpback.entity.accounting.taxes

import java.util.Date

import java.io.Serializable

class VatValuePk(var countryId : String? = "", var vatId : String? = "", var validFrom : Date = Date())
    : Serializable {
}