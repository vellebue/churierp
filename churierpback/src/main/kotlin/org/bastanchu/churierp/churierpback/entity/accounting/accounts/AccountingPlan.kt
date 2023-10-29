package org.bastanchu.churierp.churierpback.entity.accounting.accounts

import javax.persistence.*

@Entity
@Table(name = "C_ACCOUNTING_PLANS")
class AccountingPlan {

    @Id
    @Column(name = "PLAN_ID")
    var planId : String? = null

    @Column(name = "COUNTRY_ID")
    var countryId : String? = null

    @Column(name = "KEY")
    var key : String? = null

    @Column(name = "DESCRIPTION")
    var description : String? = null

}