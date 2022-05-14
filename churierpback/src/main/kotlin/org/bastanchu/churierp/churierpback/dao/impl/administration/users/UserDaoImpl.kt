package org.bastanchu.churierp.churierpback.dao.impl.administration.users

import org.bastanchu.churierp.churierpback.dao.administration.users.UserDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.users.UserDto
import org.bastanchu.churierp.churierpback.entity.users.User
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext



@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class UserDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager) : BaseDtoDaoImpl<Int, User, UserDto>(entityManager),
    UserDao {
}