package org.bastanchu.churierp.churierpback.service.administration.impl

import org.bastanchu.churierp.churierpback.dao.administration.companies.CompaniesDao
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyFilterDto
import org.bastanchu.churierp.churierpback.service.administration.CompanyService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CompanyServiceImpl(@Autowired val companyDao: CompaniesDao) : CompanyService {

    override fun filterCompanies(filter: CompanyFilterDto): List<CompanyDto> {
        val companies = companyDao.genericFilter(filter);
        return companyDao.toDataTransferObjectList(companies);
    }

    override fun getCompanyById(companyId: Integer): CompanyDto? {
        val company =  companyDao.getById(companyId)
        return companyDao.toDataTransferObject(company)
    }

    override fun createCompany(companyDto: CompanyDto):CompanyDto {
        val company = companyDao.fromDtoToEntity(companyDto)
        companyDao.create(company)
        return companyDao.toDataTransferObject(company)
    }

    override fun updateCompany(companyDto: CompanyDto) {
        companyDao.fromDtoToEntity(companyDto)
    }

    override fun deleteCompany(companyDto: CompanyDto) {
        if (companyDto != null) {
            if (companyDto.companyId == null) {
                throw RuntimeException("While deleting company, companyId is null")
            } else {
                companyDao.deleteById(companyDto.companyId as Integer)
            }
        } else {
            throw RuntimeException("While deleting company, companyDto is null")
        }
    }

}