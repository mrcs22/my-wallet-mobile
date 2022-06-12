package mrcs.mywallet.domain


data class Record(
    val id: Int,
    val user_id: Int,
    val description:String,
    val value: Int,
    val date:String,
    val type: String
)
