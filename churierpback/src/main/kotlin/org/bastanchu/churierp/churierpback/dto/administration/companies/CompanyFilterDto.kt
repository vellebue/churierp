package org.bastanchu.churierp.churierpback.dto.administration.companies

import org.bastanchu.churierp.churierpback.util.annotation.Field
import org.bastanchu.churierp.churierpback.util.annotation.FormField

class CompanyFilterDto {

    @Field("churierpweb.administration.companies.dto.filterField.name")
    @FormField(groupId = 0, indexInGroup = 0)
    public var name: String? = null

    @Field("churierpweb.administration.companies.dto.filterField.socialName")
    @FormField(groupId = 0, indexInGroup = 1)
    public var socialName: String? = null

}