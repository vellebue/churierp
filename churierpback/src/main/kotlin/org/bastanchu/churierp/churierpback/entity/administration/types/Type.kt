package org.bastanchu.churierp.churierpback.entity.administration.types

import javax.persistence.*

@Entity
@Table(name = "TYPED_TYPES")
@IdClass(TypePK::class)
class Type {

    enum class Area(val id : Int) {
        ADMINISTRATION(0),
        ACCOUNTING(1),
        SALES(2)
    }

    enum class Entity(val id : Int) {
        USER(0),
        COMPANY(1)
    }

    @Id
    @Column(name = "AREA_ID")
    var areaId : Integer? = null

    @Id
    @Column(name = "ENTITY_ID")
    var entityId : Integer? = null

    @Id
    @Column(name = "TYPE_ID")
    var typeId : String? = null

    @Column(name = "KEY")
    var key : String? = null

    @Column(name = "DESCRIPTION")
    var description : String? = null

    @Column(name = "MANAGEABLE")
    var manageable : Boolean? = null

    @OneToMany(cascade = arrayOf(CascadeType.ALL),
               fetch = FetchType.EAGER,
               orphanRemoval = true,
               mappedBy = "parentType")
    @OrderBy("typeId ASC, subtypeId ASC")
    @org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.JOIN)
    var subtypes = HashSet<Subtype>() as Set<Subtype>
}