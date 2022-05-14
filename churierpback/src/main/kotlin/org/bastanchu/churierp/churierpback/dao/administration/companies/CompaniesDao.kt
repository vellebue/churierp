package org.bastanchu.churierp.churierpback.dao.administration.companies

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.companies.CompanyDto
import org.bastanchu.churierp.churierpback.entity.companies.Company

interface CompaniesDao :BaseDtoDao <Integer, Company, CompanyDto> {
}