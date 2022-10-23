package org.bastanchu.churierp.churierpback.service.impl

import org.bastanchu.churierp.churierpback.dao.LanguagesDao
import org.bastanchu.churierp.churierpback.dto.LanguageDto
import org.bastanchu.churierp.churierpback.service.LanguageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.MessageSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service("languageService")
@Qualifier("languageService")
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class LanguageServiceImpl(@Autowired val languagesDao : LanguagesDao,
                          @Autowired val messageSource: MessageSource)  : LanguageService{

    override fun getAllLanguages(locale : Locale): List<LanguageDto> {
        val languageEntities = languagesDao.listAll();
        val languagesList = languagesDao.toDataTransferObjectList(languageEntities)
        languagesList.map {
            it.language = messageSource.getMessage(it.languageKey, null, locale)
            it
        }.sortedBy { it.language }
        return languagesList
    }

    override fun getAllLanguagesMap(locale: Locale): Map<String, String> {
        val map = LinkedHashMap<String, String>()
        val languages = getAllLanguages(locale)
        languages.forEach {
            map.put("" + it.id, it.language!!)
        }
        return map
    }

    override fun getLanguageById(id: Integer) : LanguageDto {
        val entity = languagesDao.getById(id);
        return languagesDao.toDataTransferObject(entity);
    }

}