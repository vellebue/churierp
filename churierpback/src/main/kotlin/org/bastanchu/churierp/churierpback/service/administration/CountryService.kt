package org.bastanchu.churierp.churierpback.service.administration

import java.util.Locale

interface CountryService {

    fun getCountriesMap(locale: Locale):Map<String,String>

}