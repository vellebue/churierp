package org.bastanchu.churierp.churierpback.service

import org.bastanchu.churierp.churierpback.dto.LanguageDto
import java.util.*

interface LanguageService {

    fun getAllLanguages(locale : Locale) : List<LanguageDto>

    fun getAllLanguagesMap(locale : Locale) : Map<String, String>

    fun getLanguageById(id : Integer) : LanguageDto

}