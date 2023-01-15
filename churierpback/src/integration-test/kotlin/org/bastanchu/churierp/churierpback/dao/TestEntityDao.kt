package org.bastanchu.churierp.churierpback.dao

import org.bastanchu.churierp.churierpback.dto.TestEntityDto
import org.bastanchu.churierp.churierpback.entity.TestEntity

@Deprecated("New Test structure developed")
interface TestEntityDao : BaseDtoDao<Integer, TestEntity, TestEntityDto> {
}