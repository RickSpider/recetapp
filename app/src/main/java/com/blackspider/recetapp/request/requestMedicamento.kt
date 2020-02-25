package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mMedicamento
import io.reactivex.Observable
import retrofit2.http.GET

interface requestMedicamento{

    @GET("medicamentos")
    fun getmedicamento(): Observable<ArrayList<mMedicamento>>

}