package mrcs.mywallet.services.transactions

import mrcs.mywallet.domain.Transaction
import mrcs.mywallet.domain.User
import mrcs.mywallet.services.Api
import mrcs.mywallet.services.login.LoginData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

class TransactionsServices {
    fun saveTransaction(transaction: Transaction, token:String): Call<ResponseBody> {
        val retrofit = Api().config()
        val saveTransactionHandler = retrofit.create(TransactionRequest::class.java)

        return saveTransactionHandler.saveTransaction(transaction, token)
    }
}

interface TransactionRequest {
    @Headers("Content-Type: application/json")
    @POST("transactions")
    fun saveTransaction(@Body transactionData: Transaction, @Header("Authorization") authorization:String): Call<ResponseBody>
}