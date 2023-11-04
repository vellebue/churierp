package org.bastanchu.churierp.churierpback.dao.impl

import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.util.CopyUtil
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType
import javax.persistence.EntityManager
import java.util.stream.Collectors

open abstract class BaseDtoDaoImpl<K, E, D> (override val entityManager: EntityManager) : BaseDaoImpl<K, E>(entityManager), BaseDtoDao<K, E, D> {

    var dtoClassTypeClass : Class<D>? = null

    init {
        val genericSuperclass = this::class.java.genericSuperclass
        if (genericSuperclass is ParameterizedType) {
            val parameterizedType : ParameterizedType = genericSuperclass
            dtoClassTypeClass = parameterizedType.actualTypeArguments[2] as Class<D>
        }
    }

    final override fun toDataTransferObject(entity: E): D {
        val dto:D? = dtoClassTypeClass?.getDeclaredConstructor()?.newInstance()
        if (dto != null) {
            toDataTransferObject(entity, dto)
            return dto
        } else {
            throw RuntimeException("No dto class defined")
        }
    }

    final override fun toDataTransferObjectList(entityList: List<E>): List<D> {
        return entityList.stream().map { toDataTransferObject(it) }.collect(Collectors.toList())
    }

    override fun toDataTransferObject(entity: E, dto: D) {
        val copyUtils = CopyUtil.Instance
        val entityAny = entity as Any
        val dtoAny = dto as Any
        copyUtils.copyValues(entityAny, dtoAny)
    }

    override fun fromDtoToEntity(dto:D):E {
        val copyUtils = CopyUtil.Instance
        val candidateEntity = entityClassTypeClass?.getDeclaredConstructor()?.newInstance()
        copyUtils.copyValues(dto as Any, candidateEntity as Any)
        val keyValue = getKeyValue(candidateEntity)
        if (keyValue != null) {
            val targetEntity = getById(keyValue)
            if (targetEntity != null) {
                //copyUtils.copyValues(dto as Any, targetEntity as Any);
                fromDtoToEntity(dto, targetEntity)
                return targetEntity
            } else {
                //copyUtils.copyValues(dto as Any, candidateEntity as Any);
                //fromDtoToEntity(dto, targetEntity)
                return candidateEntity
            }
        } else {
            //copyUtils.copyValues(dto as Any, candidateEntity as Any);
            fromDtoToEntity(dto, candidateEntity)
            return candidateEntity
        }
    }

    override fun fromDtoToEntity(dto:D, entity:E) {
        val copyUtils = CopyUtil.Instance
        copyUtils.copyValues(dto as Any, entity as Any)
    }

    override fun fromDtoToEntityList(dtoList: List<D>?): List<E> {
        if (dtoList != null) {
            return dtoList.stream().map { fromDtoToEntity(it) }.collect(Collectors.toList())
        } else {
            return ArrayList()
        }
    }
}