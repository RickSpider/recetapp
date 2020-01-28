package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mPaciente
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface requestPaciente{

    @GET("/paciente/{pacienteid}")
    fun getPaciente(@Path("pacienteid") pacienteid :  Long): Observable<mPaciente>

}