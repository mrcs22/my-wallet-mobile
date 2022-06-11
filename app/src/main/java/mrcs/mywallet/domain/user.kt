package mrcs.mywallet.domain

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class User(
  val name: String,
  val email: String,
  val password: String

)

