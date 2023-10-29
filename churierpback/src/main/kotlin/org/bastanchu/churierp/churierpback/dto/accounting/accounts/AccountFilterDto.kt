package org.bastanchu.churierp.churierpback.dto.accounting.accounts

import org.bastanchu.churierp.churierpback.util.annotation.ComboBoxConfiguration
import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField

class AccountFilterDto {

    @Field("churierpweb.accounting.accounts.dto.filterField.planId")
    @FormField(groupId = 0, indexInGroup = 0, maxWidthInPixels = 350.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "planIdMap"))
    var planId : String? = null

    var planIdMap : Map<String, String>? = null

    @Field("churierpweb.accounting.accounts.dto.filterField.companyId")
    @FormField(groupId = 0, indexInGroup = 1, maxWidthInPixels = 300.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "companyIdMap"))
    var companyId : String? = null

    var companyIdMap : Map<String, String>? = null

    @Field("churierpweb.accounting.accounts.dto.filterField.kindId")
    @FormField(groupId = 0, indexInGroup = 2, maxWidthInPixels = 350.0,
                comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "kindIdMap"))
    var kindId : String? = null

    var kindIdMap : Map<String, String>? = null

    @Field("churierpweb.accounting.accounts.dto.filterField.account")
    @FormField(groupId = 1, indexInGroup = 0, maxWidthInPixels = 180.0)
    var account : String? = null

    @Field("churierpweb.accounting.accounts.dto.filterField.description")
    @FormField(groupId = 1, indexInGroup = 1, maxWidthInPixels = 820.0)
    var description : String? = null

    @Field("churierpweb.accounting.accounts.dto.filterField.debHab")
    @FormField(groupId = 1, indexInGroup = 2, maxWidthInPixels = 150.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "debHabMap"))
    var debHab : String? = null

    var debHabMap : Map<String, String>? = null

    @Field("churierpweb.accounting.accounts.dto.filterField.typeId")
    @FormField(groupId = 2, indexInGroup = 0, maxWidthInPixels = 400.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "typeIdMap"))
    var typeId : String? = null

    var typeIdMap : Map<String, String>? = null

    @Field("churierpweb.accounting.accounts.dto.filterField.subtypeId")
    @FormField(groupId = 2, indexInGroup = 1, maxWidthInPixels = 400.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "subtypeIdMap", conditionFieldName = "typeId"))
    var subtypeId : String? = null

    var subtypeIdMap : Map<String, Map<String, String>>? = null

}