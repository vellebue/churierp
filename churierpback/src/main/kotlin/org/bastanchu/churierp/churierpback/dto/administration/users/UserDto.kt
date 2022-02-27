package org.bastanchu.churierp.churierpback.dto.administration.users

import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import javax.persistence.Column
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

class UserDto {

    @Field(key = "churierpweb.administration.users.dto.field.userId")
    @FormField(groupId = 0, indexInGroup = 0, readOnly = true)
    var userId: Integer? = null;
    @NotEmpty
    @Size(max = 100)
    @Field(key = "churierpweb.administration.users.dto.field.login")
    @FormField(groupId = 1, indexInGroup = 0)
    var login: String? = null;
    @NotEmpty
    @Field(key = "churierpweb.administration.users.dto.field.password")
    @FormField(groupId = 1, indexInGroup = 1, password = true)
    var password: String? = null;
    @Size(max = 100)
    @Field(key = "churierpweb.administration.users.dto.field.name")
    @FormField(groupId = 2, indexInGroup = 0)
    var name: String? = null;
    @Size(max = 255)
    @Field(key = "churierpweb.administration.users.dto.field.surname")
    @FormField(groupId = 2, indexInGroup = 1)
    var surname: String? = null;
    @NotNull
    @Field(key = "churierpweb.administration.users.dto.field.creationDate")
    @FormField(groupId = 3, indexInGroup = 0)
    var creationDate: Date? = null;
    @Field(key = "churierpweb.administration.users.dto.field.endDate", visible = false)
    @FormField(groupId = 3, indexInGroup = 1)
    var endDate: Date? = null;

}