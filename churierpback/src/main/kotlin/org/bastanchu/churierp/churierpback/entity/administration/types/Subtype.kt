package org.bastanchu.churierp.churierpback.entity.administration.types

import javax.persistence.*

@Entity
@Table(name = "TYPED_SUBTYPES")
@IdClass(SubtypePK::class)
class Subtype {

    @Id
    @Column(name = "AREA_ID")
    var areaId : Integer? = null

    @Id
    @Column(name = "ENTITY_ID")
    var entityId : Integer? = null

    @Id
    @Column(name = "TYPE_ID")
    var typeId : String? = null

    @Id
    @Column(name = "SUBTYPE_ID")
    var subtypeId : String? = null

    @Column(name = "KEY")
    var key : String? = null

    @Column(name = "DESCRIPTION")
    var description : String? = null

    @Column(name = "MANAGEABLE")
    var manageable : Boolean? = null

    @ManyToOne()
    @JoinColumns(
        JoinColumn(name = "AREA_ID", referencedColumnName = "AREA_ID", insertable = false, updatable = false),
        JoinColumn(name = "ENTITY_ID", referencedColumnName = "ENTITY_ID", insertable = false, updatable = false),
        JoinColumn(name = "TYPE_ID", referencedColumnName = "TYPE_ID", insertable = false, updatable = false)
    )
    var parentType : Type? = null
}