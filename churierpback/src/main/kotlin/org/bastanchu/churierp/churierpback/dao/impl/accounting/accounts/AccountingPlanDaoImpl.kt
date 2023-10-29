package org.bastanchu.churierp.churierpback.dao.impl.accounting.accounts

import org.bastanchu.churierp.churierpback.dao.accounting.accounts.AccountingPlanDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.accounting.accounts.AccountingPlanDto
import org.bastanchu.churierp.churierpback.entity.accounting.accounts.AccountingPlan
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class AccountingPlanDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager) :
                            BaseDtoDaoImpl<String, AccountingPlan, AccountingPlanDto>(entityManager),
                            AccountingPlanDao {

}