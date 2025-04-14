package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : BaseActivity() {

    private lateinit var bookRecyclerView: RecyclerView
    private lateinit var dbHelper: BookDatabaseHelper
    private lateinit var searchView: SearchView
    private lateinit var bookAdapter: BookAdapter
    private var allBooks: MutableList<Book> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val contentView = LayoutInflater.from(this).inflate(R.layout.activity_home, null)
        setContentView(R.layout.activity_base)
        val container = findViewById<FrameLayout>(R.id.container)
        container.removeAllViews()
        container.addView(contentView)

        setupBottomNavigation(R.id.homeActivity)

        bookRecyclerView = contentView.findViewById(R.id.bookListView)
        searchView = contentView.findViewById(R.id.searchView)

        dbHelper = BookDatabaseHelper(this)
        dbHelper.insertSampleBooksIfNeeded()

        bookRecyclerView.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(this, allBooks, dbHelper)
        bookRecyclerView.adapter = bookAdapter

        // Set up the search functionality
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filter books based on the query
                newText?.let {
                    // Call a function to filter books by title
                    val filteredBooks = dbHelper.searchBooksByTitle(it)
                    bookAdapter.updateBooks(filteredBooks)
                }
                return true
            }
        })
    }

    override fun onResume() {
        super.onResume()
        allBooks = dbHelper.getAllBooks().toMutableList()
        bookAdapter.updateBooks(allBooks)
    }
}
