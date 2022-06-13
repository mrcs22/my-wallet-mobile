package mrcs.mywallet.ui

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

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private lateinit var loading:ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
        val name = binding.tietName.text.toString()
        val email = binding.tietEmail.text.toString()
        val passwod = binding.tietPassword.text.toString()

        return SignUpData(name,email,passwod)
    }

    private fun  validateUserData(userData: SignUpData): Boolean {
        if(TextUtils.isEmpty(userData.name)){
           showMessage(getString(R.string.sem_nome))
           return false
        }

        val isEmailInvalid = !(android.util.Patterns.EMAIL_ADDRESS.matcher(userData.email).matches())
        if(isEmailInvalid){
            showMessage(getString(R.string.email_invalido))
            return false
        }


        val isPasswordBlank = userData.password.isBlank()
        val doesPasswordMatchToItsConfirm = (binding.tietConfirmPassword.text.toString() == userData.password)
        if(isPasswordBlank || !doesPasswordMatchToItsConfirm ){
            showMessage(getString(R.string.senha_diferente_ou_vazia))
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

        binding.tietName.setText("")
        binding.tietEmail.setText("")
        binding.tietPassword.setText("")
        binding.tietConfirmPassword.setText("")
    }
}