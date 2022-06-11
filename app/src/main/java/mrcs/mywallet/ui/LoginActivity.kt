package mrcs.mywallet.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mrcs.mywallet.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val signUpButton = binding.tvGoToSignUp
        signUpButton.setOnClickListener {
        switchToSignUp()
        }
    }

    private fun switchToSignUp() {
        val switchActivityIntent = Intent(this, SignUpActivity::class.java)
        startActivity(switchActivityIntent)
    }






}


