package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mReceta
import com.blackspider.recetapp.model.mRecetaDetalle
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import kotlin.collections.ArrayList

interface requestReceta{

    @GET("receta/paciente/{pacienteid}")
    fun getRecetaPaciente(@Path("pacienteid") pacienteid :  Long): Observable<ArrayList<mReceta>>

    @GET("receta/paciente/{pacienteid}/{medicoid}")
    fun getRecetaPaciente(@Path("pacienteid") pacienteid :  Long,@Path("medicoid") medicoid : Long): Observable<ArrayList<mReceta>>

    @GET("receta/detalles/{recetaid}")
    fun getRecetaDetalles(@Path("recetaid") recetaid : Long): Observable<ArrayList<mRecetaDetalle>>

    @GET("receta/{recetaid}")
    fun getReceta (@Path("recetaid") recetaid : Long) : Observable<mReceta>

    @POST
    fun postReceta (@Body mreceta : mReceta) : Completable

}