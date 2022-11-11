package org.bastanchu.churierp.churierpback.entity.accounting.taxes

import javax.persistence.*

import org.bastanchu.churierp.churierpback.entity.TraceableEntity
import java.math.BigDecimal
import java.util.Date

@Entity
@Table(name = "VAT_VALUES")
@IdClass(VatValuePk::class)
class VatValue : TraceableEntity() {

    @Id
    @Column(name = "COUNTRY_ID")
    var countryId : String? = null

    @Id
    @Column(name = "VAT_ID")
    var vatId : String? = null

    @Id
    @Column(name = "VALID_FROM")
    var validFrom : Date? = null

    @Column(name = "VALID_TO")
    var validTo : Date? = null

    @Column(name = "PERCENTAGE")
    var percentage : BigDecimal? = null
}