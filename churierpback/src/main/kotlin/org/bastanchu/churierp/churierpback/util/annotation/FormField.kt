package org.bastanchu.churierp.churierpback.util.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class  FormField(val groupId:Int, val indexInGroup:Int,
                           val field:String = "", val from:Boolean = false, val to:Boolean = false,
                           val readOnly:Boolean = false, val password:Boolean = false, val colSpan:Int = 1,
                           val widthPercentage:Double = 50.0,
                           val maxWidthInPixels:Double = 0.0,
                           val comboBoxConfiguration:ComboBoxConfiguration = ComboBoxConfiguration())
