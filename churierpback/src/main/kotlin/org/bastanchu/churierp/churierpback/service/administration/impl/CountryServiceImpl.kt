package org.bastanchu.churierp.churierpback.service.administration.impl

import org.bastanchu.churierp.churierpback.dao.administration.companies.CountriesDao
import org.bastanchu.churierp.churierpback.service.administration.CountryService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CountryServiceImpl(@Autowired val countriesDao: CountriesDao,
                         @Autowired val messageSource : MessageSource) : CountryService {

    private val logger: Logger = LoggerFactory.getLogger(CountryServiceImpl::class.java)

    override fun getCountriesMap(locale: Locale): Map<String, String> {
        val countriesList = countriesDao.listAll()
        val countriesMap = HashMap<String, String>()
        countriesList.forEach {
            val key = it.key
            var countryName:String? = null
            try {
                countryName = messageSource.getMessage(key, null, locale)
            } catch (e: NoSuchMessageException) {
                logger.warn("Not defined key for country key " + key + " in locale " + locale)
            }
            if (countryName == null) {
                countryName = it.name
            }
            val countryId = if  (it.countryId != null) it.countryId!! else ""
            countriesMap.put(countryId, countryName!!)
        }
        return countriesMap
    }

}