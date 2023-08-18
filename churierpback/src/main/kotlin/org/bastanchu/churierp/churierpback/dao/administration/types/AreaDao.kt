package org.bastanchu.churierp.churierpback.dao.administration.types

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.types.AreaDto
import org.bastanchu.churierp.churierpback.entity.administration.types.Area

interface AreaDao : BaseDtoDao<Integer, Area, AreaDto> {
}