package com.ezam.playground.commons

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.view.View
import androidx.core.os.ConfigurationCompat
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

val Context.locale: Locale
    get() = ConfigurationCompat.getLocales( resources.configuration )[0]

fun dateFormat() = dateFormatOf( null, Resources.getSystem().configuration )

fun dateFormatOf( format: String?, configuration: Configuration ) : DateFormat {

    val locale = ConfigurationCompat.getLocales(configuration)[0]

    if( format.isNullOrBlank() ){
        return DateFormat.getDateInstance( DateFormat.MEDIUM, locale )
    } else {
        return SimpleDateFormat( format, locale )
    }
}

fun simpleDateFormatOf( format: String, configuration: Configuration) : SimpleDateFormat {
    return SimpleDateFormat(format, ConfigurationCompat.getLocales(configuration)[0] )
}

fun String?.nullIfBlank() : String? = if( isNullOrBlank() ) null else this

fun DateFormat.formatOrNull(date: Date?) : String? {
    val value = date ?: return null
    return try{ format(value) } catch (e: ParseException){ null }
}