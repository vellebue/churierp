package org.bastanchu.churierp.churierpback.entity.companies

import javax.persistence.*;

@Entity
@Table(name = "COMPANIES")
class Company {

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
}