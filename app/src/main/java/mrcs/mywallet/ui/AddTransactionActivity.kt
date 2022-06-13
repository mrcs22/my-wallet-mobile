package mrcs.mywallet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        loadTransactionTypeFromExtra()

        setContentView(binding.root)
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
}