package com.example.bookstoreinventoryapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : AppCompatActivity() {

    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var dbHelper: BookDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

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
