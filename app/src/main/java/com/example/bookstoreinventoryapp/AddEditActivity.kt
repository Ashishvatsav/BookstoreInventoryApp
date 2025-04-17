package com.example.bookstoreinventoryapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class AddEditActivity : AppCompatActivity() {

    private lateinit var dbHelper: BookDatabaseHelper
    private lateinit var editTextTitle: EditText
    private lateinit var editTextAuthor: EditText
    private lateinit var editTextPrice: EditText
    private lateinit var editTextCategory: EditText
    private lateinit var editTextQuantity: EditText
    private lateinit var editTextPublisher: EditText
    private lateinit var editTextEdition: EditText
    private lateinit var editTextVendor: EditText
    private lateinit var btnSave: Button
    private lateinit var spinnerBooks: Spinner
    private lateinit var btnAddItem: Button
    private lateinit var btnEditItem: Button
    private lateinit var btnSelectImage: Button
    private lateinit var imageViewCover: ImageView

    private var selectedImageUri: Uri? = null
    private var editingBookId: Long? = null

    companion object {
        const val IMAGE_PICK_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        dbHelper = BookDatabaseHelper(this)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextAuthor = findViewById(R.id.editTextAuthor)
        editTextPrice = findViewById(R.id.editTextPrice)
        editTextCategory = findViewById(R.id.editTextCategory)
        editTextQuantity = findViewById(R.id.editTextQuantity)
        editTextPublisher = findViewById(R.id.editTextPublisher)
        editTextEdition = findViewById(R.id.editTextEdition)
        editTextVendor = findViewById(R.id.editTextVendor)
        btnSave = findViewById(R.id.btnSaveBook)
        spinnerBooks = findViewById(R.id.spinnerBooks)
        btnAddItem = findViewById(R.id.btnAddItem)
        btnEditItem = findViewById(R.id.btnEditItem)
        btnSelectImage = findViewById(R.id.btnSelectImage)
        imageViewCover = findViewById(R.id.imageViewCover)

        setAllFieldsVisibility(View.GONE)

        btnAddItem.setOnClickListener {
            setAllFieldsVisibility(View.VISIBLE)
            spinnerBooks.visibility = View.GONE
            clearFields()
            editingBookId = null
        }

        btnEditItem.setOnClickListener {
            setAllFieldsVisibility(View.VISIBLE)
            spinnerBooks.visibility = View.VISIBLE
            populateBookSpinner()
        }

        btnSelectImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        }

        btnSave.setOnClickListener {
            saveBook()
        }
    }

    private fun populateBookSpinner() {
        val titles = dbHelper.getAllBookTitles()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, titles)
        spinnerBooks.adapter = adapter

        spinnerBooks.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?, view: View?, position: Int, id: Long
            ) {
                val selectedTitle = titles[position]
                val book = dbHelper.getBookDetailsByTitle(selectedTitle)
                book?.let {
                    editingBookId = it.id
                    editTextTitle.setText(it.title)
                    editTextAuthor.setText(it.author)
                    editTextPrice.setText(it.price)
                    editTextCategory.setText(it.category)
                    editTextQuantity.setText(it.quantity.toString())
                    editTextPublisher.setText(it.publisher)
                    editTextEdition.setText(it.edition)
                    editTextVendor.setText(it.vendor)
                    selectedImageUri = if (it.imageUri.isNotEmpty()) Uri.parse(it.imageUri) else null
                    Glide.with(this@AddEditActivity)
                        .load(selectedImageUri ?: R.drawable.ic_book_placeholder)
                        .into(imageViewCover)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun saveBook() {
        val title = editTextTitle.text.toString()
        val author = editTextAuthor.text.toString()
        val price = editTextPrice.text.toString()
        val category = editTextCategory.text.toString()
        val quantity = editTextQuantity.text.toString().toIntOrNull() ?: 0
        val publisher = editTextPublisher.text.toString()
        val edition = editTextEdition.text.toString()
        val vendor = editTextVendor.text.toString()
        val imageUriString = selectedImageUri?.toString() ?: ""

        val book = Book(
            id = editingBookId ?: 0,
            title = title,
            author = author,
            price = price,
            category = category,
            quantity = quantity,
            publisher = publisher,
            edition = edition,
            vendor = vendor,
            imageUri = imageUriString
        )

        if (editingBookId == null) {
            dbHelper.insertBook(book)
            Toast.makeText(this, "Book added", Toast.LENGTH_SHORT).show()
        } else {
            dbHelper.updateBook(book)
            Toast.makeText(this, "Book updated", Toast.LENGTH_SHORT).show()
        }

        clearFields()
        setAllFieldsVisibility(View.GONE)
        spinnerBooks.visibility = View.GONE
    }

    private fun clearFields() {
        editTextTitle.text.clear()
        editTextAuthor.text.clear()
        editTextPrice.text.clear()
        editTextCategory.text.clear()
        editTextQuantity.text.clear()
        editTextPublisher.text.clear()
        editTextEdition.text.clear()
        editTextVendor.text.clear()
        imageViewCover.setImageResource(R.drawable.ic_book_placeholder)
        selectedImageUri = null
    }

    private fun setAllFieldsVisibility(visibility: Int) {
        editTextTitle.visibility = visibility
        editTextAuthor.visibility = visibility
        editTextPrice.visibility = visibility
        editTextCategory.visibility = visibility
        editTextQuantity.visibility = visibility
        editTextPublisher.visibility = visibility
        editTextEdition.visibility = visibility
        editTextVendor.visibility = visibility
        btnSave.visibility = visibility
        btnSelectImage.visibility = visibility
        imageViewCover.visibility = visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.data
            imageViewCover.setImageURI(selectedImageUri)
        }
    }
}
