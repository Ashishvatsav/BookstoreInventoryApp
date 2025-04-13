package com.example.bookstoreinventoryapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class VendorsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendors)
    }

    // This function will be called when a vendor card is clicked
    fun onVendorCardClick(view: View) {
        val vendorName = view.tag as? String ?: return

        // Start VendorDetailsActivity and pass vendor name as extra
        val intent = Intent(this, VendorDetailsActivity::class.java)
        intent.putExtra("vendor_name", vendorName)
        startActivity(intent)
    }
}