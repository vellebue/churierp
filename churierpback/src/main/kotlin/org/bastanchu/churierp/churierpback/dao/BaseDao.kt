package org.bastanchu.churierp.churierpback.dao

interface  BaseDao <K,E> {

    fun getById(id:K):E

    fun filter(filter:E):List<E>

    fun genericFilter(filter:Any):List<E>

    fun listAll():List<E>

    fun create(entity:E)

    fun flush()

}