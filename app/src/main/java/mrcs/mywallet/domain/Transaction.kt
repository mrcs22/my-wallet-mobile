package mrcs.mywallet.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigInteger

@Parcelize
data class Transaction(
    val type: String,
    val description: String,
    val value: BigInteger
) : Parcelable
