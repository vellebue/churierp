package org.bastanchu.churierp.churierpback.service.administration

import java.util.Locale

interface RegionService {

    fun getRegionsMap(locale:Locale):Map<String, Map<String, String>>

}