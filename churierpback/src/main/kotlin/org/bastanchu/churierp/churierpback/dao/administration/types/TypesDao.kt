package org.bastanchu.churierp.churierpback.dao.administration.types

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.types.TypeDto
import org.bastanchu.churierp.churierpback.entity.administration.types.Type
import org.bastanchu.churierp.churierpback.entity.administration.types.TypePK

interface TypesDao : BaseDtoDao<TypePK, Type, TypeDto> {
}