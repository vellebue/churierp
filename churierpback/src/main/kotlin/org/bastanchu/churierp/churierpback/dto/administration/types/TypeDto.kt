package org.bastanchu.churierp.churierpback.dto.administration.types

import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.bastanchu.churierp.churierpback.util.annotation.HiddenFormField
import org.bastanchu.churierp.churierpback.util.annotation.ListField
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.constraints.Size

class TypeDto {

    @HiddenFormField
    var areaId : Integer? = null

    @HiddenFormField
    var entityId : Integer? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.types.typedto.typeId")
    @FormField(groupId = 0, indexInGroup = 0, widthPercentage = 10.0)
    @ListField(keyField = true)
    var typeId : String? = null

    @HiddenFormField
    var key : String? = null

    @NotEmpty
    @Size(max = 150)
    @Field(key = "churierpweb.administration.types.typedto.description")
    @FormField(groupId = 0, indexInGroup = 1, widthPercentage = 50.0)
    var description : String? = null

    @HiddenFormField
    var manageable : Boolean? = null

    var subtypes : List<SubtypeDto>? = null

    fun getSubtype(subtypeId : String) : SubtypeDto? {
        val subtypesList = subtypes?.filter { it.subtypeId == subtypeId }
        if ((subtypesList != null) && (subtypesList.size > 0)) {
            return subtypesList.first()
        } else {
            return null
        }
    }

}