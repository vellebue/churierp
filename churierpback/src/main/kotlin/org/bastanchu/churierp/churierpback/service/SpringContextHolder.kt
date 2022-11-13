package org.bastanchu.churierp.churierpback.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service

@Component
class SpringContextHolder(@Autowired val appContext : ApplicationContext)  {


    companion object instance {

        var appContext : ApplicationContext? = null

        fun getCurrentApplicationContext():ApplicationContext? {
            return appContext;
        }
    }


    init {
        instance.appContext = this.appContext
        //println("Active context holder")
    }
}