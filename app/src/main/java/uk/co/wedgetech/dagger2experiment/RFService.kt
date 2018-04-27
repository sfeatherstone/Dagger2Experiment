package uk.co.wedgetech.dagger2experiment

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET

data class Host(val name:String, val url:String, val icon:String)

interface RFService {
    @GET("raw/1eb676f87f733f6a6a42d2272b1a74838c486ea3/sk_hosts/")
    fun loadHosts(): Observable<List<Host>>

    @GET("/")
    fun loadError(): Observable<List<Host>>
}