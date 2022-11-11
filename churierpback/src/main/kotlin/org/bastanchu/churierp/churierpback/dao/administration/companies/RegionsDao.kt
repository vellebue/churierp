package org.bastanchu.churierp.churierpback.dao.administration.companies

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.companies.RegionDto
import org.bastanchu.churierp.churierpback.entity.administration.companies.Region
import org.bastanchu.churierp.churierpback.entity.administration.companies.RegionPk

interface RegionsDao  : BaseDtoDao<RegionPk, Region, RegionDto> {
}