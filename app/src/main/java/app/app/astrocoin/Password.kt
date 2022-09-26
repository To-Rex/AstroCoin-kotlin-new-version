package app.app.astrocoin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import app.app.astrocoin.models.Getdata
import com.google.gson.Gson
import java.util.concurrent.Executors

class Password : AppCompatActivity() {

    private var txtGetName: TextView? = null
    private var txtPassword: TextView? = null
    private var txtForGatPass: TextView? = null
    private var viewOne: View? = null
    private var viewTwo: View? = null
    private var viewThree: View? = null
    private var viewFour: View? = null

    private var txtPas1: TextView? = null
    private var txtPas2: TextView? = null
    private var txtPas3: TextView? = null
    private var txtPas4: TextView? = null
    private var txtPas5: TextView? = null
    private var txtPas6: TextView? = null
    private var txtPas7: TextView? = null
    private var txtPas8: TextView? = null
    private var txtPas9: TextView? = null
    private var txtPas0: TextView? = null

    private var imgFinGer: ImageView? = null
    private var imgBack: ImageView? = null
    private var sharedPreferences: SharedPreferences? = null
    private var vibrator: Vibrator? = null
    private var promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("😫")
        .setSubtitle("😩")
        .setNegativeButtonText("Cancel")
        .build()

