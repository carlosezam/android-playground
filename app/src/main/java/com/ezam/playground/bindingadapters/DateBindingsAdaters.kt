package com.ezam.playground.bindingadapters

import android.app.DatePickerDialog
import android.widget.TextView
import androidx.core.os.ConfigurationCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener

import com.ezam.playground.commons.simpleDateFormatOf
import com.ezam.playground.customviews.InputDatePicker
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("date","dateFormat")
fun TextView.setDate(date: Date?, format: String){
    val stringDate = date?.let {
        simpleDateFormatOf( format, resources.configuration ).format( it )
    }
    setText( stringDate ?: "")
}







