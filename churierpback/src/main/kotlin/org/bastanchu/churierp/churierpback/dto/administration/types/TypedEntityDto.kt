package org.bastanchu.churierp.churierpback.dto.administration.types

class TypedEntityDto {

    var areaId : Integer? = null

    var id : Integer? = null

    var key : String? = null

    var javaClass : String? = null

    var description: String? = null

    var allowSubtypes : Boolean? = null

    var types : List<TypeDto>? = null

    fun getTypeDto(typeId : String) : TypeDto? {
        val filteredList = types?.filter { it.typeId == typeId }
        if ((filteredList != null) && (filteredList.size > 0)) {
            return filteredList.first()
        } else {
            return null
        }
    }

    fun getSubtypeDto(typeId : String, subtypeId: String) : SubtypeDto? {
        val subtype = getTypeDto(typeId)?.getSubtype(subtypeId)
        return subtype
    }
}