    private var username = ""
    private var index = 0
    private var pasiIndex = false
    private var password = ""
    private var writePass = ""
    private var newPassword = ""
    private var click = true

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_password)

        txtGetName = findViewById(R.id.txtlog_getname)
        txtPassword = findViewById(R.id.txtlog_pass)
        txtForGatPass = findViewById(R.id.txtlog_forgatpass)

        viewOne = findViewById(R.id.viewlog_one)
        viewTwo = findViewById(R.id.viewlog_two)
        viewThree = findViewById(R.id.viewlog_three)
        viewFour = findViewById(R.id.viewlog_four)

        txtPas1 = findViewById(R.id.txtlog_pas1)
        txtPas2 = findViewById(R.id.txtlog_pas2)
        txtPas3 = findViewById(R.id.txtlog_pas3)
        txtPas4 = findViewById(R.id.txtlog_pas4)
        txtPas5 = findViewById(R.id.txtlog_pas5)
        txtPas6 = findViewById(R.id.txtlog_pas6)
        txtPas7 = findViewById(R.id.txtlog_pas7)
        txtPas8 = findViewById(R.id.txtlog_pas8)
        txtPas9 = findViewById(R.id.txtlog_pas9)
        txtPas0 = findViewById(R.id.txtlog_pas0)

        imgFinGer = findViewById(R.id.imglog_finger)
        imgBack = findViewById(R.id.imglog_back)

        sharedPreferences = getSharedPreferences(this.getString(R.string.astrocoin), Context.MODE_PRIVATE)
        getUserData()
        if (password == "") {
            txtPassword?.text = getString(R.string.create_password)
        } else {
            txtPassword?.text = getString(R.string.enter_password)
        }

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        val executor = Executors.newSingleThreadExecutor()
        val activity: FragmentActivity = this
        val biometricPrompt =
            BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    vibrator?.vibrate(
                        VibrationEffect.createOneShot(
                            200,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                    viewOne?.setBackgroundResource(R.drawable.passoucses)
                    viewTwo?.setBackgroundResource(R.drawable.passoucses)
                    viewThree?.setBackgroundResource(R.drawable.passoucses)
                    viewFour?.setBackgroundResource(R.drawable.passoucses)
                    Handler(Looper.getMainLooper()).postDelayed({
                        click = true
                        startActivity(Intent(applicationContext, Sample::class.java))
                        finish()
                    }, 500)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    vibrator?.vibrate(
                        VibrationEffect.createOneShot(
                            200,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                }
            })

         promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("◦😀◦")
            .setSubtitle("    ")
            .setDescription("  ")
            .setNegativeButtonText("Cancel")
            .build()


        Handler(Looper.getMainLooper()).postDelayed({
            if (password != "") {
                biometricPrompt.authenticate(promptInfo)
            }
        }, 500)
        imgFinGer?.setOnClickListener {
            if (password != "") {
                biometricPrompt.authenticate(promptInfo)
            }
        }
        txtForGatPass?.setOnClickListener {
            sharedPreferences?.edit()?.clear()?.apply()
            startActivity(Intent(this, Login::class.java))
            finish()
        }
        txtPas1?.setOnClickListener {
            if (click) {
                index++
                writePass += "1"
                checkIndex()
            }
        }
        txtPas2?.setOnClickListener {
            if (click) {
                index++
                writePass += "2"
                checkIndex()
            }
        }
        txtPas3?.setOnClickListener {
            if (click) {
                index++
                writePass += "3"
                checkIndex()
            }
        }
        txtPas4?.setOnClickListener {
            if (click) {
                index++
                writePass += "4"
                checkIndex()
            }
        }
        txtPas5?.setOnClickListener {
            if (click) {
                index++
                writePass += "5"
                checkIndex()
            }
        }
        txtPas6?.setOnClickListener {
            if (click) {
                index++
                writePass += "6"
                checkIndex()
            }
        }
        txtPas7?.setOnClickListener {
            if (click) {
                index++
                writePass += "7"
                checkIndex()
            }
        }
        txtPas8?.setOnClickListener {
            if (click) {
                index++
                writePass += "8"
                checkIndex()
            }
        }
        txtPas9?.setOnClickListener {
            if (click) {
                index++
                writePass += "9"
                checkIndex()
            }
        }
        txtPas0?.setOnClickListener {
            if (click) {
                index++
                writePass += "0"
                checkIndex()
            }
        }
        imgBack?.setOnClickListener {
            if (click) {
                index--
                checkIndexBack()
            }
        }
    }

    @SuppressLint("NewApi")
    private fun checkIndex() {
        if (index > 4) {
            index = 4
        }
        when (index) {
            1 -> {
                viewOne?.setBackgroundResource(R.drawable.pascheck)
            }
            2 -> {
                viewTwo?.setBackgroundResource(R.drawable.pascheck)
            }
            3 -> {
                viewThree?.setBackgroundResource(R.drawable.pascheck)
            }
            4 -> {
                viewFour?.setBackgroundResource(R.drawable.pascheck)
                if (password == "" && pasiIndex) {
                    if (newPassword == writePass) {
                        click = false
                        vibrator?.vibrate(
                            VibrationEffect.createOneShot(
                                100,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        writePass = ""
                        index = 0
                        viewOne?.setBackgroundResource(R.drawable.passoucses)
                        viewTwo?.setBackgroundResource(R.drawable.passoucses)
                        viewThree?.setBackgroundResource(R.drawable.passoucses)
                        viewFour?.setBackgroundResource(R.drawable.passoucses)
                        Handler(Looper.getMainLooper()).postDelayed({
                            click = true
                            sharedPreferences?.edit()?.putString("password", newPassword)?.apply()
                            startActivity(Intent(this, Sample::class.java))
                            finish()
                        }, 500)
                    } else {
                        click = false
                        vibrator?.vibrate(
                            VibrationEffect.createOneShot(
                                500,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        writePass = ""
                        index = 0
                        viewOne?.setBackgroundResource(R.drawable.passerror)
                        viewTwo?.setBackgroundResource(R.drawable.passerror)
                        viewThree?.setBackgroundResource(R.drawable.passerror)
                        viewFour?.setBackgroundResource(R.drawable.passerror)
                        Handler(Looper.getMainLooper()).postDelayed({
                            click = true
                            viewOne?.setBackgroundResource(R.drawable.passsign)
                            viewTwo?.setBackgroundResource(R.drawable.passsign)
                            viewThree?.setBackgroundResource(R.drawable.passsign)
                            viewFour?.setBackgroundResource(R.drawable.passsign)
                            txtPassword?.text = getString(R.string.repat_password)
                        }, 500)
                        Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
                    }
                }
                if (password == "" && !pasiIndex) {
                    click = false
                    vibrator?.vibrate(
                        VibrationEffect.createOneShot(
                            100,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                    newPassword = writePass
                    writePass = ""
                    index = 0
                    pasiIndex = true
                    Handler(Looper.getMainLooper()).postDelayed({
                        click = true
                        viewOne?.setBackgroundResource(R.drawable.passsign)
                        viewTwo?.setBackgroundResource(R.drawable.passsign)
                        viewThree?.setBackgroundResource(R.drawable.passsign)
                        viewFour?.setBackgroundResource(R.drawable.passsign)
                        txtPassword?.text = getString(R.string.repat_password)
                    }, 500)
                }
                if (password != "" && !pasiIndex) {
                    if (writePass == password) {
                        click = false
                        vibrator?.vibrate(
                            VibrationEffect.createOneShot(
                                100,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        writePass = ""
                        index = 0
                        viewOne?.setBackgroundResource(R.drawable.passoucses)
                        viewTwo?.setBackgroundResource(R.drawable.passoucses)
                        viewThree?.setBackgroundResource(R.drawable.passoucses)
                        viewFour?.setBackgroundResource(R.drawable.passoucses)
                        Handler(Looper.getMainLooper()).postDelayed({
                            click = true
                            startActivity(Intent(this, Sample::class.java))
                            finish()
                        }, 500)
                    } else {
                        click = false
                        writePass = ""
                        vibrator?.vibrate(
                            VibrationEffect.createOneShot(
                                500,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                        index = 0
                        viewOne?.setBackgroundResource(R.drawable.passerror)
                        viewTwo?.setBackgroundResource(R.drawable.passerror)
                        viewThree?.setBackgroundResource(R.drawable.passerror)
                        viewFour?.setBackgroundResource(R.drawable.passerror)
                        Handler(Looper.getMainLooper()).postDelayed({
                            click = true
                            viewOne?.setBackgroundResource(R.drawable.passsign)
                            viewTwo?.setBackgroundResource(R.drawable.passsign)
                            viewThree?.setBackgroundResource(R.drawable.passsign)
                            viewFour?.setBackgroundResource(R.drawable.passsign)
                        }, 500)
                        Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun checkIndexBack() {
        if (index > 4) {
            index = 4
        }
        if (index < 0) {
            index = 0
        }
        if (index > 0) {
            writePass = writePass.substring(0, writePass.length - 1)
        }
        when (index) {
            0 -> {
                viewOne?.setBackgroundResource(R.drawable.passsign)
            }
            1 -> {
                viewTwo?.setBackgroundResource(R.drawable.passsign)
            }
            2 -> {
                viewThree?.setBackgroundResource(R.drawable.passsign)
            }
            3 -> {
                viewFour?.setBackgroundResource(R.drawable.passsign)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        password = sharedPreferences?.getString("password", "")!!
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        val user = gson.fromJson(json, Getdata::class.java)
        username = user.name
        txtGetName?.text = "Hello, $username"
    }

}