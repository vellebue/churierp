package org.bastanchu.churierp.churierpback.dto.administration.companies

import org.bastanchu.churierp.churierpback.util.annotation.*
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

    @HiddenFormField
    var addressId : Integer? = null

    var type: String? = null

    @NotEmpty
    @Size(max = 512)
    @Field(key = "churierpweb.administration.companies.dto.field.address")
    @FormField(groupId = 3, indexInGroup = 0, colSpan = 2)
    @ListField(selected = false)
    var address: String? = null

    @NotEmpty
    @Size(max = 15)
    @Field(key = "churierpweb.administration.companies.dto.field.postalCode")
    @FormField(groupId = 4, indexInGroup = 0)
    @ListField(selected = false)
    var postalCode : String? = null

    @NotEmpty
    @Size(max = 100)
    @Field(key = "churierpweb.administration.companies.dto.field.city")
    @FormField(groupId = 4, indexInGroup = 1)
    @ListField(selected = false)
    var city : String? = null

    @NotEmpty
    @Size(max = 2)
    @Field(key = "churierpweb.administration.companies.dto.field.countryId")
    @FormField(groupId = 5, indexInGroup = 0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "countriesMap"))
    @ListField(selected = false)
    var countryId : String? = null

    var countriesMap : Map<String,String>? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.companies.dto.field.regionId")
    @FormField(groupId = 5, indexInGroup = 1,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "regionsMap", conditionFieldName = "countryId"))
    @ListField(selected = false)
    var regionId : String? = null

    var regionsMap : Map<String,Map<String,String>>? = null
}