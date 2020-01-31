package com.blackspider.recetapp.recursos

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

//val URLdoxa = "http://181.126.90.59:5050/"
//val URLdoxa = "http://200.24.0.200:5050/"
val URLdoxa = "http://app.doxa.com.py:8080/recetapp/"

fun connector(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(URLdoxa)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}