package org.bastanchu.churierp.churierpback.service.administration

import org.bastanchu.churierp.churierpback.BaseContainerDBITCase
import org.bastanchu.churierp.churierpback.dto.administration.types.SubtypeDto
import org.bastanchu.churierp.churierpback.dto.administration.types.TypeDto

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

import org.springframework.beans.factory.annotation.Autowired

class TypesSubtypesServiceUpdateOperationsITCase(@Autowired val typesSubtypesService: TypesSubtypesService) :BaseContainerDBITCase() {

    override fun getScriptContent(): String {
        return """
            insert into typed_types (area_id, entity_id, type_id, key, description, manageable)
            values (0, 0, 'IT_USER', null, 'IT Specialist', true);
            insert into typed_types (area_id, entity_id, type_id, key, description, manageable)
            values (0, 0, 'DELE_USER', null, 'User Type To Delete', true);
            INSERT INTO typed_subtypes
            (area_id, entity_id, type_id, subtype_id, "key", description, manageable)
            VALUES(0, 0, 'DELE_USER', 'DELE_S_USE', null, 'User Subtype To Delete', true);
            commit;
        """.trimIndent()
    }

    @Test
    fun shouldInsertTypesAndProperly() {
        // Define user type
        val customUserTypeDto = TypeDto()
        customUserTypeDto.areaId = 0 as Integer
        customUserTypeDto.entityId = 0 as Integer
        customUserTypeDto.typeId = "ADM_USER"
        customUserTypeDto.key = null
        customUserTypeDto.description = "Admin User"
        customUserTypeDto.manageable = true
        // Test user type
        typesSubtypesService.createType(customUserTypeDto)
        val customUserTypeDBDto = getTypeFromDB(customUserTypeDto.areaId!!, customUserTypeDto.entityId!!,
            customUserTypeDto.typeId!!)
        assertEquals(customUserTypeDto.areaId, customUserTypeDBDto?.areaId)
        assertEquals(customUserTypeDto.entityId, customUserTypeDBDto?.entityId)
        assertEquals(customUserTypeDto.typeId, customUserTypeDBDto?.typeId)
        assertEquals(customUserTypeDto.key, customUserTypeDBDto?.key)
        assertEquals(customUserTypeDto.description, customUserTypeDBDto?.description)
        assertEquals(customUserTypeDto.manageable, customUserTypeDBDto?.manageable)
    }

    @Test
    fun shouldUpdateTypesAndSubtypesProperly() {
        // Get type
        val typeDto = typesSubtypesService.getType(0, 0, "IT_USER")!!
        // Modify type
        typeDto.description = "My IT Specialist"
        val customUserSubtypeDto = SubtypeDto()
        customUserSubtypeDto.areaId = 0 as Integer
        customUserSubtypeDto.entityId = 0 as Integer
        customUserSubtypeDto.typeId = "IT_USER"
        customUserSubtypeDto.subtypeId = "IT_DEVEL"
        customUserSubtypeDto.key = null
        customUserSubtypeDto.description = "Software developer"
        customUserSubtypeDto.manageable = true
        val list = ArrayList<SubtypeDto>()
        list.add(customUserSubtypeDto)
        typeDto.subtypes = list
        // Update type
        typesSubtypesService.updateType(typeDto)
        // Get type from DB
        val typeDtoFromDB = getTypeFromDB(0 as Integer, 0 as Integer, "IT_USER")
        assertEquals(typeDto.areaId, typeDtoFromDB?.areaId)
        assertEquals(typeDto.entityId, typeDtoFromDB?.entityId)
        assertEquals(typeDto.typeId, typeDtoFromDB?.typeId)
        assertEquals(typeDto.key, typeDtoFromDB?.key)
        assertEquals(typeDto.description, typeDtoFromDB?.description)
        assertEquals(typeDto.manageable, typeDtoFromDB?.manageable)
        // Get subtype from DB
        val subtypeDtoFromDB = getSubtypeFromDB(0 as Integer, 0 as Integer, "IT_USER", "IT_DEVEL")
        assertEquals(customUserSubtypeDto.areaId, subtypeDtoFromDB?.areaId)
        assertEquals(customUserSubtypeDto.entityId, subtypeDtoFromDB?.entityId)
        assertEquals(customUserSubtypeDto.typeId, subtypeDtoFromDB?.typeId)
        assertEquals(customUserSubtypeDto.subtypeId, subtypeDtoFromDB?.subtypeId)
        assertEquals(customUserSubtypeDto.key, subtypeDtoFromDB?.key)
        assertEquals(customUserSubtypeDto.description, subtypeDtoFromDB?.description)
        assertEquals(customUserSubtypeDto.manageable, subtypeDtoFromDB?.manageable)
    }

