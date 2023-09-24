package org.bastanchu.churierp.churierpback.service.administration

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.slf4j.LoggerFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import java.util.*

class TypesSubtypesServiceReadOperationsITCase(@Autowired val typesSubtypesService : TypesSubtypesService,
                                               @Autowired val messageSource: MessageSource) : BaseContainerDBITCase() {

    val logger = LoggerFactory.getLogger(TypesSubtypesServiceReadOperationsITCase::class.java)

    override fun getScriptContent(): String {
        return """
            insert into C_TYPED_AREAS (id, key, description)
            values (99, '', 'CustomArea');
            insert into C_TYPED_ENTITIES(area_id, id, java_class, key, description, allow_subtypes)
            values (0,2, 'org.bastanchu.NonExistingJavaClass', '', 'CustomTestUserEntity', true);
            COMMIT;
        """.trimIndent()
    }

    @Test
    fun shouldGetFullTypesDataGatherAreasTypedEntitiesTypesAndSubtypesConsistently() {
        logger.info("Retrieving areas, entities, types and subtypes")
        //LocaleContextHolder.setDefaultLocale(Locale("en","EN"))
        val areasList = typesSubtypesService.getFullTypesData()
        // Test the presence of all defined areas
        val optionalAdministrationArea = areasList.stream()
            .filter { it.description == "Administration" }.findAny();
        assertTrue(optionalAdministrationArea.isPresent, "Adminstration area from typed areas not found")
        val optionalAccountingArea = areasList.stream()
            .filter { it.description == "Accounting" }.findAny();
        assertTrue(optionalAccountingArea.isPresent, "Accounting area from typed areas not found")
        val optionalSalesArea = areasList.stream()
            .filter { it.description =="Sales" }.findAny();
        assertTrue(optionalSalesArea.isPresent, "Sales area from typed areas not found")
        val optionalCustomArea = areasList.stream()
            .filter { it.description == "CustomArea"}.findAny()
        // Test entities from Administration Area
        val administrationArea = optionalAdministrationArea.get()
        val optionalUserEntityFromAdministrationArea = administrationArea.typedEntities?.stream()
            ?.filter { it.description == "Users" }?.findAny()
        assertTrue(optionalUserEntityFromAdministrationArea?.isPresent!!, "Users Entity from Administration Area not found")
        val optionalCompanyEntityFromAdministrationArea = administrationArea.typedEntities?.stream()
            ?.filter { it.description == "Companies" }?.findAny()
        assertTrue(optionalCompanyEntityFromAdministrationArea?.isPresent!!, "Companies Entity from Administration Area not found")
        val optionCustomUserTestEntityFromAdminstationArea =  administrationArea.typedEntities?.stream()
            ?.filter { it.description == "CustomTestUserEntity" }?.findAny()
        assertTrue(optionCustomUserTestEntityFromAdminstationArea?.isPresent!!,
            "CustomTestUserEntity created ad hoc for Administration Area is not present")
    }

    @Test
    fun shouldRetrieveTypesMapProperlyAreaAdministrationEntityCompanies() {
        val companiesTypesMap = typesSubtypesService.getTypesMap(0, 1)
        //There is only a company type
        assertEquals(1, companiesTypesMap.keys.size)
        val singleKey = companiesTypesMap.keys.stream().findFirst().get()
        assertEquals("REG_COMP", singleKey)
        val singleValue = companiesTypesMap.getValue(singleKey)
        val singleValueTranslated = messageSource.getMessage(singleValue, null, Locale("en"))
        assertEquals("Regular company", singleValueTranslated)
    }

    @Test
    fun shouldRetrieveSubypesMapProperlyAreaAdministrationEntityCompanies() {
        val companiesSubtypesMapMap = typesSubtypesService.getSubtypesMap(0, 1)
        //There is only a company type
        assertEquals(companiesSubtypesMapMap.keys.size, 1)
        val singleTypeKey = companiesSubtypesMapMap.keys.stream().findFirst().get()
        assertEquals("REG_COMP", singleTypeKey)
        val subtypesMap = companiesSubtypesMapMap.get(singleTypeKey)!!
        // There is only a company subtype
        assertEquals(1, subtypesMap.keys.size)
        val singleSubtypeKey = subtypesMap.keys.stream().findFirst().get()
        assertEquals("DEF_REG_CO", singleSubtypeKey)
        val singleSubtypeValue = subtypesMap.getValue(singleSubtypeKey)
        val singleSubtypeValueResolved = messageSource.getMessage(singleSubtypeValue, null, Locale("en"))
        assertEquals("Default regular company", singleSubtypeValueResolved)
    }

    @Test
    fun shouldGetExampleTypeReturnFilledTypeDtoProperly() {
        val typeDto = typesSubtypesService.getType(0, 1, "REG_COMP")!!
        assertEquals(0, typeDto?.areaId)
        assertEquals(1, typeDto?.entityId)
        assertEquals("REG_COMP", typeDto?.typeId)
        assertEquals(typeDto?.description, "Regular company")
        val subtypeDto = typeDto.getSubtype("DEF_REG_CO")
        assertNotNull(subtypeDto)
        assertEquals(0, subtypeDto?.areaId)
        assertEquals(1, subtypeDto?.entityId)
        assertEquals("REG_COMP", subtypeDto?.typeId)
        assertEquals("DEF_REG_CO", subtypeDto?.subtypeId)
        assertEquals("Default regular company", subtypeDto?.description)
    }

    @Test
    fun shouldGetTypeReturnNullForNonExistingType() {
        val typeDto = typesSubtypesService.getType(0, 1, "NOT_EXIST")
        assertNull(typeDto)
    }
}