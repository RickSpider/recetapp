package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mMedico
import com.blackspider.recetapp.model.mPaciente
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*

interface requestMedico {

    @GET("medico/{id}")
    fun getOneMedico(@Path("id") id: Long): Observable<mMedico>

    @POST("medico")
    fun postMedico(@Body mmedico: mMedico): Completable

    @PUT("medico")
    fun putMedico(@Body mmedico : mMedico) : Completable

}