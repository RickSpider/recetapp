package com.blackspider.recetapp.request


import com.blackspider.recetapp.model.mCiudad
import io.reactivex.Observable
import retrofit2.http.GET


interface requestCiudad{

    @GET("ciudades")
    fun getCiudades(): Observable<ArrayList<mCiudad>>

}