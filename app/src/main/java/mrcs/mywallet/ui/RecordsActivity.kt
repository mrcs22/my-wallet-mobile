package mrcs.mywallet.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import mrcs.mywallet.databinding.ActivityRecordsBinding
import mrcs.mywallet.domain.User

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

        setContentView(binding.root)

        loadUserFromExtra()
    }

    private fun loadUserFromExtra(){
        intent?.extras?.getParcelable<User>(Extras.USER).let{
            if(it == null){
                this.onBackPressed()
            }

            grettingsHolder.text = "Olá, ${it?.name}"
            authToken= it?.token.toString()
        }
    }
}