package org.bastanchu.churierp.churierpback.entity

interface TypedEntity {

    companion object {

        fun typesInUse(): List<String> {
            return ArrayList<String>()
        }

        fun subtypesInUse() : Map<String, String> {
            return HashMap<String, String>()
        }

    }
}