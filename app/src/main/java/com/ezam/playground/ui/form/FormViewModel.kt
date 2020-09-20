package com.ezam.playground.ui.form

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class FormViewModel : ViewModel() {
    private var dateFormatLiveData = MutableLiveData("EEE, d MMM yyyy HH:mm:ss Z")
    private var dateLiveData = MutableLiveData<Date>( Calendar.getInstance().time )

    fun getDateFormat() : LiveData<String> = dateFormatLiveData
    fun getDate() = dateLiveData

    fun changeFormat(){
        dateFormatLiveData.postValue( "EEE d MMM")
    }
}