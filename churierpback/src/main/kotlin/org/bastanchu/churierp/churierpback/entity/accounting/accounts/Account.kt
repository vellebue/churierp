package org.bastanchu.churierp.churierpback.entity.accounting.accounts

import javax.persistence.*

import org.bastanchu.churierp.churierpback.entity.TraceableEntity

@Entity()
@Table(name = "ACCOUNTING_ACCOUNTS")
class Account : TraceableEntity() {

    @Id
    @Column(name = "ACC_ID")
    @GeneratedValue(generator = "seq_ac_accounts")
    @SequenceGenerator(name = "seq_ac_accounts", sequenceName = "SEQ_AC_ACCOUNTS", allocationSize = 1)
    var accountId : Integer? = null

    @Column(name = "PLAN_ID")
    var planId : String? = null

    @Column(name = "COMPANY_ID")
    var companyId : Integer? = null

    @Column(name = "KIND_ID")
    var kindId : String? = null

    @Column(name = "DEB_HAB")
    var debHab : String? = null

    @Column(name = "ACCOUNT")
    var account : String? = null

    @Column(name = "DESCRIPTION")
    var description : String? = null

    @Column(name = "TYPE_ID")
    var typeId : String? = null

    @Column(name = "SUBTYPE_ID")
    var subtypeId : String? = null

    @Column(name = "QUALIFIED")
    var qualified : Boolean? = null
}