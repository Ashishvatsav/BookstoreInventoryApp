package com.example.bookstoreinventoryapp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val prefs = getSharedPreferences("user_auth", MODE_PRIVATE)
        val userId = prefs.getInt("user_id", -1)

        if (userId != -1) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
            return
        }

        val emailInput = findViewById<EditText>(R.id.et_email)
        val passwordInput = findViewById<EditText>(R.id.et_password)
        val loginBtn = findViewById<Button>(R.id.btn_login)
        val signupLink = findViewById<TextView>(R.id.tv_go_to_signup)

        val db = UserDatabaseHelper(this)

        loginBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Invalid email format", Toast.LENGTH_SHORT).show()
            } else if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            } else {
                val id = db.validateUser(email, password)
                if (id != null) {
                    prefs.edit().putInt("user_id", id).apply()
                    startActivity(Intent(this, HomeActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Invalid credentials!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        signupLink.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
