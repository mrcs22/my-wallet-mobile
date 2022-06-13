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
    private lateinit var authToken: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRecordsBinding.inflate(layoutInflater)

        binding.ibSingOut.setOnClickListener{
            this.onBackPressed()
        }

        binding.mbAddExpense.setOnClickListener{
            switchToAddTransaction("out")
        }

        binding.mbAddIncome.setOnClickListener{
            switchToAddTransaction("in")
        }

        setContentView(binding.root)

        loadUserFromExtra()
    }

    override fun onResume() {
        super.onResume()

        binding.tvNoTransactions.visibility=View.GONE
        binding.pbLoading.visibility = View.VISIBLE

        getRecords()
    }

    private fun switchToAddTransaction(transactionType: String){
        val switchActivityIntent = Intent(this, AddTransactionActivity::class.java)

        switchActivityIntent.putExtra(AddTransactionActivity.Extras.TRANSACTION_TYPE, transactionType)
        switchActivityIntent.putExtra(AddTransactionActivity.Extras.AUTH_TOKEN, authToken)

        startActivity(switchActivityIntent)
    }

    private fun loadUserFromExtra(){
        intent?.extras?.getParcelable<User>(Extras.USER).let{
            if(it == null){
                this.onBackPressed()
            }

            binding.tvGreetings.text = "${getString(R.string.ola)}, ${it?.name}"
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
                val totalValueColor = if(totalValue >= 0) "#009900" else "#f70000"
                binding.tvTotalValue.setTextColor(Color.parseColor(totalValueColor))

                val parsedTotalValue = String.format("%.2f",totalValue.toFloat()/100)
                binding.tvTotalValue.text = parsedTotalValue.replace("-","")

                binding.pbLoading.visibility = View.GONE

                if(body?.transactions?.isEmpty() == true) {
                    binding.tvNoTransactions.visibility = View.VISIBLE
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