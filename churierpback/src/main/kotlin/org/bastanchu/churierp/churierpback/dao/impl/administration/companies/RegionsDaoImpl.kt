package org.bastanchu.churierp.churierpback.dao.impl.administration.companies

import org.bastanchu.churierp.churierpback.dao.administration.companies.RegionsDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.companies.RegionDto
import org.bastanchu.churierp.churierpback.entity.administration.companies.Region
import org.bastanchu.churierp.churierpback.entity.administration.companies.RegionPk
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class RegionsDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
    : BaseDtoDaoImpl<RegionPk, Region, RegionDto>(entityManager) ,
      RegionsDao {
}