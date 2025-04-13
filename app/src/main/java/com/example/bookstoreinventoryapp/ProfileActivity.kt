package com.example.bookstoreinventoryapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial

class ProfileActivity : AppCompatActivity() {

    private lateinit var nameView: TextView
    private lateinit var profileNameView: TextView
    private lateinit var emailView: TextView
    private lateinit var logoutBtn: MaterialButton
    private lateinit var notificationSwitch: SwitchMaterial
    private lateinit var darkModeSwitch: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Apply activity transition animation
        try {
            overridePendingTransition(R.anim.slide_up, android.R.anim.fade_out)
        } catch (e: Exception) {
            // Fallback if animation resources are not found
        }

        // Set up toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Profile"

        // Initialize views
        nameView = findViewById(R.id.tv_name)
        profileNameView = findViewById(R.id.tv_profile_name)
        emailView = findViewById(R.id.tv_email)
        logoutBtn = findViewById(R.id.btn_logout)
        notificationSwitch = findViewById(R.id.switch_notifications)
        darkModeSwitch = findViewById(R.id.switch_dark_mode)

        // Profile image click
        findViewById<View>(R.id.profile_image).setOnClickListener {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Change profile picture feature coming soon!",
                Snackbar.LENGTH_SHORT
            ).show()
        }

        loadUserData()
        setupSwitches()
        setupLogoutButton()
    }

    private fun loadUserData() {
        val prefs = getSharedPreferences("user_auth", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId != -1) {
            val db = UserDatabaseHelper(this)
            val user = db.getUserById(userId)

            user?.let {
                profileNameView.text = it.name
                nameView.text = it.name
                emailView.text = it.email
            }
        } else {
            redirectToLogin()
        }
    }

    private fun setupSwitches() {
        val prefs = getSharedPreferences("app_settings", MODE_PRIVATE)

        try {
            val notificationsEnabled = prefs.getBoolean("notifications_enabled", true)
            val darkModeEnabled = prefs.getBoolean("dark_mode_enabled", false)

            notificationSwitch.isChecked = notificationsEnabled
            darkModeSwitch.isChecked = darkModeEnabled

            findViewById<View>(R.id.notification_setting)?.setOnClickListener {
                notificationSwitch.isChecked = !notificationSwitch.isChecked
            }

            findViewById<View>(R.id.dark_mode_setting)?.setOnClickListener {
                darkModeSwitch.isChecked = !darkModeSwitch.isChecked
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error loading preferences: ${e.message}", Toast.LENGTH_SHORT).show()
        }

        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("notifications_enabled", isChecked).apply()
            val message = if (isChecked) "Notifications enabled" else "Notifications disabled"
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
        }

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            prefs.edit().putBoolean("dark_mode_enabled", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupLogoutButton() {
        logoutBtn.setOnClickListener {
            android.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    getSharedPreferences("user_auth", MODE_PRIVATE)
                        .edit()
                        .clear()
                        .apply()

                    Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()
                    redirectToLogin()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun finish() {
        super.finish()
        try {
            overridePendingTransition(android.R.anim.fade_in, R.anim.slide_down)
        } catch (e: Exception) {
            // Fallback if animation resources are not found
        }
    }
}
