package app.app.astrocoin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Password : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_password)

    }
}