package org.bastanchu.churierp.churierpback.dao.impl.administration.companies

import org.bastanchu.churierp.churierpback.dao.administration.companies.CompaniesDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto
import org.bastanchu.churierp.churierpback.entity.companies.Company
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CompaniesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager) : BaseDtoDaoImpl<Integer, Company, CompanyDto>(entityManager),
      CompaniesDao {
}