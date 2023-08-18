package org.bastanchu.churierp.churierpback.dto.administration.users

import org.bastanchu.churierp.churierpback.util.annotation.ComboBoxConfiguration
import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.bastanchu.churierp.churierpback.util.annotation.ListField
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class UserDto {

    @Field(key = "churierpweb.administration.users.dto.field.userId")
    @FormField(groupId = 0, indexInGroup = 0, readOnly = true)
    @ListField
    var userId: Integer? = null;

    @NotEmpty
    @Size(max = 100)
    @Field(key = "churierpweb.administration.users.dto.field.login")
    @FormField(groupId = 1, indexInGroup = 0)
    @ListField
    var login: String? = null;

    @NotEmpty
    @Field(key = "churierpweb.administration.users.dto.field.password")
    @FormField(groupId = 1, indexInGroup = 1, password = true)
    var password: String? = null;

    @Size(max = 100)
    @Field(key = "churierpweb.administration.users.dto.field.name")
    @FormField(groupId = 2, indexInGroup = 0)
    @ListField
    var name: String? = null;

    @Size(max = 255)
    @Field(key = "churierpweb.administration.users.dto.field.surname")
    @FormField(groupId = 2, indexInGroup = 1)
    @ListField
    var surname: String? = null;

    @Size(max = 100)
    @Email
    @Field(key = "churierpweb.administration.users.dto.field.email")
    @FormField(groupId = 3, indexInGroup = 0)
    @ListField
    var email:String? = null;

    @NotNull
    @Field(key = "churierpweb.administration.users.dto.field.creationDate")
    @FormField(groupId = 4, indexInGroup = 0)
    @ListField
    var creationDate: Date? = null;

    @Field(key = "churierpweb.administration.users.dto.field.endDate")
    @FormField(groupId = 4, indexInGroup = 1)
    @ListField(selected = false)
    var endDate: Date? = null;

    @Field(key = "churierpweb.administration.users.dto.field.language")
    @FormField(groupId = 5, indexInGroup = 0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "languagesMap"))
    @ListField(selected = false)
    var languageId : String? = null

    var languagesMap : Map<String, String>? = null;

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.users.dto.field.userType")
    @FormField(groupId = 6, indexInGroup = 0,
        comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "userTypesMap"))
    @ListField(selected = false)
    var userType : String? = null

    var userTypesMap : Map<String, String>? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.users.dto.field.userSubtype")
    @FormField(groupId = 6, indexInGroup = 1,
        comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "userSubtypesMap", conditionFieldName = "userType"))
    @ListField(selected = false)
    var userSubtype : String? = null

    var userSubtypesMap : Map<String, Map<String, String>>? = null
}