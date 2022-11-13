package org.bastanchu.churierp.churierpback.dto.accounting.taxes

import org.bastanchu.churierp.churierpback.dto.Reseteable
import org.bastanchu.churierp.churierpback.util.annotation.ComboBoxConfiguration
import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.bastanchu.churierp.churierpback.util.annotation.ListField
import org.hibernate.validator.constraints.NotEmpty
import javax.validation.constraints.Size
import org.bastanchu.churierp.churierpback.dto.Copiable
import org.springframework.context.ApplicationContext

class VatTypeDto : Reseteable, Copiable<VatTypeDto> {

    var appContext : ApplicationContext? = null

    @NotEmpty
    @Size(max = 2)
    @Field(key = "churierpweb.accounting.taxes.vattype.dto.field.countryId")
    @FormField(groupId = 0, indexInGroup = 0, widthPercentage = 30.3,
               comboBoxConfiguration = ComboBoxConfiguration(mapFieldName = "countriesMap")
    )
    @ListField(keyField = true)
    var countryId : String? = null

    var countriesMap : Map<String,String>? = null

    @NotEmpty
    @Size(max = 2)
    @Field(key = "churierpweb.accounting.taxes.vattype.dto.field.vatId")
    @FormField(groupId = 0, indexInGroup = 1, widthPercentage = 30.3)
    @ListField(keyField = true)
    var vatId : String? = null;

    @NotEmpty
    @Size(max = 256)
    @Field(key = "churierpweb.accounting.taxes.vattype.dto.field.description")
    @FormField(groupId = 0, indexInGroup = 2, widthPercentage = 30.3)
    @ListField
    var description : String? = null;

    override fun reset() {
        countryId = null
        vatId = null
        description = null
    }

    override fun copy(): VatTypeDto {
        val copy = VatTypeDto()
        copy.countryId = countryId
        copy.countriesMap = countriesMap
        copy.vatId = vatId
        copy.description = description
        return copy
    }
}