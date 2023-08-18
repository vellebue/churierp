package org.bastanchu.churierp.churierpback.entity.administration.types

import java.io.Serializable

data class TypePK(var areaId : Integer = 0 as Integer,
                  var entityId : Integer = 0 as Integer,
                  var typeId : String = "") : Serializable {
}