    @Test
    fun shouldDeleteATypeWithSubtypesProperly() {
        val typeToDeleteDto = typesSubtypesService.getType(0, 0, "DELE_USER")!!
        assertNotNull(typeToDeleteDto)
        assertEquals(1, typeToDeleteDto?.subtypes?.size)
        typesSubtypesService.deleteType(typeToDeleteDto)
        val typeDbDto = getTypeFromDB(0 as Integer, 0 as Integer, "DELE_USER")
        assertNull(typeDbDto)
        val subtypeDbDto = getSubtypeFromDB(0 as Integer, 0 as Integer, "DELE_USER", "DELE_S_USE")
        assertNull(subtypeDbDto)
    }

    private fun getTypeFromDB(areaId : Integer, entityId : Integer, typeId : String) :TypeDto? {
        val sql = """
            select area_id, entity_id, type_id, key, description, manageable
            from typed_types
            where area_id = ? and entity_id = ? and type_id = ?
        """.trimIndent()
        val conn = dataSource?.connection!!
        conn.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, areaId.toInt())
            statement.setInt(2, entityId.toInt())
            statement.setString(3, typeId)
            statement.use {
                val resultSet = it.executeQuery()
                resultSet.use {
                    val typeDto = TypeDto()
                    if (it.next()) {
                        typeDto.areaId = resultSet.getInt("area_id") as Integer
                        typeDto.entityId = resultSet.getInt("entity_id") as Integer
                        typeDto.typeId = resultSet.getString("type_id")
                        typeDto.key = resultSet.getString("key")
                        typeDto.description = resultSet.getString("description")
                        typeDto.manageable = resultSet.getBoolean("manageable")
                        return typeDto
                    }
                }
            }
        }
        return null
    }

    private fun getSubtypeFromDB(areaId : Integer, entityId : Integer, typeId : String, subtypeId : String) : SubtypeDto? {
        val sql = """
            select area_id, entity_id, type_id, subtype_id, key, description, manageable
            from typed_subtypes
            where area_id = ? and entity_id = ? and type_id = ? and subtype_id = ?
        """.trimIndent()
        val conn = dataSource?.connection!!
        conn.use {
            val statement = it.prepareStatement(sql)
            statement.setInt(1, areaId.toInt())
            statement.setInt(2, entityId.toInt())
            statement.setString(3, typeId)
            statement.setString(4, subtypeId)
            statement.use {
                val resultSet = it.executeQuery()
                resultSet.use {
                    val subtypeDto = SubtypeDto()
                    if (it.next()) {
                        subtypeDto.areaId = resultSet.getInt("area_id") as Integer
                        subtypeDto.entityId = resultSet.getInt("entity_id") as Integer
                        subtypeDto.typeId = resultSet.getString("type_id")
                        subtypeDto.subtypeId = resultSet.getString("subtype_id")
                        subtypeDto.key = resultSet.getString("key")
                        subtypeDto.description = resultSet.getString("description")
                        subtypeDto.manageable = resultSet.getBoolean("manageable")
                        return subtypeDto
                    }
                }
            }
        }
        return null
    }
}