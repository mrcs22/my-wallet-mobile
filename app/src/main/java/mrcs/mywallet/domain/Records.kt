package mrcs.mywallet.domain


data class Records(
    val transactions: List<Record>,
    val total: Int
    )
