package mrcs.mywallet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import mrcs.mywallet.databinding.ActivityAddTransactionBinding
import mrcs.mywallet.domain.Transaction

class AddTransactionActivity : AppCompatActivity() {

    object Extras{
        const val TRANSACTION_TYPE = "EXTRA_TRANSACTION_TYPE"
    }

    private lateinit var binding: ActivityAddTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tietValue.setOnKeyListener(::maskInput)

        loadTransactionTypeFromExtra()
    }

    private fun loadTransactionTypeFromExtra(){
        intent?.extras?.getParcelable<Transaction>(AddTransactionActivity.Extras.TRANSACTION_TYPE).let{

            var transactionTypeTitle = "Nova Saída"
            if(it?.type == "income"){
                transactionTypeTitle = "Nova Entrada"
            }

            binding.tvTitle.text = transactionTypeTitle
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
}