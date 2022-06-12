package mrcs.mywallet.services.records

import mrcs.mywallet.domain.Records
import mrcs.mywallet.services.Api
import retrofit2.Call
import retrofit2.http.*

class RecordsServices {
    fun getRecords(token: String): Call<Records> {
        val retrofit = Api().config()
        val getRecordsHandler = retrofit.create(GetRecordsRequest::class.java)

        return getRecordsHandler.getRecords(token)
    }
}

interface GetRecordsRequest {
    @Headers("Content-Type: application/json")
    @GET("transactions")
    fun getRecords(@Header("Authorization: Bearer") authorization:String): Call<Records>
}
