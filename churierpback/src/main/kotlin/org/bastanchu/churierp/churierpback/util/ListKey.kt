package org.bastanchu.churierp.churierpback.util

import org.bastanchu.churierp.churierpback.util.annotation.ListField

class ListKey {

    var keyMap = HashMap<String, Any>();

    companion object Instance {

        fun <T : Any> buildListKey(item: T) : ListKey {
            var result = ListKey()
            val itemClass = item::class.java
            itemClass.declaredFields.forEach {
                val listAnnotation = it.getAnnotation(ListField::class.java)
                if ((listAnnotation != null) && listAnnotation.keyField) {
                    it.trySetAccessible()
                    result.keyMap.put(it.name, it.get(item))
                }
            }
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (other is ListKey) {
            return keyMap.equals(other.keyMap)
        } else {
            return false
        }
    }

    override fun hashCode(): Int {
        return keyMap.hashCode()
    }
}