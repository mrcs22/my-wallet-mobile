package mrcs.mywallet.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import mrcs.mywallet.R
import mrcs.mywallet.databinding.ActivityAddTransactionBinding
import mrcs.mywallet.domain.Transaction
import mrcs.mywallet.services.transactions.TransactionsServices
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.math.BigInteger

class AddTransactionActivity : AppCompatActivity() {

    object Extras{
        const val TRANSACTION_TYPE = "EXTRA_TRANSACTION_TYPE"
        const val AUTH_TOKEN = "EXTRA_AUTH_TOKEN"
    }

    private lateinit var binding: ActivityAddTransactionBinding
    private lateinit var authToken: String;
    private lateinit var transactionType: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tietValue.setOnKeyListener(::maskInput)

        loadTransactionTypeFromExtra()

        binding.mbSaveTransaction.setOnClickListener{
            saveTransaction()
        }
    }

    private fun  maskInput(view: View, i: Int, keyEvent: KeyEvent):Boolean{
        val valueHolder = binding.tietValue

        val alowedCharacteres = "0123456789"
        val typedCharacter = keyEvent.number.toString()

        val isAllowedCharacter = (alowedCharacteres.contains(typedCharacter))
        val isDeleteKey = (i == 67)

        if(isAllowedCharacter || isDeleteKey){
            if(keyEvent.action == 1) return true

            val formatValueAction = if(i == 67) "delete" else "add"
            val formattedValue = formatValue(valueHolder.text.toString(), formatValueAction, typedCharacter)

            valueHolder.setText(formattedValue)
            valueHolder.setSelection(valueHolder.length())

            return true
        }

        return false
    }

    private fun formatValue(lastValue: String, action: String, newCharacter: String): String {
        val lastCentsValue = lastValue.replace(",","").toBigInteger().toString()

        var currentCentsValue = "$lastCentsValue$newCharacter"
        if(action == "delete"){
            currentCentsValue = if(lastCentsValue.length == 1) "000"
            else lastCentsValue.substring(0, lastCentsValue.length - 1);

            if(currentCentsValue.length < 2){
                currentCentsValue = "0$currentCentsValue"
            }
        }

        if(currentCentsValue.length < 3){
            currentCentsValue = "0$currentCentsValue"
        }

        val decimalIndex = currentCentsValue.length - 2
        val currentValue = StringBuilder(currentCentsValue).insert(decimalIndex, ",")

        return currentValue.toString()
    }

    private fun saveTransaction(){
        binding.pbLoading.visibility = View.VISIBLE

        val transaction = Transaction(transactionType,binding.tietDescription.text.toString(), getTransactionCentsValue())

        val isTransactionDataValid = validateTransactionData(transaction)
        if(!isTransactionDataValid) {
            return
        }

        TransactionsServices().saveTransaction(transaction, "Bearer $authToken").enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {

                if(response?.isSuccessful == false){
                    showMessage(getString(R.string.erro_inesperado))
                    return
                }

                resetAddTransactionScreen()
                switchToRecords()
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                showMessage(getString(R.string.erro_inesperado))
            }
        })
    }

    private fun validateTransactionData(transaction: Transaction): Boolean {
        var result = true
        if(transaction.value <= "0".toBigInteger()){
            showMessage(getString(R.string.valor_transacao_invalido))
            result = false
        }

        if(transaction.description.isBlank()){
            showMessage(getString(R.string.descricao_transacao_invalida))
            result = false
        }

        return result
    }

    private fun getTransactionCentsValue(): BigInteger {
        val transactionValue = binding.tietValue.text.toString().replace(",", "")

        return transactionValue.toBigInteger()
    }

    private fun resetAddTransactionScreen(){
        binding.tietValue.setText("")
        binding.tietDescription.setText("")

        binding.pbLoading.visibility = View.GONE
    }

    private fun switchToRecords(){
     this.onBackPressed()
    }

    private fun showMessage(message: String){
        binding.pbLoading.visibility = View.GONE
        Snackbar.make(binding.mbSaveTransaction, message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.RED)
            .show()
    }

    private fun loadTransactionTypeFromExtra() {
        transactionType =  intent.getStringExtra(AddTransactionActivity.Extras.TRANSACTION_TYPE).toString()
        updateScreenTitle(transactionType)

        authToken = intent.getStringExtra(AddTransactionActivity.Extras.AUTH_TOKEN).toString()
    }

    private fun updateScreenTitle(transactionType: String){
        var title = getString(R.string.nova_saida)

        if (transactionType == "in") {
            title = getString(R.string.nova_entrada)
        }

        binding.tvTitle.text = title
    }
}