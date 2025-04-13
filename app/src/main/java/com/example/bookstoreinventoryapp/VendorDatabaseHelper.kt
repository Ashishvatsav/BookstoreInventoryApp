package com.example.bookstoreinventoryapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VendorDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "vendors.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
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
        db.execSQL(createVendorTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS vendors")
        onCreate(db)
    }

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

    fun getAllVendors(): List<VendorDetailsActivity.VendorDetails> {
        val vendorList = mutableListOf<VendorDetailsActivity.VendorDetails>()
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT name, address, owner, experience, rating, books, verified FROM vendors",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val name = cursor.getString(0)
                val address = cursor.getString(1)
                val owner = cursor.getString(2)
                val experience = cursor.getString(3)
                val rating = cursor.getString(4)
                val books = cursor.getString(5)
                val verified = cursor.getInt(6) == 1

                val vendor = VendorDetailsActivity.VendorDetails(
                    name,
                    address,
                    owner,
                    experience,
                    rating,
                    books,
                    verified
                )
                vendorList.add(vendor)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return vendorList
    }
}

