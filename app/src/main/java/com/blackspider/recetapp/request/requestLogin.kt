package com.blackspider.recetapp.request

import com.blackspider.recetapp.model.mLogin
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface requestLogin{

    @POST("login")
    fun postLogin(@Body mlogin: mLogin): Observable<mLogin>

}