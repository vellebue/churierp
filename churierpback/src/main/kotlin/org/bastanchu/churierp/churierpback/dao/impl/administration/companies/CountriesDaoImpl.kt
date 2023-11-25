package org.bastanchu.churierp.churierpback.dao.impl.administration.companies

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
//import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.bastanchu.churierp.churierpback.dao.administration.companies.CountriesDao
import org.bastanchu.churierp.churierpback.dao.administration.companies.RegionsDao
import org.bastanchu.churierp.churierpback.dao.impl.BaseDtoDaoImpl
import org.bastanchu.churierp.churierpback.dto.administration.companies.AreaDto
import org.bastanchu.churierp.churierpback.dto.administration.companies.CountryDto
import org.bastanchu.churierp.churierpback.entity.administration.companies.Country
import org.bastanchu.churierp.churierpback.entity.administration.companies.Region
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.io.InputStreamReader
import javax.annotation.PostConstruct
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class CountriesDaoImpl(@PersistenceContext(unitName = "entityManagerFactory") override val entityManager: EntityManager)
    : BaseDtoDaoImpl<String, Country, CountryDto>(entityManager),
      CountriesDao {

    @Autowired
    var thisDao:CountriesDao? = null
    @Autowired
    var regionsDao:RegionsDao? = null

    val COUNTRIES_FILE = "db/data/countries.json";
    val REGIONS_FILE = "db/data/regions.json";

    val daoLogger = LoggerFactory.getLogger(CountriesDaoImpl::class.java)

    @PostConstruct
    fun initialize() {
        thisDao?.initializeCountriesRegions()
    }

    override fun initializeCountriesRegions() {
        //initializeCountries()
        //initializeRegions()
    }

    @Deprecated("No longer required, database countries are initielized using database script.")
    private fun initializeCountries() {
        val previousCountriesDb = listAll()
        val loader = Thread.currentThread().contextClassLoader
        val countriesStream = InputStreamReader(loader.getResourceAsStream(COUNTRIES_FILE))
        if (countriesStream != null && (previousCountriesDb.size == 0)) {
            countriesStream.use {
                val countriesObjectMapper = ObjectMapper()//.registerModule(KotlinModule())
                val areaDtoList = countriesObjectMapper.readValue<ArrayList<AreaDto>>(it, object : TypeReference<ArrayList<AreaDto>>(){})
                daoLogger.info("Parsed countries list")
                for (areaItem in areaDtoList) {
                    var country = Country()
                    country.countryId = areaItem.code
                    country.name = areaItem.name
                    country.key = "churierpweb.country." + areaItem.code
                    thisDao?.create(country)
                    logger.info("Created country: " + country.countryId + " " + country.name)
                }
            }
        }
    }

    @Deprecated("No longer required, database regions are initielized using database script.")
    private fun initializeRegions() {
        val previousRegions = regionsDao?.listAll()
        val loader = Thread.currentThread().contextClassLoader
        val regionsStream = InputStreamReader(loader.getResourceAsStream(REGIONS_FILE))
        if (regionsStream != null && (previousRegions?.size == 0)) {
            regionsStream.use {
                val regionsObjectMapper = ObjectMapper()
                val areaDtoList = regionsObjectMapper.readValue<ArrayList<AreaDto>>(it, object : TypeReference<ArrayList<AreaDto>>(){})
                for (areaItem in areaDtoList) {
                    var region = Region()
                    region.countryId = areaItem.country
                    region.regionId = areaItem.code
                    region.name = areaItem.name
                    region.key = "churierpweb.region." + areaItem.country + "." + areaItem.code
                    regionsDao?.create(region)
                    logger.info("Created region: " +  region.countryId+  " " + region.regionId + " " + region.name)
                }
            }
        }
    }
}