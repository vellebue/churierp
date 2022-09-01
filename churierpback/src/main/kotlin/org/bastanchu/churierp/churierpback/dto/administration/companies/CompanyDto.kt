package org.bastanchu.churierp.churierpback.dto.administration.companies

import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.bastanchu.churierp.churierpback.util.annotation.ListField
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.constraints.Size

class CompanyDto {

    //Company fields

    @Field(key = "churierpweb.administration.companies.dto.field.companyId")
    @FormField(groupId = 0, indexInGroup = 0, readOnly = true)
    @ListField
    var companyId: Integer? = null

    @NotEmpty
    @Size(max = 100)
    @Field(key = "churierpweb.administration.companies.dto.field.name")
    @FormField(groupId = 1, indexInGroup = 0)
    @ListField
    var name: String? = null

    @NotEmpty
    @Size(max = 100)
    @Field(key = "churierpweb.administration.companies.dto.field.socialName")
    @FormField(groupId = 1, indexInGroup = 1)
    @ListField
    var socialName: String? = null

    @NotEmpty
    @Size(max = 15)
    @Field(key = "churierpweb.administration.companies.dto.field.vatNumber")
    @FormField(groupId = 2, indexInGroup = 0)
    @ListField
    var vatNumber: String? = null

    // Address fields

    var addressId : Integer? = null

    var type: String? = null

    var address: String? = null

    var postalCode : String? = null

    var city : String? = null

    var countryId : String? = null

    var regionId : String? = null
}