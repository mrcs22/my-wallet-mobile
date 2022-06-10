package mrcs.mywallet

import android.content.Intent
import android.os.Bundle
import android.view.View
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
           switchActivities()
        }

    }

    private fun switchActivities() {
        val switchActivityIntent = Intent(this, SignUpActivity::class.java)
        startActivity(switchActivityIntent)
    }


}