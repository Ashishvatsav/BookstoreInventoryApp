package com.example.bookstoreinventoryapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check login status first
        val prefs = getSharedPreferences("user_auth", MODE_PRIVATE)
        if (prefs.getInt("user_id", -1) == -1) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_main)

        // Enable edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = true
        controller.isAppearanceLightNavigationBars = true
        controller.show(WindowInsetsCompat.Type.systemBars())

        // Bottom Navigation setup
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeActivity -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.addEditActivity -> {
                    startActivity(Intent(this, AddEditActivity::class.java))
                    true
                }
                R.id.detailsActivity -> {
                    startActivity(Intent(this, DetailsActivity::class.java))
                    true
                }
                R.id.profileActivity -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.vendorsActivity -> {
                    startActivity(Intent(this, VendorsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
