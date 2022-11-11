package org.bastanchu.churierp.churierpback.util.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class ListField(val selected:Boolean = true, val keyField:Boolean = false)
