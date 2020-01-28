package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mMedicoPaciente
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface requestMedicoPaciente {

    @GET("/medicopaciente/{id}")
    fun getPacientesxMedicos(@Path("id") id: Long): Observable<ArrayList<mMedicoPaciente>>

}