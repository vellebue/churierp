package org.bastanchu.churierp.churierpback.dao

interface BaseDtoDao <K,E, D> : BaseDao<K,E> {

    public fun toDataTransferObject(entity:E):D;

    public fun toDataTransferObjectList(entityList:List<E>):List<D>;

    public fun toDataTransferObject(entity:E, dto:D);

    public fun fromDtoToEntity(dto:D):E;

    public fun fromDtoToEntity(dto:D, entity:E);
}