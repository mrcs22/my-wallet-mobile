package mrcs.mywallet.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
  @SerializedName("id")
  val id: Int,

  @SerializedName("name")
  val name: String,

  @SerializedName("email")
  val email: String,

  @SerializedName("token")
  val token: String
): Parcelable

