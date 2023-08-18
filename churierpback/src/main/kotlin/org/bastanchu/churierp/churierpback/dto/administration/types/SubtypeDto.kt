package org.bastanchu.churierp.churierpback.dto.administration.types

import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.bastanchu.churierp.churierpback.util.annotation.HiddenFormField
import org.bastanchu.churierp.churierpback.util.annotation.ListField
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.constraints.Size

class SubtypeDto {

    @HiddenFormField
    var areaId : Integer? = null

    @HiddenFormField
    var entityId : Integer? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.types.typedto.typeId")
    @FormField(groupId = 0, indexInGroup = 0, readOnly = true, widthPercentage = 10.0)
    @ListField(keyField = true)
    var typeId : String? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.types.subtypedto.subtypeId")
    @FormField(groupId = 0, indexInGroup = 1, widthPercentage = 10.0)
    @ListField(keyField = true)
    var subtypeId : String? = null

    @HiddenFormField
    var key : String? = null

    @NotEmpty
    @Size(max = 150)
    @Field(key = "churierpweb.administration.types.subtypedto.description")
    @FormField(groupId = 0, indexInGroup = 2, widthPercentage = 40.0)
    var description : String? = null

    @HiddenFormField
    var manageable : Boolean? = null

}