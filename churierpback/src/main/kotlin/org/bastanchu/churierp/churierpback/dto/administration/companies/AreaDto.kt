package org.bastanchu.churierp.churierpback.dto.administration.companies

import com.fasterxml.jackson.annotation.JsonProperty

class AreaDto {

    @JsonProperty(value = "Country")
    var country:String? = null;
    @JsonProperty(value = "Code")
    var code:String? = null;
    @JsonProperty(value = "Name")
    var name:String? = null;

}