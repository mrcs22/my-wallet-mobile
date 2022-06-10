package mrcs.mywallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mrcs.mywallet.databinding.ActivitySignUpBinding
import mrcs.mywallet.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.tvGoToLogin
        loginButton.setOnClickListener {
            switchActivities()
        }

    }

    private fun switchActivities() {
        val switchActivityIntent = Intent(this, LoginActivity::class.java)
        startActivity(switchActivityIntent)
    }
}