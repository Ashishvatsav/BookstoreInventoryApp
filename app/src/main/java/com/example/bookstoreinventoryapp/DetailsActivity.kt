package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.button.MaterialButton
import com.google.android.material.snackbar.Snackbar
import java.util.*

class DetailsActivity : AppCompatActivity() {

    private lateinit var dbHelper: BookDatabaseHelper
    private lateinit var bookSearchDropdown: AutoCompleteTextView
    private lateinit var showDetailsButton: MaterialButton
    private lateinit var bookDetailsLayout: CardView
    private lateinit var detailsText: TextView
    private lateinit var detailsCoverImage: ImageView

    private val TAG = "DetailsActivity" // For logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        dbHelper = BookDatabaseHelper(this)

        try {
            // Initialize views
            bookSearchDropdown = findViewById(R.id.bookSearchDropdown)
            showDetailsButton = findViewById(R.id.btn_show_details)
            bookDetailsLayout = findViewById(R.id.bookDetailsLayout)
            detailsText = findViewById(R.id.detailsText)
            detailsCoverImage = findViewById(R.id.detailsCoverImage)

            Log.d(TAG, "Views initialized successfully")

            // Fetch all titles from the DB
            val titles = dbHelper.getAllBookTitles()
            Log.d(TAG, "Fetched ${titles.size} book titles from database")

            // Create adapter
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                titles
            )
            bookSearchDropdown.setAdapter(adapter)
            bookSearchDropdown.threshold = 1

            // Intent auto-fill BEFORE watcher
            val bookTitleFromIntent = intent.getStringExtra("bookTitle")
            if (!bookTitleFromIntent.isNullOrEmpty()) {
                bookSearchDropdown.setText(bookTitleFromIntent)
                showBookDetails(bookTitleFromIntent.trim())
            }

            // On dropdown item selected
            bookSearchDropdown.setOnItemClickListener { _, _, _, _ ->
                showBookDetails(bookSearchDropdown.text.toString().trim())
            }

            // Text change listener to only hide details when text is cleared
            bookSearchDropdown.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable?) {
                    // Only hide the details if the text is cleared
                    if (s.isNullOrEmpty()) {
                        bookDetailsLayout.visibility = View.GONE
                    }
                    // We don't automatically show details anymore - user must select from dropdown or click button
                }
            })

            // Show details button click
            showDetailsButton.setOnClickListener {
                Log.d(TAG, "Show Details button clicked")
                val selectedTitle = bookSearchDropdown.text.toString().trim()
                showBookDetails(selectedTitle)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error initializing: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showBookDetails(selectedTitle: String) {
        if (selectedTitle.isEmpty()) {
            Snackbar.make(
                findViewById(android.R.id.content),
                "Please enter or select a book title",
                Snackbar.LENGTH_SHORT
            ).show()
            Log.d(TAG, "No title entered")
            return
        }

        Log.d(TAG, "Searching for book: $selectedTitle")
        
        try {
            val book = dbHelper.getBookDetailsByTitle(selectedTitle)

            if (book != null) {
                Log.d(TAG, "Book found: ${book.title}")
                val formattedDetails = buildFormattedDetails(book)

                detailsText.text = formattedDetails
                detailsCoverImage.setImageResource(book.imageResId)

                val fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
                fadeIn.duration = 500
                bookDetailsLayout.visibility = View.VISIBLE
                bookDetailsLayout.startAnimation(fadeIn)

                bookDetailsLayout.invalidate()
                bookDetailsLayout.requestLayout()
            } else {
                Log.d(TAG, "Book not found")
                bookDetailsLayout.visibility = View.GONE
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Book not found. Please check the title and try again.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error searching for book: ${e.message}", e)
            bookDetailsLayout.visibility = View.GONE
            Snackbar.make(
                findViewById(android.R.id.content),
                "Error searching for book: ${e.message}",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    private fun buildFormattedDetails(book: Book): CharSequence {
        return """
            Title: ${book.title}
            
            Author: ${book.author}
            
            Category: ${book.category}
            
            Price: $${book.price}
            
            Quantity: ${book.quantity} in stock
            
            Publisher: ${book.publisher}
            
            Edition: ${book.edition}
            
            Vendor: ${book.vendor}
        """.trimIndent()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


