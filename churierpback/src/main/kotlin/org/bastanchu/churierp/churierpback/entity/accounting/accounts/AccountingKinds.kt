package org.bastanchu.churierp.churierpback.entity.accounting.accounts

import javax.persistence.*

@Entity
@Table(name = "C_ACCOUNTING_ACCOUNT_KINDS")
class AccountingKinds {

    @Id
    @Column(name ="ID")
    var id : String? = null

    @Column(name = "KEY")
    var key : String? = null

    @Column(name = "DESCRIPTION")
    var description : String? = null

}