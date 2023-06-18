package org.bastanchu.churierp.churierpback.dto.accounting.taxes

import org.bastanchu.churierp.churierpback.dto.Copiable
import org.bastanchu.churierp.churierpback.dto.Reseteable
import org.bastanchu.churierp.churierpback.dto.Validator
import org.bastanchu.churierp.churierpback.util.annotation.ComboBoxConfiguration
import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.bastanchu.churierp.churierpback.util.annotation.ListField
import org.hibernate.validator.constraints.NotEmpty
import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.*
import kotlin.collections.ArrayList

class VatValueDto : Validator, Reseteable, Copiable<VatValueDto>{

    @NotEmpty
    @Size(max = 2)
    @Field(key = "churierpweb.accounting.taxes.vatvalue.dto.field.countryId")
    @FormField(groupId = 0, indexInGroup = 0, widthPercentage = 40.0,
        comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "countriesMap")
    )
    @ListField(keyField = true)
    var countryId : String? = null

    var countriesMap : Map<String, String>? = null

    @NotEmpty
    @Size(max = 2)
    @Field(key = "churierpweb.accounting.taxes.vatvalue.dto.field.vatId")
    @FormField(groupId = 0, indexInGroup = 1, widthPercentage = 40.0,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "vatTypesMap", conditionFieldName = "countryId"))
    @ListField(keyField = true)
    var vatId : String? = null

    var vatTypesMap : Map<String, Map<String, String>>? = null

    @NotNull
    @Field(key = "churierpweb.accounting.taxes.vatvalue.dto.field.validFrom")
    @FormField(groupId = 1, indexInGroup = 0, widthPercentage = 30.0)
    @ListField(keyField = true)
    var validFrom : Date? = null

    @Field(key = "churierpweb.accounting.taxes.vatvalue.dto.field.validTo")
    @FormField(groupId = 1, indexInGroup = 1, widthPercentage = 30.0)
    @ListField
    var validTo : Date? = null;

    @NotNull
    @DecimalMin("0.0", inclusive = true)
    @DecimalMax("100.0", inclusive = true)
    @Digits(integer=3, fraction=2)
    @Field(key = "churierpweb.accounting.taxes.vatvalue.dto.field.percentage")
    @FormField(groupId = 2, indexInGroup = 0, widthPercentage = 25.0)
    @ListField
    var percentage : BigDecimal? = null

    @NotNull
    @DecimalMin("0.0", inclusive = true)
    @DecimalMax("100.0", inclusive = true)
    @Digits(integer=3, fraction=2)
    @Field(key = "churierpweb.accounting.taxes.vatvalue.dto.field.upcharge")
    @FormField(groupId = 2, indexInGroup = 1, widthPercentage = 33.0)
    @ListField
    var upcharge : BigDecimal? = null

    override fun reset() {
        countryId = null
        vatId = null
        validFrom = null
        validTo = null
        percentage = null
    }

    override fun copy(): VatValueDto {
        val copy = VatValueDto()
        copy.countryId = countryId
        copy.countriesMap = countriesMap
        copy.vatTypesMap = vatTypesMap
        copy.vatId = vatId
        copy.validFrom = validFrom
        copy.validTo = validTo
        copy.percentage = percentage
        copy.upcharge = upcharge
        return copy
    }

    override fun validate(): List<String> {
        val errors = ArrayList<String>()
        if ((validFrom != null) && (validTo != null) && (validTo!! < validFrom)) {
            errors.add("churierpweb.accounting.taxes.vatvalue.dto.validation.invalidValidToDate")
        }
        return errors;
    }
}