package org.bastanchu.churierp.churierpback.entity.administration.companies

import javax.persistence.*;

@Entity
@Table(name = "C_COUNTRIES")
class Country {

    @Id
    @Column(name = "COUNTRY_ID")
    var countryId:String? =null
    @Column(name = "NAME")
    var name:String? =null
    @Column(name = "KEY")
    var key:String? =null

}