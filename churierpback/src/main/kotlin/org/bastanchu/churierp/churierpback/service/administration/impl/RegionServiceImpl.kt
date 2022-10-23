package org.bastanchu.churierp.churierpback.service.administration.impl

import org.bastanchu.churierp.churierpback.dao.administration.companies.RegionsDao
import org.bastanchu.churierp.churierpback.service.administration.RegionService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.collections.HashMap

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class RegionServiceImpl(@Autowired val regionsDao:RegionsDao,
                        @Autowired val messageSource : MessageSource) : RegionService {

    val logger = LoggerFactory.getLogger(RegionServiceImpl::class.java)

    override fun getRegionsMap(locale: Locale): Map<String, Map<String, String>> {
        val regionsList = regionsDao.listAll();
        var regionsMap = HashMap<String, MutableMap<String,String>>()
        regionsList.forEach {
            val countryId = it.countryId!!
            var countryMap = regionsMap.get(countryId)
            if (countryMap == null) {
                countryMap = HashMap<String, String>()
                regionsMap.put(countryId, countryMap)
            }
            val regionKey = it.key!!
            var regionName:String? = null
            try {
                regionName = messageSource.getMessage(regionKey, null, locale)
            } catch (e: NoSuchMessageException) {
                logger.warn("Region definition not found for key " + regionKey + " and locale " + locale)
            }
            if (regionName == null) {
                regionName = it.name
            }
            val regionId = it.regionId!!
            countryMap.put(regionId, regionName as String)
        }
        return regionsMap
    }
}