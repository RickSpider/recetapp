package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mMedico
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface requestMedico{

    @GET("medico/{id}")
    fun getOneMedico(@Path("id") id :  Long): Observable<mMedico>


}