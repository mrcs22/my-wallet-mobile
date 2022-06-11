package mrcs.mywallet.services.signUp

import mrcs.mywallet.domain.User
import mrcs.mywallet.services.Api
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class SignUpServices {
    fun signUp(userData: User): Call<ResponseBody> {
        val retrofit = Api().config()
        val signUpHandler = retrofit.create(SignUpRequest::class.java)

       return signUpHandler.signUp(userData)
    }
}

interface SignUpRequest {
    @Headers("Content-Type: application/json")
    @POST("sign-up")
    fun signUp(@Body userData: User ): Call<ResponseBody>
}
