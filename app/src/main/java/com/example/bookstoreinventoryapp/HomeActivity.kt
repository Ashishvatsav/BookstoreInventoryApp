package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : BaseActivity() {

    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var dbHelper: BookDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the specific layout and attach it to the base layout's container
        val contentView = LayoutInflater.from(this).inflate(R.layout.activity_home, null)
        setContentView(R.layout.activity_base)
        findViewById<android.widget.FrameLayout>(R.id.container).addView(contentView)

        setupBottomNavigation(R.id.homeActivity)

        // Initialize RecyclerView and Database Helper
        bookRecyclerView = findViewById(R.id.bookListView)
        dbHelper = BookDatabaseHelper(this)

        // Insert sample data if needed
        dbHelper.insertSampleBooksIfNeeded()

        // Fetch books from the database
        val books = dbHelper.getAllBooks()

        // Set up RecyclerView with an adapter
        val bookAdapter = BookAdapter(this, books)
        bookRecyclerView.layoutManager = LinearLayoutManager(this)
        bookRecyclerView.adapter = bookAdapter
    }
}
