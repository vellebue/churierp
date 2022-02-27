package org.bastanchu.churierp.churierpback.util.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Field(val key:String, val visible:Boolean = true)
