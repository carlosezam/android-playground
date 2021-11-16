package com.ezam.networkresponseadapter

import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test

class NetworkResponseCallTest{

    @Test
    fun nrWithJsonDecodeObject(){
        val person = nullableReified<Person>("""{"name":"carlos","age":"26"}""")
        Assert.assertEquals( Person("carlos",26), person )
    }

    @Test(expected = NullPointerException::class)
    fun decodeNullString_withNonNullableType_throwsNPE(){
        val person = nullableReified<Person>(null)
    }

    @Test
    fun decodeNull_withNullableType_returnsNull(){
        val person = nullableReified<Person?>(null)
        Assert.assertNull( person )
    }
}

@Serializable
data class Person(val name: String, val age: Int)

inline fun <reified T> nullableReified(string: String?) : T {

    if( string != null ){
        return Json.decodeFromString<T>( string!! )
    } else if ( null is T){
        return null as T
    } else {
        throw NullPointerException()
    }

}