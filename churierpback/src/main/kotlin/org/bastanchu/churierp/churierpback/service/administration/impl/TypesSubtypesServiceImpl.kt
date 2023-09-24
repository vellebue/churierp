package org.bastanchu.churierp.churierpback.service.administration.impl

import org.bastanchu.churierp.churierpback.dao.administration.types.AreaDao
import org.bastanchu.churierp.churierpback.dao.administration.types.SubtypesDao
import org.bastanchu.churierp.churierpback.dao.administration.types.TypedEntityDao
import org.bastanchu.churierp.churierpback.dao.administration.types.TypesDao
import org.bastanchu.churierp.churierpback.dto.administration.types.AreaDto
import org.bastanchu.churierp.churierpback.dto.administration.types.TypeDto
import org.bastanchu.churierp.churierpback.entity.administration.types.Subtype
import org.bastanchu.churierp.churierpback.entity.administration.types.Type
import org.bastanchu.churierp.churierpback.entity.administration.types.TypePK
import org.bastanchu.churierp.churierpback.service.administration.TypesSubtypesService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Service
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED)
class TypesSubtypesServiceImpl(@Autowired val areaDao : AreaDao,
                               @Autowired val typedEntityDao: TypedEntityDao,
                               @Autowired val typesDao : TypesDao,
                               @Autowired val subtypesDao : SubtypesDao,
                               @Autowired val messageSource : MessageSource) : TypesSubtypesService {

    val logger = LoggerFactory.getLogger(TypesSubtypesServiceImpl::class.java)

    override fun getFullTypesData(): List<AreaDto> {
        val areasList = ArrayList<AreaDto>()
        val areas = areaDao.listAll()
        // Load areas
        areas.forEach {
            val areaDto = areaDao.toDataTransferObject(it)
            var areaKey : String? = areaDto.key
            var areaDescription = ""
            try {
                if (areaKey == "") { areaKey = null}
                areaDescription = messageSource.getMessage(areaKey, null,
                    LocaleContextHolder.getLocale())
                areaDto.description = areaDescription
            } catch (e: Exception) {
                logger.debug("Area key ${areaKey} not found, use default value ${areaDto.description}")
            }
            areaDto.typedEntities = typedEntityDao.toDataTransferObjectList(it.typedEntities.stream().collect(Collectors.toList()))
            // Load entity types
            it.typedEntities.forEach {
                val typedEntity = it
                areaDto.typedEntities?.forEach {
                    val typedEntityDto = it
                    var typedEntityKey : String? = typedEntityDto.key
                    var typedEntityDescription = ""
                    try {
                        if (typedEntityKey == "") { typedEntityKey = null }
                        typedEntityDescription = messageSource.getMessage(typedEntityKey, null, LocaleContextHolder.getLocale())
                        typedEntityDto.description = typedEntityDescription
                    } catch (e: NoSuchMessageException) {
                        logger.debug("Typed entity key ${typedEntityKey} with no value, use default value ${typedEntityDto.description}")
                    }
                    if ((typedEntity.areaId == typedEntityDto.areaId) &&
                        (typedEntity.id == typedEntityDto.id)) {
                        // Load types
                        typedEntityDto.types = typesDao.toDataTransferObjectList(typedEntity.types.stream().collect(Collectors.toList()))
                        typedEntity.types.forEach {
                            val type = it
                            typedEntityDto.types?.forEach {
                                val typeDto = it
                                val typeKey = typeDto.key
                                var typeDescription = ""
                                try {
                                    typeDescription = messageSource.getMessage(typeKey, null,
                                        LocaleContextHolder.getLocale())
                                    typeDto.description = typeDescription
                                } catch (e: NoSuchMessageException) {
                                    logger.debug("Type key ${typeKey} with no value, use default value ${typeDto.description}")
                                }
                                if ((type.areaId == typeDto.areaId) &&
                                    (type.entityId == typeDto.entityId) &&
                                    (type.typeId == typeDto.typeId)) {
                                    //Load subtypes
                                    typeDto.subtypes = subtypesDao.toDataTransferObjectList(type.subtypes.stream().collect(Collectors.toList()))
                                    typeDto.subtypes?.forEach {
                                        val subtypeDto = it
                                        val subtypeKey = subtypeDto.key
                                        var subtypeDescription = ""
                                        try {
                                            subtypeDescription = messageSource.getMessage(subtypeKey, null,
                                                LocaleContextHolder.getLocale())
                                            subtypeDto.description = subtypeDescription
                                        } catch (e: NoSuchMessageException) {
                                            logger.debug("Subtype key ${subtypeKey} with no value, use default value ${subtypeDto.description}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            areasList.add(areaDto)
        }
        return areasList
    }

    override fun getTypesMap(areaId: Int, typedEntityId: Int): Map<String, String> {
        val typesMap = HashMap<String, String>()
        val typeFilter  = Type()
        typeFilter.areaId = areaId as Integer
        typeFilter.entityId = typedEntityId as Integer
        val typesList = typesDao.filter(typeFilter)
        typesList.forEach {
            val typeId = it.typeId!!
            var description = ""
            val descriptionKey = it.key
            try {
                description = messageSource.getMessage(descriptionKey, null, LocaleContextHolder.getLocale())
            } catch (e: NoSuchMessageException) {
                logger.debug("Type key ${descriptionKey} with no value, use default value ${it.description}")
                description = it.description!!
            }
            typesMap.put(typeId, description)
        }
        return typesMap
    }

    override fun getSubtypesMap(areaId: Int, typedEntityId: Int): Map<String, Map<String, String>> {
        val typesSubtypesMap = HashMap<String, Map<String,String>>()
        val typeFilter  = Type()
        typeFilter.areaId = areaId as Integer
        typeFilter.entityId = typedEntityId as Integer
        val typesList = typesDao.filter(typeFilter)
        typesList.forEach {
            val typeId = it.typeId!!
            val subtypesMap = HashMap<String, String>()
            it.subtypes.forEach {
                val subtypeId = it.subtypeId!!
                var description = ""
                val descriptionKey = it.key
                try {
                    description = messageSource.getMessage(descriptionKey, null, LocaleContextHolder.getLocale())
                } catch (e: NoSuchMessageException) {
                    logger.debug("Subtype key ${descriptionKey} with no value, use default value ${it.description}")
                    description = it.description!!
                }
                subtypesMap.put(subtypeId, description)
            }
            typesSubtypesMap.put(typeId, subtypesMap)
        }
        return typesSubtypesMap;
    }

    override fun getType(areaId: Int, typedEntityId: Int, typeId: String): TypeDto? {
        val typePk = TypePK(areaId as Integer, typedEntityId as Integer, typeId)
        val type = typesDao.getById(typePk)
        if (type != null) {
            val typeDto = typesDao.toDataTransferObject(type)
            typeDto.subtypes = subtypesDao.toDataTransferObjectList(type.subtypes.stream().collect(Collectors.toList()))
            return typeDto
        } else {
            return null
        }
    }

    override fun createType(typeDto: TypeDto) {
        val type = typesDao.fromDtoToEntity(typeDto)
        typesDao.create(type)
    }

    override fun updateType(typeDto: TypeDto) {
        val type = typesDao.fromDtoToEntity(typeDto)
        val subtypesArray = subtypesDao.fromDtoToEntityList(typeDto!!.subtypes)
        (type.subtypes as MutableSet<Subtype>).removeIf({ true })
        (type.subtypes as MutableSet<Subtype>).addAll(subtypesArray)
    }

    override fun deleteType(typeDto: TypeDto) {
        val type = typesDao.fromDtoToEntity(typeDto)
        if (type.subtypes != null) {
            type.subtypes.forEach {
                subtypesDao.delete(it)
            }
        }
        typesDao.delete(type)
    }
}