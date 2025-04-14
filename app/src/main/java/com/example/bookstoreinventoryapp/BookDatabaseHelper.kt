package com.example.bookstoreinventoryapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class BookDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Bookstore.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_BOOKS = "Books"

        // Columns
        private const val ID = "id"
        private const val TITLE = "title"
        private const val AUTHOR = "author"
        private const val PRICE = "price"
        private const val CATEGORY = "category"
        private const val QUANTITY = "quantity"
        private const val PUBLISHER = "publisher"
        private const val EDITION = "edition"
        private const val VENDOR = "vendor"
        private const val IMAGE_RES_ID = "imageResId"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_BOOKS (
                $ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $TITLE TEXT,
                $AUTHOR TEXT,
                $PRICE TEXT,
                $CATEGORY TEXT,
                $QUANTITY INTEGER,
                $PUBLISHER TEXT,
                $EDITION TEXT,
                $VENDOR TEXT,
                $IMAGE_RES_ID INTEGER
            )
        """.trimIndent()
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    fun insertBook(book: Book): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TITLE, book.title)
            put(AUTHOR, book.author)
            put(PRICE, book.price)
            put(CATEGORY, book.category)
            put(QUANTITY, book.quantity)
            put(PUBLISHER, book.publisher)
            put(EDITION, book.edition)
            put(VENDOR, book.vendor)
            put(IMAGE_RES_ID, book.imageResId)
        }
        val id = db.insert(TABLE_BOOKS, null, values)
        db.close()
        return id
    }

    fun insertSampleBooksIfNeeded() {
        if (getBooksCount() == 0) {
            BookData.sampleBooks.forEach { insertBook(it) }
        }
    }

    private fun getBooksCount(): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_BOOKS", null)
        return cursor.use {
            if (it.moveToFirst()) it.getInt(0) else 0
        }
    }

    fun getAllBooks(): List<Book> {
        val db = readableDatabase
        val cursor = db.query(TABLE_BOOKS, null, null, null, null, null, null)
        return cursor.use {
            val books = mutableListOf<Book>()
            while (it.moveToNext()) {
                books.add(extractBookFromCursor(it))
            }
            books
        }
    }

    fun searchBooks(query: String): List<Book> {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_BOOKS WHERE " +
                    "LOWER($TITLE) LIKE ? OR LOWER($AUTHOR) LIKE ?",
            arrayOf("%${query.lowercase()}%", "%${query.lowercase()}%")
        )
        return cursor.use {
            val books = mutableListOf<Book>()
            while (it.moveToNext()) {
                books.add(extractBookFromCursor(it))
            }
            books
        }
    }

    private fun extractBookFromCursor(cursor: Cursor): Book {
        return Book(
            id = cursor.getLong(cursor.getColumnIndexOrThrow(ID)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE)),
            author = cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR)),
            price = cursor.getString(cursor.getColumnIndexOrThrow(PRICE)),
            category = cursor.getString(cursor.getColumnIndexOrThrow(CATEGORY)),
            quantity = cursor.getInt(cursor.getColumnIndexOrThrow(QUANTITY)),
            publisher = cursor.getString(cursor.getColumnIndexOrThrow(PUBLISHER)),
            edition = cursor.getString(cursor.getColumnIndexOrThrow(EDITION)),
            vendor = cursor.getString(cursor.getColumnIndexOrThrow(VENDOR)),
            imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(IMAGE_RES_ID))
        )
    }


    // Other methods (getBookByTitle, etc.) remain the same

    // Add these methods to your BookDatabaseHelper class
    fun getAllBookTitles(): List<String> {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            arrayOf(TITLE),
            null, null, null, null, null
        )

        return cursor.use {
            val titles = mutableListOf<String>()
            while (it.moveToNext()) {
                titles.add(it.getString(it.getColumnIndexOrThrow(TITLE)))
            }
            titles
        }
    }

    fun getBookDetailsByTitle(title: String): Book? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            null,
            "$TITLE = ?",
            arrayOf(title),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                extractBookFromCursor(it)
            } else {
                null
            }
        }
    }

    fun updateBook(book: Book): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(TITLE, book.title)
            put(AUTHOR, book.author)
            put(PRICE, book.price)
            put(CATEGORY, book.category)
            put(QUANTITY, book.quantity)
            put(PUBLISHER, book.publisher)
            put(EDITION, book.edition)
            put(VENDOR, book.vendor)
            put(IMAGE_RES_ID, book.imageResId)
        }

        return db.update(TABLE_BOOKS, values, "$ID = ?", arrayOf(book.id.toString()))
    }

    fun getBookDetailsById(bookId: Long): Book? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            null,
            "$ID = ?",
            arrayOf(bookId.toString()), // Convert Long to String
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                extractBookFromCursor(it)
            } else {
                null
            }
        }
    }

    fun getBookIdByTitle(title: String): Long? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            arrayOf(ID),
            "$TITLE = ?",
            arrayOf(title),
            null, null, null
        )

        return cursor.use {
            if (it.moveToFirst()) {
                it.getLong(it.getColumnIndexOrThrow(ID))
            } else {
                null
            }
        }
    }

    fun deleteBook(bookId: Int): Boolean {
        val db = writableDatabase
        val result = db.delete("books", "id = ?", arrayOf(bookId.toString()))
        db.close()
        return result > 0
    }

    fun searchBooksByTitle(query: String): List<Book> {
        val books = mutableListOf<Book>()
        val db = this.readableDatabase

        // Define query to search by book title
        val cursor = db.rawQuery("SELECT * FROM books WHERE title LIKE ?", arrayOf("%$query%"))

        // Get column indexes safely
        val idIndex = cursor.getColumnIndex("id")
        val titleIndex = cursor.getColumnIndex("title")
        val authorIndex = cursor.getColumnIndex("author")
        val imageResIdIndex = cursor.getColumnIndex("imageResId")
        val categoryIndex = cursor.getColumnIndex("category")
        val editionIndex = cursor.getColumnIndex("edition")
        val priceIndex = cursor.getColumnIndex("price")
        val publisherIndex = cursor.getColumnIndex("publisher")
        val quantityIndex = cursor.getColumnIndex("quantity")
        val vendorIndex = cursor.getColumnIndex("vendor")

        if (idIndex == -1 || titleIndex == -1 || authorIndex == -1) {
            // Log or throw an exception, depending on your app's behavior
            Log.e("Database", "One or more columns are missing in the database!")
            cursor.close()
            return books
        }

        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    id = cursor.getLong(idIndex),
                    title = cursor.getString(titleIndex),
                    author = cursor.getString(authorIndex),
                    imageResId = if (imageResIdIndex != -1) cursor.getInt(imageResIdIndex) else 0,
                    category = if (categoryIndex != -1) cursor.getString(categoryIndex) else "",
                    edition = if (editionIndex != -1) cursor.getString(editionIndex) else "",
                    price = if (priceIndex != -1) cursor.getString(priceIndex) else "0.0", // Changed to String for price
                    publisher = if (publisherIndex != -1) cursor.getString(publisherIndex) else "",
                    quantity = if (quantityIndex != -1) cursor.getInt(quantityIndex) else 0,
                    vendor = if (vendorIndex != -1) cursor.getString(vendorIndex) else ""
                )
                books.add(book)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return books
    }









}