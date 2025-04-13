package com.example.bookstoreinventoryapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class UserDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "users.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create users table
        val createUserTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                email TEXT UNIQUE,
                password TEXT
            )
        """.trimIndent()

        // Create vendors table
        val createVendorTable = """
            CREATE TABLE vendors (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT,
                address TEXT,
                owner TEXT,
                experience TEXT,
                rating TEXT,
                books TEXT,
                verified INTEGER
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createVendorTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS vendors")
        onCreate(db)
    }

    // ================= USER METHODS =================

    fun insertUser(name: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("email", email)
            put("password", password)
        }
        return db.insert("users", null, values) != -1L
    }

    fun validateUser(email: String, password: String): Int? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE email = ? AND password = ?", arrayOf(email, password))
        return if (cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            cursor.close()
            id
        } else {
            cursor.close()
            null
        }
    }

    fun getUserById(userId: Int): User? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT name, email FROM users WHERE id = ?", arrayOf(userId.toString()))
        return if (cursor.moveToFirst()) {
            val user = User(cursor.getString(0), cursor.getString(1))
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }

    data class User(val name: String, val email: String)

    // ================= VENDOR METHODS =================

    fun insertVendor(vendor: VendorDetailsActivity.VendorDetails) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("name", vendor.name)
            put("address", vendor.address)
            put("owner", vendor.owner)
            put("experience", vendor.experience)
            put("rating", vendor.rating)
            put("books", vendor.books)
            put("verified", if (vendor.verified) 1 else 0)
        }

        db.insert("vendors", null, values)
        db.close()
    }
}