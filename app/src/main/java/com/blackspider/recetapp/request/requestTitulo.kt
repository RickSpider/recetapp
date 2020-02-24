package com.blackspider.recetapp.request


import com.blackspider.recetapp.model.mTitulo
import io.reactivex.Observable
import retrofit2.http.GET

interface requestTitulo{

    @GET("titulo")
    fun getTitulos() : Observable<List<mTitulo>>

}