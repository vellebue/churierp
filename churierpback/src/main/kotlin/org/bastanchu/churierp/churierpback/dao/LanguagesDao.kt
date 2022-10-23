package org.bastanchu.churierp.churierpback.dao

import org.bastanchu.churierp.churierpback.dto.LanguageDto
import org.bastanchu.churierp.churierpback.entity.Language

interface LanguagesDao : BaseDtoDao<Integer, Language, LanguageDto> {
}