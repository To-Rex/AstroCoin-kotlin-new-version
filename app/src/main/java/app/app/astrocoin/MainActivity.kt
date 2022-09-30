package app.app.astrocoin

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var sharedPreferences: SharedPreferences? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            android.view.WindowManager.LayoutParams.FLAG_SECURE,
            android.view.WindowManager.LayoutParams.FLAG_SECURE
        )
        setContentView(R.layout.activity_main)
        checkInternet()
    }
    private fun checkInternetConnection(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                return true
            }
        }
        return false
    }
    private fun checkInternet(){
        if (!checkInternetConnection()) {
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_item)
            val btnTryAgain = dialog.findViewById<Button>(R.id.btnAlertItemY)
            btnTryAgain.setOnClickListener {
                checkInternet()
                dialog.dismiss()
            }
            dialog.show()
        }else{
            sharedPreferences = getSharedPreferences(this.getString(R.string.astrocoin), MODE_PRIVATE)
            Handler(Looper.getMainLooper()).postDelayed(
                {
                    if (sharedPreferences!!.getString("token", "") != "") {
                        startActivity(Intent(this@MainActivity, Password::class.java))
                        finish()
                    } else {
                        startActivity(Intent(this@MainActivity, Login::class.java))
                        finish()
                    }
                }, 800
            )
        }
    }
}