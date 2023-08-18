package org.bastanchu.churierp.churierpback.util

import java.lang.reflect.Field
import java.math.BigDecimal
import java.util.*

class CopyUtil {

    companion object Instance {

        val COPIABLE_TYPES: Array<Class<*>> = arrayOf(Integer::class.java, String::class.java,
                                                      Date::class.java, BigDecimal::class.java,
                                                      java.lang.Boolean::class.java)
        var COMPARATORS = HashMap<Class<out Any>, Comparator<out Any>>();

        init {
            COMPARATORS.put(Integer::class.java , Comparator({ a:Int, b:Int ->
                a.compareTo(b)
            }))
            COMPARATORS.put(String::class.java, Comparator({a:String, b:String ->
                a.compareTo(b)
            }))
            COMPARATORS.put(Date::class.java, Comparator({a:Date, b:Date ->
                a.compareTo(b)
            }))
            COMPARATORS.put(Boolean::class.java, Comparator({a:Boolean, b:Boolean ->
                a.compareTo(b)
            }))
        }

        fun <S : Any, T : Any> copyValues(source: S, target: T) {
            val declaredFields = source::class.java.declaredFields;
            for (field in declaredFields) {
                if (isCopiableType(field.type)) {
                    if (hasFieldWithName(target::class.java, field.name)) {
                        val targetField: Field = target::class.java.getDeclaredField(field.name);
                        if (targetField.type.isAssignableFrom(field.type)) {
                            targetField.isAccessible = true
                            field.isAccessible = true
                            targetField.set(target, field.get(source))
                        }
                    }
                }
            }
        }

        fun <T:Class<out Any>> getComparator(clazz:T):Comparator<out Any>? {
            return COMPARATORS.get(clazz)
        }

        private fun <T : Any> isCopiableType(type: Class<T>): Boolean {
            for (copiableType in COPIABLE_TYPES) {
                if (type.isAssignableFrom(copiableType)) {
                    return true
                }
            }
            return false
        }

        private fun <T : Any> hasFieldWithName(clazz: Class<T>, name: String): Boolean {
            val declaredFields = clazz.declaredFields
            for (field in declaredFields) {
                if (field.name.equals(name)) {
                    return true
                }
            }
            return false
        }
    }
}