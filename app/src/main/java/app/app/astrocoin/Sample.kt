package app.app.astrocoin

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class Sample : AppCompatActivity() {
    var resume = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.sample)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment_sample)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_home,
            R.id.navigation_dashboard, R.id.navigation_notifications))

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStop() {
        resume = false
        Handler(Looper.getMainLooper()).postDelayed(
            {
                finish()
            }, 15000
        )
        super.onStop()
    }

    override fun onStart() {
        resume = true
        super.onStart()
    }

}