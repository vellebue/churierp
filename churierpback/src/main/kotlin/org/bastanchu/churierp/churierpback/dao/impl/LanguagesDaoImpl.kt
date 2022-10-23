package org.bastanchu.churierp.churierpback.dao.impl

import org.bastanchu.churierp.churierpback.dao.LanguagesDao
import org.bastanchu.churierp.churierpback.dto.LanguageDto
import org.bastanchu.churierp.churierpback.entity.Language
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class LanguagesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
    : BaseDtoDaoImpl<Integer, Language, LanguageDto>(entityManager) , LanguagesDao {
}