package mrcs.mywallet.ui

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import mrcs.mywallet.R
import mrcs.mywallet.databinding.ActivityLoginBinding
import mrcs.mywallet.domain.User
import mrcs.mywallet.services.login.LoginData
import mrcs.mywallet.services.login.LoginServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loggedUser: User

    private lateinit var loading: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loading = binding.pbLoading

        binding.mbLogin.setOnClickListener{
            login()
        }

        val signUpButton = binding.tvGoToSignUp
        signUpButton.setOnClickListener {
        switchToSignUp()
        }
    }

    private fun switchToSignUp() {
        val switchActivityIntent = Intent(this, SignUpActivity::class.java)
        startActivity(switchActivityIntent)
    }

    private fun switchToRecords() {
        val switchActivityIntent = Intent(this, RecordsActivity::class.java)
        switchActivityIntent.putExtra(RecordsActivity.Extras.USER, loggedUser)
        startActivity(switchActivityIntent)
    }

    private fun login(){
        loading.visibility = View.VISIBLE
        val loginData : LoginData = getLoginData()

        val isUserDataValid = validateLoginData(loginData)
        if(!isUserDataValid) {
            return
        }

        LoginServices().login(loginData).enqueue(object : Callback<User?> {
            override fun onResponse(call: Call<User?>?, response: Response<User?>?) {

                if(response?.isSuccessful == true){
                    loggedUser = response.body()!!
                    resetLogin()
                    switchToRecords()
                    return
                }

                if(response?.code() == 400){
                    showMessage(getString(R.string.dados_incorretos))
                }else{
                    showMessage(getString(R.string.erro_inesperado))
                }
            }

            override fun onFailure(call: Call<User?>, t: Throwable) {
                showMessage(getString(R.string.erro_inesperado))
            }
        })
    }

    private fun getLoginData(): LoginData {
        val email = binding.tietEmail.text.toString()
        val password = binding.tietPassword.text.toString()

        return LoginData(email, password)
    }

    private fun  validateLoginData(loginData: LoginData): Boolean {
        val isEmailInvalid = !(android.util.Patterns.EMAIL_ADDRESS.matcher(loginData.email).matches())
        if(isEmailInvalid){
            showMessage(getString(R.string.email_invalido))
            return false
        }

        if(TextUtils.isEmpty(loginData.password)) {
            showMessage(getString(R.string.sem_senha))
            return false
        }

        return true
    }

    private fun showMessage(message: String){
        loading.visibility = View.GONE
        Snackbar.make(binding.mbLogin, message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.RED)
            .show()
    }

    private fun resetLogin(){
        loading.visibility = View.GONE

        binding.tietEmail.setText("")
        binding.tietPassword.setText("")
    }
}


