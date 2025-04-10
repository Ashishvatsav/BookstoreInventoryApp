package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddEditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        // Placeholder TextView
        val textView = TextView(this)
        textView.text = "Add/Edit Activity - Placeholder"
        setContentView(textView)
    }
}
