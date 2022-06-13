package mrcs.mywallet.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import mrcs.mywallet.R
import mrcs.mywallet.databinding.ActivityRecordsBinding
import mrcs.mywallet.domain.Records
import mrcs.mywallet.domain.Transaction
import mrcs.mywallet.domain.User
import mrcs.mywallet.services.records.RecordsServices
import mrcs.mywallet.ui.adapter.RecordsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecordsActivity : AppCompatActivity() {

    object Extras{
        const val USER = "EXTRA_USER"
    }

    private lateinit var binding: ActivityRecordsBinding
    private lateinit var grettingsHolder: TextView;
    private lateinit var authToken: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordsBinding.inflate(layoutInflater)

        grettingsHolder = binding.tvGreetings

        binding.mbAddExpense.setOnClickListener{
            switchToAddTransaction(Transaction("expense", null))
        }

        binding.mbAddIncome.setOnClickListener{
            switchToAddTransaction(Transaction("income",null))
        }

        setContentView(binding.root)

        loadUserFromExtra()
    }

    private fun switchToAddTransaction(transactionType: Transaction){
        val switchActivityIntent = Intent(this, AddTransactionActivity::class.java)
        switchActivityIntent.putExtra(AddTransactionActivity.Extras.TRANSACTION_TYPE, transactionType)
        startActivity(switchActivityIntent)
    }

    private fun loadUserFromExtra(){
        intent?.extras?.getParcelable<User>(Extras.USER).let{
            if(it == null){
                this.onBackPressed()
            }

            grettingsHolder.text = "Olá, ${it?.name}"
            authToken= it?.token.toString()

            getRecords()
        }
    }

    private fun getRecords(){
        binding.rcRecords.setHasFixedSize(true)
        binding.rcRecords.layoutManager = LinearLayoutManager(this)

        RecordsServices().getRecords(authToken).enqueue(object : Callback<Records?> {
            override fun onResponse(call: Call<Records?>?, response: Response<Records?>?) {
                val body = response?.body()

                if(response?.isSuccessful == false){
                    showMessage(getString(R.string.erro_inesperado))
                    return
                }

                binding.rcRecords.adapter = body?.transactions?.let {
                    RecordsAdapter(it)
                }

                val totalValue = body?.total ?: 0
                if(totalValue >= 0){
                    binding.tvTotalValue.setTextColor(Color.parseColor("#009900"))
                }

                val parsedTotalValue = String.format("%.2f",totalValue.toFloat()/100)
                binding.tvTotalValue.text = parsedTotalValue.replace("-","")

                binding.pbLoading.visibility = View.GONE
                if(body?.transactions?.isEmpty() == true){
                    binding.tvNoTransactions.visibility=View.VISIBLE
                }
            }

            override fun onFailure(call: Call<Records?>, t: Throwable) {
                showMessage(getString(R.string.erro_inesperado))
            }
        })
    }

    private fun showMessage(message: String){
      binding.pbLoading.visibility = View.GONE
        Snackbar.make(binding.mbAddExpense, message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.RED)
            .show()
    }

}