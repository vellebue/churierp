package org.bastanchu.churierp.churierpback.dao

import org.bastanchu.churierp.churierpback.dto.TestEntityDto
import org.bastanchu.churierp.churierpback.entity.TestEntity

interface TestEntityDao : BaseDtoDao<Integer, TestEntity, TestEntityDto> {
}