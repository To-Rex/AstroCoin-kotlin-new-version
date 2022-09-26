package app.app.astrocoin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.*
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import app.app.astrocoin.models.LoginRequest
import app.app.astrocoin.models.TokenRequest
import app.app.astrocoin.sampleclass.ApiClient
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response

class Login : AppCompatActivity() {

    private var ediLoginEmail: EditText? = null
    private var ediLoginPassword: EditText? = null
    private var btnLoginRecover: Button? = null
    private var btnSignup: Button? = null
    private var btnSignIn: Button? = null
    private var vibrator: Vibrator? = null
    private var sharedPreferences: SharedPreferences? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_login)

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
        ediLoginEmail = findViewById(R.id.ediloginemail)
        ediLoginPassword = findViewById(R.id.ediloginpassword)
        btnLoginRecover = findViewById(R.id.btnloginrecover)
        btnSignup = findViewById(R.id.btnsignup)
        btnSignIn = findViewById(R.id.btnsignin)
        sharedPreferences = getSharedPreferences(this.getString(R.string.astrocoin), Context.MODE_PRIVATE)

        btnSignIn?.setOnClickListener {
            val email = ediLoginEmail?.text.toString()
            val password = ediLoginPassword?.text.toString()
            if (email.isEmpty()) {
                ediLoginEmail?.error = "Email is required"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                ediLoginPassword?.error = "Password is required"
                return@setOnClickListener
            }
            if (password.length < 5) {
                ediLoginPassword?.error = "Password must be at least 5 characters"
                return@setOnClickListener
            }
            if (email.length < 5) {
                ediLoginEmail?.error = "Email must be at least 5 characters"
                return@setOnClickListener
            }
            val loginRequest = LoginRequest()
            loginRequest.email = email
            loginRequest.password = password
            val loginResponseCall: Call<LoginRequest>? =
                ApiClient.userService.uerLogin(loginRequest)
            loginResponseCall?.enqueue(object : retrofit2.Callback<LoginRequest> {
                @SuppressLint("NewApi")
                override fun onResponse(
                    call: Call<LoginRequest>,
                    response: Response<LoginRequest>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            vibrator?.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
                            println(response.body()!!.token)
                            val editor = sharedPreferences?.edit()
                            editor?.putString("token", response.body()!!.token)
                            editor?.apply()
                            getUsers()

                        }
                    } else {
                        vibrator?.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE))
                        ediLoginEmail?.error = "Email or password is incorrect"
                        ediLoginPassword?.error = "Email or password is incorrect"
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                ediLoginPassword?.text?.clear()
                            }, 800
                        )
                    }
                }

                override fun onFailure(call: Call<LoginRequest>, t: Throwable) {
                    Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun getUsers() {
        val tokenRespOnceCall = ApiClient.userService
            .userTokenRequest("Bearer " + sharedPreferences?.getString("token", ""))
        tokenRespOnceCall.enqueue(object : retrofit2.Callback<TokenRequest> {
            override fun onResponse(call: Call<TokenRequest>, response: Response<TokenRequest>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val gson = Gson()
                        val json = gson.toJson(loginResponse)
                        val editor = sharedPreferences?.edit()
                        editor?.putString("user", json)
                        editor?.apply()
                        startActivity(Intent(this@Login, Password::class.java))
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<TokenRequest>, t: Throwable) {
                Toast.makeText(this@Login, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }
}