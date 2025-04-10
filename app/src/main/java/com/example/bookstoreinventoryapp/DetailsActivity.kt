package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        // Placeholder TextView
        val textView = TextView(this)
        textView.text = "Details Activity - Placeholder"
        setContentView(textView)
    }
}
