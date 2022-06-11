package mrcs.mywallet.services.login

import mrcs.mywallet.domain.User
import mrcs.mywallet.services.Api
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

class LoginServices {
    fun login(loginData: LoginData): Call<User> {
        val retrofit = Api().config()
        val loginHandler = retrofit.create(LoginRequest::class.java)

        return loginHandler.login(loginData)
    }
}

interface LoginRequest {
    @Headers("Content-Type: application/json")
    @POST("sign-in")
    fun login(@Body userData: LoginData ): Call<User>
}
