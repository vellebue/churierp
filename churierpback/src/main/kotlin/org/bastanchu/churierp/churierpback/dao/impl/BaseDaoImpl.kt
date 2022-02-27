package org.bastanchu.churierp.churierpback.dao.impl

import org.bastanchu.churierp.churierpback.dao.BaseDao
import org.bastanchu.churierp.churierpback.util.annotation.FormField
import org.hibernate.Session
import org.slf4j.LoggerFactory
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import javax.persistence.EntityManager
import javax.persistence.Id
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root


open class BaseDaoImpl<K,E> (open val entityManager: EntityManager)  : BaseDao<K,E> {

    val logger = LoggerFactory.getLogger(BaseDaoImpl::class.java);

    var entityClassTypeClass : Class<E>? = null;
    var keyClassTypeClass : Class<K>? = null;

    init {
        val genericSuperclass = this::class.java.genericSuperclass;
        if (genericSuperclass is ParameterizedType) {
            val parameterizedType : ParameterizedType = genericSuperclass;
            keyClassTypeClass = parameterizedType.actualTypeArguments[0] as Class<K>;
            entityClassTypeClass = parameterizedType.actualTypeArguments[1] as Class<E>;
        }
    }

    override fun getById(id: K): E {
        val entity = entityManager.find(entityClassTypeClass, id);
        return entity;
    }

    override fun filter(filter: E): List<E> {
        val session = entityManager.unwrap(Session::class.java);
        val builder = session.criteriaBuilder;
        var criteriaQuery = builder.createQuery(entityClassTypeClass) as CriteriaQuery<E>;
        var root = criteriaQuery.from(entityClassTypeClass) as Root<E>
        val filterDeclaredFields = entityClassTypeClass?.declaredFields;
        criteriaQuery = criteriaQuery.select(root);
        if (filterDeclaredFields != null) {
            for (field in filterDeclaredFields) {
                field.isAccessible = true;
                val fieldValue = field.get(filter);
                val fieldName = field.name;
                if (fieldValue != null) {
                    criteriaQuery = criteriaQuery.where(builder.equal(root.get<Any>(fieldName), fieldValue));
                }
            }
        }
        val query = session.createQuery(criteriaQuery);
        val list = query.resultList;
        return list;
    }

    override fun listAll(): List<E> {
        val entityNameQualified = entityClassTypeClass?.name;
        val entityNameSplitted = entityNameQualified?.split(".");
        var entityName:String? = null;
        if (entityNameSplitted != null) {
            entityName = entityNameSplitted[entityNameSplitted.lastIndex];
        }
        if (entityName != null) {
            val stringQuery = "select e from " + entityName + " e";
            val query = entityManager.createQuery(stringQuery);
            val list = query.resultList as List<E>;
            return list;
        } else {
            throw RuntimeException("Entity class not defined");
        }
    }

    override fun genericFilter(filter: Any): List<E> {
        val qualifiedEntityName = entityClassTypeClass?.name;//?.name?.split('.')[0];
        if (qualifiedEntityName != null) {
            val entityNameQuals = qualifiedEntityName.split('.');
            val entityName = entityNameQuals[entityNameQuals.lastIndex];
            var queryString = StringBuilder("select e from " + entityName + " e \n");
            val fields = filter::class.java.declaredFields;
            var firstField = true;
            var values = ArrayList<Any>();
            var index = 1;
            for(field in fields) {
                val value = getFilterValue(field, filter);
                if (field.getAnnotation(FormField::class.java) != null && (value != null)) {
                    if (firstField) {
                        queryString.append("where " + buildGenericFilterClause(field, value, index) + "\n");
                        firstField = false;
                    } else {
                        queryString.append("and " + buildGenericFilterClause(field, value, index) + "\n");
                    }
                    values.add(value);
                    index++;
                }
            }
            // TODO Invoke query
            logger.debug("Generic filter query:\n" + queryString);
            val query = entityManager.createQuery(queryString.toString());
            index = 1;
            for (value in values) {
                query.setParameter(index, value);
                index++;
            }
            return query.resultList as List<E>;
        } else {
            return ArrayList<E>();
        }
    }

    private fun buildGenericFilterClause(field: Field, value:Any, index:Int):String {
        val formFieldAnnotataion = field.getAnnotation(FormField::class.java);
        if (formFieldAnnotataion != null) {
            var clause = StringBuilder("( ");
            var fieldName = if(formFieldAnnotataion.field == "")  "e." + field.name
                            else "e." + formFieldAnnotataion.field;
            if (formFieldAnnotataion.from) {
                clause.append("?" +index + " <= " + fieldName)
            } else if (formFieldAnnotataion.to) {
                clause.append(fieldName + " <= ?"+ index)
            } else {
                if ((value is String) && value.contains('%')) {
                    clause.append(fieldName + " like ?" + index);
                } else {
                    clause.append(fieldName + " = ?" + index)
                }
            }
            clause.append(" )");
            return clause.toString();
        } else {
            throw RuntimeException("Invalid field for clause: " + field.name);
        }
    }

    private fun getFilterValue(field:Field, thisObject:Any):Any? {
        field.isAccessible= true;
        val candidateValue = field.get(thisObject);
        var value:Any? = null;
        if (candidateValue is String && !(candidateValue.trim() == "")) {
            value = candidateValue.replace('*','%');
        } else if (candidateValue is String) {
            value = null;
        } else {
            value = candidateValue;
        }
        return value;
    }

    protected fun getKeyValue(entity:E?):K? {
        if (entity != null) {
            val declaredFields = entityClassTypeClass?.declaredFields;
            val keyDeclaredFields = declaredFields?.filter {
                val annotation = it.getAnnotation(Id::class.java);
                annotation != null;
            };
            if ((keyDeclaredFields == null) || keyDeclaredFields.isEmpty()) {
                // No key value annotation found it may not happen
                return null;
            } else if (keyDeclaredFields.size == 1) {
                // Single field key found, the most common case
                val declaredKeyField = keyDeclaredFields.get(0);
                declaredKeyField.isAccessible = true;
                val keyValue = declaredKeyField.get(entity);
                return keyValue as K;
            } else {
                // TODO Multiple key value
                return null;
            }
        } else {
            return null;
        }
    }

    override fun create(entity: E) {
        entityManager.persist(entity);
    }
}
