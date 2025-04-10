package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nameInput = findViewById<EditText>(R.id.et_name)
        val emailInput = findViewById<EditText>(R.id.et_email)
        val passwordInput = findViewById<EditText>(R.id.et_password)
        val signupBtn = findViewById<Button>(R.id.btn_signup)

        val db = UserDatabaseHelper(this)

        signupBtn.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                val success = db.insertUser(name, email, password)
                if (success) {
                    Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "User already exists!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
