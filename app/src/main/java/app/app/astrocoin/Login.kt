package app.app.astrocoin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Vibrator
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

    var ediloginemail: EditText? = null
    var ediloginpassword: EditText? = null
    var btnloginrecover: Button? = null
    var btnsignup: Button? = null
    var btnsignin: Button? = null
    var sharedPreferences: SharedPreferences? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_login)

        ediloginemail = findViewById(R.id.ediloginemail)
        ediloginpassword = findViewById(R.id.ediloginpassword)
        btnloginrecover = findViewById(R.id.btnloginrecover)
        btnsignup = findViewById(R.id.btnsignup)
        btnsignin = findViewById(R.id.btnsignin)
        sharedPreferences = getSharedPreferences("astrocoin", Context.MODE_PRIVATE)

        btnsignin?.setOnClickListener {
            val email = ediloginemail?.text.toString()
            val password = ediloginpassword?.text.toString()
            if (email.isEmpty()) {
                ediloginemail?.error = "Email is required"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                ediloginpassword?.error = "Password is required"
                return@setOnClickListener
            }
            if (password.length < 5) {
                ediloginpassword?.error = "Password must be at least 5 characters"
                return@setOnClickListener
            }
            if (email.length < 5) {
                ediloginemail?.error = "Email must be at least 5 characters"
                return@setOnClickListener
            }
            val loginRequest = LoginRequest()
            loginRequest.email = email
            loginRequest.password = password
            val loginResponseCall: Call<LoginRequest>? =
                ApiClient.getUserService().uerLogin(loginRequest)
            loginResponseCall?.enqueue(object : retrofit2.Callback<LoginRequest> {
                override fun onResponse(
                    call: Call<LoginRequest>,
                    response: Response<LoginRequest>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        if (loginResponse != null) {
                            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrator.vibrate(100)
                            println(response.body()!!.token)
                            val editor = sharedPreferences?.edit()
                            editor?.putString("token", response.body()!!.token)
                            editor?.apply()
                            GetUsers()

                        }
                    } else {
                        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(1000)
                        ediloginemail?.error = "Email or password is incorrect"
                        ediloginpassword?.error = "Email or password is incorrect"
                        Handler(Looper.getMainLooper()).postDelayed(
                            {
                                ediloginpassword?.text?.clear()
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

    private fun GetUsers() {
        val tokenResponceCall = ApiClient.getUserService()
            .userTokenRequest("Bearer " + sharedPreferences?.getString("token", ""))
        tokenResponceCall.enqueue(object : retrofit2.Callback<TokenRequest> {
            override fun onResponse(call: Call<TokenRequest>, response: Response<TokenRequest>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val gson = Gson()
                        val json = gson.toJson(loginResponse)
                        val editor = sharedPreferences?.edit()
                        editor?.putString("user", json)
                        Toast.makeText(this@Login, json, Toast.LENGTH_SHORT).show()
                        editor?.apply()
                        val intent = Intent(this@Login, Password::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            override fun onFailure(call: Call<TokenRequest>, t: Throwable) {
                Toast.makeText(this@Login, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}