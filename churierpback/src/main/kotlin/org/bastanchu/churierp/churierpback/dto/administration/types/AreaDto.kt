package org.bastanchu.churierp.churierpback.dto.administration.types

class AreaDto {

    var id : Integer? = null

    var key : String? = null

    var description: String? = null

    var typedEntities : List<TypedEntityDto>? = null

    fun findTypeDto(entityId : Integer, typeId : String) :TypeDto? {
        val type = typedEntities?.filter { it.id == entityId }?.first()?.getTypeDto(typeId)
        return type
    }

    fun findSubtypeDto(entityId : Integer, typeId : String, subtypeId : String) : SubtypeDto? {
        val subtype = typedEntities?.filter { it.id == entityId }?.
                         first()?.getTypeDto(typeId)?.getSubtype(subtypeId)
        return subtype
    }
}