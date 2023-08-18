package org.bastanchu.churierp.churierpback.entity.administration.types

import javax.persistence.*

@Entity
@Table(name = "C_TYPED_ENTITIES")
@IdClass(TypedEntityPK::class)
class TypedEntity {

    @Id
    @Column(name = "AREA_ID")
    var areaId : Integer? = null

    @Id
    @Column(name = "ID")
    var id : Integer? = null

    @Column(name = "KEY")
    var key : String? = null

    @Column(name = "JAVA_CLASS")
    var javaClass : String? = null

    @Column(name = "DESCRIPTION")
    var description: String? = null

    @Column(name = "ALLOW_SUBTYPES")
    var allowSubtypes : Boolean? = null

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumns(
        JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID"),
        JoinColumn(name = "ENTITY_ID", referencedColumnName = "ID")
    )
    @OrderBy("typeId ASC")
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.JOIN)
    var types = HashSet<Type>() as Set<Type>

}