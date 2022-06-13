package mrcs.mywallet.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transaction(
    val type: String,
    val value: Int?
) : Parcelable
