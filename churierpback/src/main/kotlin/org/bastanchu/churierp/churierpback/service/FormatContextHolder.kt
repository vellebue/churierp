package org.bastanchu.churierp.churierpback.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component
import org.springframework.stereotype.Service
import java.text.SimpleDateFormat
import java.util.*

@Component
class FormatContextHolder(@Autowired val msgSource : MessageSource) {

    companion object instance {

        var messageSource : MessageSource? = null

        fun formatDate(sourceDate : Date) : String {
            val stringDateFormat = messageSource!!.getMessage("churierpweb.dateFormat",
                null, LocaleContextHolder.getLocale())
            val dateFormat = SimpleDateFormat(stringDateFormat)
            val dateFormatted = dateFormat.format(sourceDate)
            return dateFormatted
        }
    }

    init {
        instance.messageSource = this.msgSource
    }
}