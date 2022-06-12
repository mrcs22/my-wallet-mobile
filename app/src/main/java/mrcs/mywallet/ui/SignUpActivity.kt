package mrcs.mywallet.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import mrcs.mywallet.R
import mrcs.mywallet.databinding.ActivitySignUpBinding
import mrcs.mywallet.services.signUp.SignUpData
import mrcs.mywallet.services.signUp.SignUpServices
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import  com.google.android.material.textfield.TextInputEditText as TextInput

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var nameHolder: TextInput
    private lateinit var emailHolder: TextInput
    private lateinit var passwordHolder: TextInput
    private lateinit var confirmPasswordHolder: TextInput

    private lateinit var loading:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nameHolder = binding.tietName
        emailHolder = binding.tietEmail
        passwordHolder = binding.tietPassword
        confirmPasswordHolder = binding.tietConfirmPassword

        loading = binding.pbLoading

        val signUpButton = binding.mbSignUp
        signUpButton.setOnClickListener{
            signUp()
        }

        val loginButton = binding.tvGoToLogin
        loginButton.setOnClickListener {
            switchToLogin()
        }
    }

    private fun switchToLogin() {
      this.onBackPressed()
    }

    private fun signUp(){
        loading.visibility = View.VISIBLE
        val userData : SignUpData = getUserData()

        val isUserDataValid = validateUserData(userData)
        if(!isUserDataValid) {
            return
        }

        SignUpServices().signUp(userData).enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(call: Call<ResponseBody?>?, response: Response<ResponseBody?>?) {

                if(response?.isSuccessful == true){
                    resetSignUp()
                    switchToLogin()
                    return
                }

                if(response?.code() == 409){
                    showMessage(getString(R.string.conflito_cadastro))
                }else{
                    showMessage(getString(R.string.erro_inesperado))
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                showMessage(getString(R.string.erro_inesperado))
            }
        })
    }

    private fun getUserData(): SignUpData {
        val name = nameHolder.text.toString()
        val email = emailHolder.text.toString()
        val passwod = passwordHolder.text.toString()

        return SignUpData(name,email,passwod)
    }

    private fun  validateUserData(userData: SignUpData): Boolean {
       if(TextUtils.isEmpty(userData.name)){
           showMessage(getString(R.string.sem_nome))
           return false
       }

        if(TextUtils.isEmpty(userData.email)){
            showMessage(getString(R.string.sem_email))
            return false
        }

        val isEmailInvalid = !(android.util.Patterns.EMAIL_ADDRESS.matcher(userData.email).matches())
        if(isEmailInvalid){
            showMessage(getString(R.string.email_invalido))
            return false
        }

        if(TextUtils.isEmpty(userData.password)){
            showMessage(getString(R.string.sem_senha))
            return false
        }

        val passwordConfirmation = confirmPasswordHolder.text.toString()
        if(passwordConfirmation != userData.password){
            showMessage(getString(R.string.senhas_diferentes))
            return false
        }

        return true
    }

    private fun showMessage(message: String){
        loading.visibility = View.GONE
        Snackbar.make(binding.mbSignUp, message, Snackbar.LENGTH_LONG)
            .setTextColor(Color.RED)
            .show()
    }

    private fun resetSignUp(){
        loading.visibility = View.GONE

        nameHolder.setText("")
        emailHolder.setText("")
        passwordHolder.setText("")
        confirmPasswordHolder.setText("")
    }
}