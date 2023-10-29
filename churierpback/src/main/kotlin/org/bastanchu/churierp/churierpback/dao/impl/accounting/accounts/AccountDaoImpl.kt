package org.bastanchu.churierp.churierpback.dao.impl.accounting.accounts

import org.bastanchu.churierp.churierpback.dao.accounting.accounts.AccountDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountDto
import org.bastanchu.churierp.churierpback.entity.accounting.accounts.Account
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class AccountDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager) :
                     BaseDtoDaoImpl<Integer, Account, AccountDto>(entityManager) ,
                     AccountDao {
}