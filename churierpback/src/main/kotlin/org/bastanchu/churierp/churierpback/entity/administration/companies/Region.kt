package org.bastanchu.churierp.churierpback.entity.administration.companies

import javax.persistence.*;

@Entity
@Table(name = "C_REGIONS")
@IdClass(RegionPk::class)
class Region {

    @Id
    @Column(name = "COUNTRY_ID")
    var countryId: String? = null

    @Id
    @Column(name = "REGION_ID")
    var regionId: String? = null

    @Column(name = "NAME")
    var name: String? = null

    @Column(name = "KEY")
    var key: String? = null
}