package org.bastanchu.churierp.churierpback.dao.administration.types

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.types.TypedEntityDto
import org.bastanchu.churierp.churierpback.entity.administration.types.TypedEntity
import org.bastanchu.churierp.churierpback.entity.administration.types.TypedEntityPK

interface TypedEntityDao : BaseDtoDao<TypedEntityPK, TypedEntity, TypedEntityDto> {
}