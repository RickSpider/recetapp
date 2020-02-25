package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mMedico
import com.blackspider.recetapp.model.mPaciente
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface requestMedico {

    @GET("medico/{id}")
    fun getOneMedico(@Path("id") id: Long): Observable<mMedico>

    @POST("medicos")
    fun postMedico(@Body mmmedico: mMedico): Completable

}