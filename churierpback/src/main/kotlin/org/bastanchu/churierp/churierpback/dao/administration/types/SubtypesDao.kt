package org.bastanchu.churierp.churierpback.dao.administration.types

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.types.SubtypeDto
import org.bastanchu.churierp.churierpback.entity.administration.types.Subtype
import org.bastanchu.churierp.churierpback.entity.administration.types.SubtypePK

interface SubtypesDao : BaseDtoDao<SubtypePK, Subtype, SubtypeDto> {
}