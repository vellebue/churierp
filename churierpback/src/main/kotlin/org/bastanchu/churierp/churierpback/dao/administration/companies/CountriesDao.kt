package org.bastanchu.churierp.churierpback.dao.administration.companies

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.dto.administration.companies.CountryDto
import org.bastanchu.churierp.churierpback.entity.companies.Country

interface CountriesDao : BaseDtoDao<String, Country, CountryDto> {

    public fun initializeCountriesRegions();
}