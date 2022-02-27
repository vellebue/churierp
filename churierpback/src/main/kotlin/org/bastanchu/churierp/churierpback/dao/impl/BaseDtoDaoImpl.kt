package org.bastanchu.churierp.churierpback.dao.impl

import org.bastanchu.churierp.churierpback.dao.BaseDao
import org.bastanchu.churierp.churierpback.dao.BaseDtoDao
import org.bastanchu.churierp.churierpback.util.CopyUtil
import java.lang.RuntimeException
import java.lang.reflect.ParameterizedType
import javax.persistence.EntityManager

open abstract class BaseDtoDaoImpl<K, E, D> (override val entityManager: EntityManager) : BaseDaoImpl<K, E>(entityManager), BaseDtoDao<K, E, D> {

    var dtoClassTypeClass : Class<D>? = null;

    init {
        val genericSuperclass = this::class.java.genericSuperclass;
        if (genericSuperclass is ParameterizedType) {
            val parameterizedType : ParameterizedType = genericSuperclass;
            dtoClassTypeClass = parameterizedType.actualTypeArguments[2] as Class<D>;
        }
    }

    override fun toDataTransferObject(entity: E): D {
        val dto:D? = dtoClassTypeClass?.getDeclaredConstructor()?.newInstance();
        if (dto != null) {
            toDataTransferObject(entity, dto);
            return dto;
        } else {
            throw RuntimeException("No dto class defined");
        }
    }

    override fun toDataTransferObject(entity: E, dto: D) {
        val copyUtils = CopyUtil.Instance;
        val entityAny = entity as Any;
        val dtoAny = dto as Any;
        copyUtils.copyValues(entityAny, dtoAny);
    }

    override fun fromDtoToEntity(dto:D):E {
        val copyUtils = CopyUtil.Instance;
        val candidateEntity = entityClassTypeClass?.getDeclaredConstructor()?.newInstance();
        val keyValue = getKeyValue(candidateEntity);
        if (keyValue != null) {
            val targetEntity = getById(keyValue);
            if (targetEntity != null) {
                copyUtils.copyValues(dto as Any, targetEntity as Any);
                return targetEntity;
            } else {
                copyUtils.copyValues(dto as Any, candidateEntity as Any);
                return candidateEntity;
            }
        } else {
            copyUtils.copyValues(dto as Any, candidateEntity as Any);
            return candidateEntity;
        }
    }

    override fun fromDtoToEntity(dto:D, entity:E) {
        val copyUtils = CopyUtil.Instance;
        copyUtils.copyValues(dto as Any, entity as Any);
    }
}