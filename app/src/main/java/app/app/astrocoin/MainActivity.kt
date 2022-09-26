package app.app.astrocoin
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        sharedPreferences = getSharedPreferences(this.getString(R.string.astrocoin), MODE_PRIVATE)
        Handler(Looper.getMainLooper()).postDelayed({
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