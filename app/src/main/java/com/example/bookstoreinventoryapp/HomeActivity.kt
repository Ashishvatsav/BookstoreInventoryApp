package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : BaseActivity() {

    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var dbHelper: BookDatabaseHelper
    private lateinit var searchView: SearchView // or androidx.appcompat.widget.SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate activity_home into the container of activity_base
        val contentView = LayoutInflater.from(this).inflate(R.layout.activity_home, null)
        setContentView(R.layout.activity_base)
        val container = findViewById<FrameLayout>(R.id.container)
        container.removeAllViews() // Just in case
        container.addView(contentView)


        setupBottomNavigation(R.id.homeActivity)

        // Initialize views from the contentView (activity_home.xml)
        bookRecyclerView = contentView.findViewById(R.id.bookListView)
        searchView = contentView.findViewById(R.id.searchView)

        // Rest of your code...
        dbHelper = BookDatabaseHelper(this)
        dbHelper.insertSampleBooksIfNeeded()

        val books = dbHelper.getAllBooks() as MutableList<Book>
        val bookAdapter = BookAdapter(this, books)
        bookRecyclerView.layoutManager = LinearLayoutManager(this)
        bookRecyclerView.adapter = bookAdapter

        // Setup search view
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredBooks = dbHelper.searchBooks(it)
                    bookAdapter.updateBooks(filteredBooks)
                }
                return true
            }
        })
    }
}