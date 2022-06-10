package mrcs.mywallet

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import mrcs.mywallet.databinding.ActivityLoginBinding
import mrcs.mywallet.domain.User

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}