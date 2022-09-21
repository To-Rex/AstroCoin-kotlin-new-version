package app.app.astrocoin

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    var password = ""
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

        getUserData()
        txtpas1?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas2?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas3?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas4?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas5?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas6?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas7?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas8?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas9?.setOnClickListener {
            index++
            checkindex()
        }
        txtpas0?.setOnClickListener {
            index++
            checkindex()
        }
        imgback?.setOnClickListener {
            index--
            checkindexback()
        }
    }

    private fun checkindex() {
        if(index > 4){
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

            }
        }
    }

    private fun checkindexback() {
        if(index>4){
            index = 4
        }
        if (index < 0) {
            index = 0
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
    private fun getUserData(){
        val gson = Gson()
        val json = sharedPreferences?.getString("user", "")
        println(json)
        val user = gson.fromJson(json, Getdata::class.java)
        username = user.name
        Toast.makeText(this, username, Toast.LENGTH_SHORT).show()
        txtgetname?.text = "Hello, $username"
    }
}