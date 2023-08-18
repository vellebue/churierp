package org.bastanchu.churierp.churierpback.service.administration

import org.bastanchu.churierp.churierpback.dto.administration.types.AreaDto
import org.bastanchu.churierp.churierpback.dto.administration.types.TypeDto

interface TypesSubtypesService {

    fun getFullTypesData() : List<AreaDto>

    fun getTypesMap(areaId : Int, typedEntityId : Int) : Map<String, String>

    fun getSubtypesMap(areaId : Int, typedEntityId : Int) : Map<String, Map<String, String>>

    fun getType(areaId: Int, typedEntityId: Int, typeId : String) :TypeDto?

    fun createType(typeDto : TypeDto)

    fun updateType(typeDto : TypeDto)

    fun deleteType(typeDto : TypeDto)
}