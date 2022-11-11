package org.bastanchu.churierp.churierpback.dao.impl.administration.companies

import org.bastanchu.churierp.churierpback.dao.administration.companies.CompaniesDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto
import org.bastanchu.churierp.churierpback.entity.Address
import org.bastanchu.churierp.churierpback.entity.administration.companies.Company
import org.bastanchu.churierp.churierpback.util.CopyUtil
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CompaniesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager) : BaseDtoDaoImpl<Integer, Company, CompanyDto>(entityManager),
      CompaniesDao {

      val copyUtils = CopyUtil.Instance

      override fun toDataTransferObject(entity: Company, dto: CompanyDto) {
            super.toDataTransferObject(entity, dto)
            copyUtils.copyValues(entity.address as Any, dto as Any);
      }

      override fun fromDtoToEntity(dto: CompanyDto, entity: Company) {
            super.fromDtoToEntity(dto, entity)
            if (entity.address == null) {
                  var address = Address()
                  copyUtils.copyValues(dto, address)
                  address.type = Address.AddressType.ADDRESS_TYPE_COMPANY.toString()
                  entity.address = address
            } else {
                  copyUtils.copyValues(dto, entity.address as Any)
                  entity.address!!.type = Address.AddressType.ADDRESS_TYPE_COMPANY.toString()
            }
      }

}