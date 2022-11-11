package org.bastanchu.churierp.churierpback.entity.administration.companies

import org.bastanchu.churierp.churierpback.entity.Address
import org.bastanchu.churierp.churierpback.entity.TraceableEntity
import javax.persistence.*;

@Entity
@Table(name = "COMPANIES")
class Company : TraceableEntity() {

    @Id
    @Column(name = "COMPANY_ID")
    @GeneratedValue(generator = "SEQ_COMPANIES")
    @SequenceGenerator(name = "SEQ_COMPANIES", sequenceName = "SEQ_COMPANIES", allocationSize = 1)
    var companyId: Integer? = null

    @Column(name = "NAME")
    var name: String? = null

    @Column(name = "SOCIAL_NAME")
    var socialName: String? = null

    @Column(name = "VAT_NUMBER")
    var vatNumber: String? = null

    @OneToOne(cascade = arrayOf(CascadeType.ALL) )
    @JoinColumn(name = "address_id")
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.JOIN)
    var address : Address? = null
}