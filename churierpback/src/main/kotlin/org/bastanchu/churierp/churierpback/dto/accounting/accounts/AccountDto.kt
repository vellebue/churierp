package org.bastanchu.churierp.churierpback.dto.accounting.accounts

import org.bastanchu.churierp.churierpback.util.annotation.ComboBoxConfiguration
import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.bastanchu.churierp.churierpback.util.annotation.ListField
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.constraints.Size

class AccountDto {

    @Field(key = "churierpweb.administration.accounts.dto.field.accountId")
    @FormField(groupId = 0, indexInGroup = 0, readOnly = true,
               maxWidthInPixels = 120.0)
    @ListField(selected = false)
    var accountId : Integer? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.accounts.dto.field.planId")
    @FormField(groupId = 0, indexInGroup = 1, maxWidthInPixels = 450.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "planIdMap"))
    @ListField(selected = false)
    var planId : String? = null

    var planIdMap : Map<String, String>? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.accounts.dto.field.companyId")
    @FormField(groupId = 0, indexInGroup = 2, maxWidthInPixels = 300.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "companyIdMap"))
    @ListField(selected = false)
    var companyIdStringField : String? = null

    var companyIdMap : Map<String, String>? = null

    var companyId : Integer? = null

    @NotEmpty
    @Size(max = 2)
    @Field(key = "churierpweb.administration.accounts.dto.field.kindId")
    @FormField(groupId = 1, indexInGroup = 0, maxWidthInPixels = 350.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "kindIdMap"))
    @ListField
    var kindId : String? = null

    var kindIdMap : Map<String, String>? = null

    @NotEmpty
    @Size(max = 1)
    @Field(key = "churierpweb.administration.accounts.dto.field.debHab")
    @FormField(groupId = 1, indexInGroup = 1, maxWidthInPixels = 150.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "debHabMap"))
    @ListField(selected = false)
    var debHab : String? = null

    var debHabMap : Map<String, String>? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.accounts.dto.field.account")
    @FormField(groupId = 2, indexInGroup = 0, maxWidthInPixels = 150.0)
    @ListField
    var account : String? = null

    @NotEmpty
    @Size(max = 512)
    @Field(key = "churierpweb.administration.accounts.dto.field.description")
    @FormField(groupId = 2, indexInGroup = 1, maxWidthInPixels = 680.0)
    @ListField
    var description : String? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.accounts.dto.field.typeId")
    @FormField(groupId = 3, indexInGroup = 0 , maxWidthInPixels = 350.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "typeIdMap"))
    @ListField
    var typeId : String? = null

    var typeIdMap : Map<String, String>? = null

    @NotEmpty
    @Size(max = 10)
    @Field(key = "churierpweb.administration.accounts.dto.field.subtypeId")
    @FormField(groupId = 3, indexInGroup = 1, maxWidthInPixels = 350.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "subtypeIdMap",
                                                             conditionFieldName = "typeId"))
    @ListField
    var subtypeId : String? = null

    var subtypeIdMap : Map<String, Map<String, String>>? = null

    @Field(key = "churierpweb.administration.accounts.dto.field.qualified")
    @FormField(groupId = 3, indexInGroup = 2, maxWidthInPixels = 100.0)
    @ListField
    var qualified : Boolean? = null
}