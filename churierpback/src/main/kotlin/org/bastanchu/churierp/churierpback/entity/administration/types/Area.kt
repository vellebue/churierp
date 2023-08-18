package org.bastanchu.churierp.churierpback.entity.administration.types

import javax.persistence.*

@Entity
@Table(name = "C_TYPED_AREAS")
class Area {

    @Id
    @Column(name = "ID")
    var id : Integer? = null

    @Column(name = "KEY")
    var key : String? = null

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumns(
        JoinColumn(name = "AREA_ID", referencedColumnName = "ID")
    )
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.JOIN)
    var typedEntities = HashSet<TypedEntity>() as Set<TypedEntity>
}