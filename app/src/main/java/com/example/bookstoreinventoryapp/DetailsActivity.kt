package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: BookDatabaseHelper
    private lateinit var bookSearchDropdown: AutoCompleteTextView
    private lateinit var showDetailsButton: Button
    private lateinit var bookDetailsLayout: LinearLayout
    private lateinit var detailsText: TextView
    private lateinit var detailsCoverImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbHelper = BookDatabaseHelper(this)

        bookSearchDropdown = findViewById(R.id.bookSearchDropdown)
        showDetailsButton = findViewById(R.id.btn_show_details)
        bookDetailsLayout = findViewById(R.id.bookDetailsLayout)
        detailsText = findViewById(R.id.detailsText)
        detailsCoverImage = findViewById(R.id.detailsCoverImage)

        // Fetch all titles from the DB
        val titles = dbHelper.getAllBookTitles()

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, titles)
        bookSearchDropdown.setAdapter(adapter)

        showDetailsButton.setOnClickListener {
            val selectedTitle = bookSearchDropdown.text.toString()
            val book = dbHelper.getBookDetailsByTitle(selectedTitle)

            if (book != null) {
                bookDetailsLayout.visibility = View.VISIBLE
                detailsText.text = """
                    Title: ${book.title}
                    Author: ${book.author}
                    Category: ${book.category}
                    Price: ${book.price}
                    Quantity: ${book.quantity}
                    Publisher: ${book.publisher}
                    Edition: ${book.edition}
                    Vendor: ${book.vendor}
                """.trimIndent()
                detailsCoverImage.setImageResource(book.imageResId)
            } else {
                bookDetailsLayout.visibility = View.GONE
                Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show()
            }
        }

        // Auto-fill if opened via HomeActivity click
        val bookTitleFromIntent = intent.getStringExtra("bookTitle")
        if (!bookTitleFromIntent.isNullOrEmpty()) {
            bookSearchDropdown.setText(bookTitleFromIntent)
            showDetailsButton.performClick()
        }
    }
}
