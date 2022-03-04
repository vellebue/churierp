package org.bastanchu.churierp.churierpback.dto

import org.bastanchu.churierp.churierpback.util.annotation.FormField
import java.util.*

class TestEntityFilterDto {

    @FormField(groupId = 0, indexInGroup = 0)
    var id:Integer? = null

    @FormField(groupId = 1, indexInGroup = 0)
    var text:String? = null

    @FormField(groupId = 1, indexInGroup = 1, field = "eventDate", from = true)
    var eventDateFrom: Date? = null;

    @FormField(groupId = 1, indexInGroup = 2, field = "eventDate", to = true)
    var eventDateTo: Date? = null;
}