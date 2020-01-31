package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mReceta
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*
import kotlin.collections.ArrayList

interface requestReceta{

    @GET("receta/paciente/{pacienteid}")
    fun getRecetaPaciente(@Path("pacienteid") pacienteid :  Long): Observable<ArrayList<mReceta>>

}