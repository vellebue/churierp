package org.bastanchu.churierp.churierpback.dao.impl.administration.users

import org.bastanchu.churierp.churierpback.dao.LanguagesDao
import org.bastanchu.churierp.churierpback.dao.administration.users.UserDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto
import org.bastanchu.churierp.churierpback.entity.administration.users.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext



@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class UserDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager,
                  @Autowired val languageDao : LanguagesDao) : BaseDtoDaoImpl<Int, User, UserDto>(entityManager),
    UserDao {



    override fun toDataTransferObject(entity: User, dto: UserDto) {
        super.toDataTransferObject(entity, dto)
        val language = entity.language
        dto.languageId = if (language != null) "" + language.id else null
    }

    override fun fromDtoToEntity(dto: UserDto, entity: User) {
        super.fromDtoToEntity(dto, entity)
        if (dto.languageId != null) {
            val languageId = Integer.parseInt(dto.languageId) as Integer
            val language = languageDao.getById(languageId);
            entity.language = language
        }
    }
}