package app.app.astrocoin

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.app.astrocoin.models.Getdata
import app.app.astrocoin.models.LoginRequest
import app.app.astrocoin.models.TokenRequest
import com.google.gson.Gson

open class Password : AppCompatActivity() {

    private var txtgetname: TextView? = null
    private var txtpassword: TextView? = null
    private var viewone: View? = null
    private var viewtwo: View? = null
    private var viewthree: View? = null
    private var viewfour: View? = null

    private var txtpas1: TextView? = null
    private var txtpas2: TextView? = null
    private var txtpas3: TextView? = null
    private var txtpas4: TextView? = null
    private var txtpas5: TextView? = null
    private var txtpas6: TextView? = null
    private var txtpas7: TextView? = null
    private var txtpas8: TextView? = null
    private var txtpas9: TextView? = null
    private var txtpas0: TextView? = null

    private var imgfinger: ImageView? = null
    private var imgback: ImageView? = null
    var sharedPreferences: SharedPreferences? = null

    var username = ""
    var index = 0
    var pasindex = false
    var password = ""
    var writepass = ""
    var newpassword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_password)

        txtgetname = findViewById(R.id.txtlog_getname)
        txtpassword = findViewById(R.id.txtlog_pass)

        viewone = findViewById(R.id.viewlog_one)
        viewtwo = findViewById(R.id.viewlog_two)
        viewthree = findViewById(R.id.viewlog_three)
        viewfour = findViewById(R.id.viewlog_four)

        txtpas1 = findViewById(R.id.txtlog_pas1)
        txtpas2 = findViewById(R.id.txtlog_pas2)
        txtpas3 = findViewById(R.id.txtlog_pas3)
        txtpas4 = findViewById(R.id.txtlog_pas4)
        txtpas5 = findViewById(R.id.txtlog_pas5)
        txtpas6 = findViewById(R.id.txtlog_pas6)
        txtpas7 = findViewById(R.id.txtlog_pas7)
        txtpas8 = findViewById(R.id.txtlog_pas8)
        txtpas9 = findViewById(R.id.txtlog_pas9)
        txtpas0 = findViewById(R.id.txtlog_pas0)

        imgfinger = findViewById(R.id.imglog_finger)
        imgback = findViewById(R.id.imglog_back)


        sharedPreferences = getSharedPreferences("astrocoin", Context.MODE_PRIVATE)

        if (password == "") {
            txtpassword?.text = getString(R.string.create_password)
        } else {
            txtpassword?.text = getString(R.string.enter_password)
        }

        getUserData()
        txtpas1?.setOnClickListener {
            index++
            writepass += "1"
            checkindex()
        }
        txtpas2?.setOnClickListener {
            index++
            writepass += "2"
            checkindex()
        }
        txtpas3?.setOnClickListener {
            index++
            writepass += "3"
            checkindex()
        }
        txtpas4?.setOnClickListener {
            index++
            writepass += "4"
            checkindex()
        }
        txtpas5?.setOnClickListener {
            index++
            writepass += "5"
            checkindex()
        }
        txtpas6?.setOnClickListener {
            index++
            writepass += "6"
            checkindex()
        }
        txtpas7?.setOnClickListener {
            index++
            writepass += "7"
            checkindex()
        }
        txtpas8?.setOnClickListener {
            index++
            writepass += "8"
            checkindex()
        }
        txtpas9?.setOnClickListener {
            index++
            writepass += "9"
            checkindex()
        }
        txtpas0?.setOnClickListener {
            index++
            writepass += "0"
            checkindex()
        }
        imgback?.setOnClickListener {
            index--
            checkindexback()
        }
    }

    private fun checkindex() {
        if (index > 4) {
            index = 4
        }
        when (index) {
            1 -> {
                viewone?.setBackgroundResource(R.drawable.pascheck)
            }
            2 -> {
                viewtwo?.setBackgroundResource(R.drawable.pascheck)
            }
            3 -> {
                viewthree?.setBackgroundResource(R.drawable.pascheck)
            }
            4 -> {
                viewfour?.setBackgroundResource(R.drawable.pascheck)
                if (password == "" && pasindex) {
                    if (newpassword == writepass) {
                        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(100)
                        writepass = ""
                        index = 0
                        viewone?.setBackgroundResource(R.drawable.passoucses)
                        viewtwo?.setBackgroundResource(R.drawable.passoucses)
                        viewthree?.setBackgroundResource(R.drawable.passoucses)
                        viewfour?.setBackgroundResource(R.drawable.passoucses)
                        Handler().postDelayed({
                            sharedPreferences?.edit()?.putString("password", newpassword)?.apply()
                            val intent = Intent(this, Sample::class.java)
                            startActivity(intent)
                            finish()
                        }, 500)
                        Toast.makeText(this, "Password created", Toast.LENGTH_SHORT).show()
                    } else {
                        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(700)
                        writepass = ""
                        index = 0
                        viewone?.setBackgroundResource(R.drawable.passerror)
                        viewtwo?.setBackgroundResource(R.drawable.passerror)
                        viewthree?.setBackgroundResource(R.drawable.passerror)
                        viewfour?.setBackgroundResource(R.drawable.passerror)
                        Handler().postDelayed({
                            viewone?.setBackgroundResource(R.drawable.passsign)
                            viewtwo?.setBackgroundResource(R.drawable.passsign)
                            viewthree?.setBackgroundResource(R.drawable.passsign)
                            viewfour?.setBackgroundResource(R.drawable.passsign)
                            txtpassword?.text = getString(R.string.repat_password)
                            val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            vibrator.vibrate(100)
                        }, 500)
                        Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
                    }
                }
                if (password == "" && !pasindex) {
                    newpassword = writepass
                    writepass = ""
                    index = 0
                    pasindex = true
                    Handler().postDelayed({
                        viewone?.setBackgroundResource(R.drawable.passsign)
                        viewtwo?.setBackgroundResource(R.drawable.passsign)
                        viewthree?.setBackgroundResource(R.drawable.passsign)
                        viewfour?.setBackgroundResource(R.drawable.passsign)
                        txtpassword?.text = getString(R.string.repat_password)
                        val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        vibrator.vibrate(100)
                    }, 500)
                }
                /*if(password!="" && writepass == password){
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)
                    writepass = ""
                    index = 0
                }else{
                    val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(100)
                    writepass = ""
                    index = 0
                    Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show()
                }*/
            }
        }
    }

    private fun checkindexback() {
        if (index > 4) {
            index = 4
        }
        if (index < 0) {
            index = 0
        }
        if (index > 0) {
            writepass = writepass.substring(0, writepass.length - 1)
        }
        when (index) {
            0 -> {
                viewone?.setBackgroundResource(R.drawable.passsign)
            }
            1 -> {
                viewtwo?.setBackgroundResource(R.drawable.passsign)
            }
            2 -> {
                viewthree?.setBackgroundResource(R.drawable.passsign)
            }
            3 -> {
                viewfour?.setBackgroundResource(R.drawable.passsign)
            }
        }


    }

    @SuppressLint("SetTextI18n")
    private fun getUserData() {
        password = sharedPreferences?.getString("password", "")!!
        Toast.makeText(this, password, Toast.LENGTH_SHORT).show()
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        val user = gson.fromJson(json, Getdata::class.java)
        username = user.name
        txtgetname?.text = "Hello, $username"
    }
}