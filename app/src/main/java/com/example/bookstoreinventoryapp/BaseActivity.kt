package com.example.bookstoreinventoryapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BaseActivity : AppCompatActivity() {

    protected fun setupBottomNavigation(selectedItemId: Int) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = selectedItemId

        bottomNav.setOnItemSelectedListener { item ->
            if (item.itemId == selectedItemId) return@setOnItemSelectedListener true

            when (item.itemId) {
                R.id.homeActivity -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.addEditActivity -> {
                    startActivity(Intent(this, AddEditActivity::class.java))
                    true
                }
                R.id.detailsActivity -> {
                    startActivity(Intent(this, DetailsActivity::class.java))
                    true
                }
                R.id.profileActivity -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.vendorsActivity -> {
                    startActivity(Intent(this, VendorsActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}
