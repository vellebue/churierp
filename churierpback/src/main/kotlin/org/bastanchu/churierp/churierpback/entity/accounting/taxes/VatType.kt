package org.bastanchu.churierp.churierpback.entity.accounting.taxes

import javax.persistence.*

import org.bastanchu.churierp.churierpback.entity.TraceableEntity

@Entity
@Table(name = "VAT_TYPES")
@IdClass(VatTypePk::class)
class VatType : TraceableEntity() {

    @Id
    @Column(name = "COUNTRY_ID")
    var countryId : String? = null

    @Id
    @Column(name = "VAT_ID")
    var vatId : String? = null;

    @Column(name = "DESCRIPTION")
    var description : String? = null;

}