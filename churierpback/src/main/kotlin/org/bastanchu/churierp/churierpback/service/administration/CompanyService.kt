package org.bastanchu.churierp.churierpback.service.administration

import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyFilterDto

interface CompanyService {

    fun filterCompanies(filter: CompanyFilterDto):List<CompanyDto>

    fun getCompanyById(companyId : Integer) : CompanyDto?

    fun createCompany(companyDto: CompanyDto) : CompanyDto

    fun updateCompany(companyDto: CompanyDto)

    fun deleteCompany(companyDto : CompanyDto)

}