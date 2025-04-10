package com.example.bookstoreinventoryapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)

        val prefs = getSharedPreferences("user_auth", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        val nameView = findViewById<TextView>(R.id.tv_name)
        val emailView = findViewById<TextView>(R.id.tv_email)
        val logoutBtn = findViewById<Button>(R.id.btn_logout)

        if (userId != -1) {
            val db = UserDatabaseHelper(this)
            val user = db.getUserById(userId)
            user?.let {
                nameView.text = "Name: ${it.name}"
                emailView.text = "Email: ${it.email}"
            }
        }

        logoutBtn.setOnClickListener {
            prefs.edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
