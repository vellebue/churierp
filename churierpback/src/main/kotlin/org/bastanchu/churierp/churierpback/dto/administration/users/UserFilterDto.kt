package org.bastanchu.churierp.churierpback.dto.administration.users

import org.bastanchu.churierp.churierpback.dto.Validator
import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotEmpty
import java.util.*
import javax.validation.Valid
import kotlin.collections.ArrayList

class UserFilterDto : Validator {

    @Field(key = "churierpweb.administration.users.dto.filterField.login")
    @FormField(groupId = 0, indexInGroup = 0)
    private var login:String? = null;
    @Field(key = "churierpweb.administration.users.dto.filterField.name")
    @FormField(groupId = 1, indexInGroup = 0)
    private var name:String? = null;
    @Field(key = "churierpweb.administration.users.dto.filterField.surname")
    @FormField(groupId = 1, indexInGroup = 1)
    private var surname:String? = null;
    @Field(key = "churierpweb.administration.users.dto.filterField.creationDateFrom")
    @FormField(groupId = 2, indexInGroup = 0, field = "creationDate", from = true)
    private var creationDateFrom: Date? = null;
    @Field(key = "churierpweb.administration.users.dto.filterField.creationDateTo")
    @FormField(groupId = 2, indexInGroup = 1, field = "creationDate", to = true)
    private var creationDateTo: Date? = null;
    @Field(key = "churierpweb.administration.users.dto.filterField.endDateFrom")
    @FormField(groupId = 3, indexInGroup = 0, field = "endDate", from = true)
    private var endDateFrom: Date? = null;
    @Field(key = "churierpweb.administration.users.dto.filterField.endDateTo")
    @FormField(groupId = 3, indexInGroup = 1, field = "endDate", to = true)
    private var endDateTo: Date? = null;

    override fun validate(): List<String> {
        var errors = ArrayList<String>();
        if ((creationDateFrom != null) && (creationDateTo != null) &&
            (creationDateFrom!!.time > creationDateTo!!.time)) {
            errors.add("churierpweb.administration.users.dto.validation.creationDateFromTo");
        }
        if ((endDateFrom != null) && (endDateTo != null) &&
            (endDateFrom!!.time > endDateTo!!.time)) {
            errors.add("churierpweb.administration.users.dto.validation.endDateFromTo");
        }
        return errors;
    }

}