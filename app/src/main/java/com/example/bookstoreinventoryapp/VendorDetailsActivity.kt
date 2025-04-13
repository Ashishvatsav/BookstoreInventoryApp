package com.example.bookstoreinventoryapp

import android.os.Bundle
import android.widget.TextView
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class VendorDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vendor_details)

        // Retrieve the vendor name passed via the intent
        val vendorName = intent.getStringExtra("vendor_name") ?: "Unknown Vendor"

        Log.d("VendorDetailsActivity", "Vendor Name: $vendorName")

        // Get vendor details
        val vendorDetails = getVendorDetails(vendorName)

        // ✅ Save vendor details to SQLite (vendors.db)
        val dbHelper = VendorDatabaseHelper(this)
        dbHelper.insertVendor(vendorDetails)

        // Display vendor details on screen
        findViewById<TextView>(R.id.vendorName).text = vendorDetails.name
        findViewById<TextView>(R.id.vendorAddress).text = "Address: ${vendorDetails.address}"
        findViewById<TextView>(R.id.vendorOwner).text = "Owner: ${vendorDetails.owner}"
        findViewById<TextView>(R.id.vendorExperience).text = "Experience: ${vendorDetails.experience}"
        findViewById<TextView>(R.id.vendorRating).text = "Rating: ${vendorDetails.rating}"
        findViewById<TextView>(R.id.vendorBooks).text = "Books: ${vendorDetails.books}"
        findViewById<TextView>(R.id.vendorVerified).text =
            if (vendorDetails.verified) "Trust Verified ✅" else "Not Verified ❌"
    }

    data class VendorDetails(
        val name: String,
        val address: String,
        val owner: String,
        val experience: String,
        val rating: String,
        val books: String,
        val verified: Boolean
    )

    private fun getVendorDetails(name: String?): VendorDetails {
        return when (name) {
            "Book Galaxy" -> VendorDetails(
                name = "Book Galaxy",
                address = "12 Market Street, Downtown Plaza, Hyderabad - 500081",
                owner = "Anita Desai (Founder & Curator)",
                experience = "8 years in specialty book retail and curation",
                rating = "★★★★☆ (4.5 based on 1.2k reviews)",
                books = "Sci-Fi, Romance, Kids, Graphic Novels, Manga, Bestsellers, Collectibles",
                verified = true
            )
            "Readers Hub" -> VendorDetails(
                name = "Readers Hub",
                address = "45 Elm Road, Jubilee Hills, Hyderabad - 500033",
                owner = "Rajeev Menon (Co-founder & Book Critic)",
                experience = "5 years in literary curation and academic publishing",
                rating = "★★★★☆ (4.3 from 900+ verified customers)",
                books = "Biography, Fiction, Science, History, Philosophy, Contemporary Literature",
                verified = false
            )
            "Fiction Avenue" -> VendorDetails(
                name = "Fiction Avenue",
                address = "789 Fiction Blvd, Banjara Hills, Hyderabad - 500034",
                owner = "Meera Patel (Founder & Literary Curator)",
                experience = "10 years in fiction retail, author events, and storytelling clubs",
                rating = "★★★★☆ (4.6 from 1.5k fiction lovers)",
                books = "Biography, Fiction, Science, Young Adult, Mystery, Historical Novels, Women-Centric Literature",
                verified = true
            )
            "The Storyteller’s Shelf" -> VendorDetails(
                name = "The Storyteller’s Shelf",
                address = "63 Willow Lane, Literary Enclave, Hyderabad - 500029",
                owner = "Karthik Sharma (Founder & Cultural Historian)",
                experience = "4 years specializing in historical narratives and curated literary collections",
                rating = "★★★★☆ (4.4 based on 600+ loyal readers)",
                books = "Historical Fiction, Essays, Narrative Non-Fiction, Biographical Sketches, Classic Memoirs, Travelogues",
                verified = false
            )
            "BookBarn Express" -> VendorDetails(
                name = "BookBarn Express",
                address = "88 Express Way, Knowledge Park, Hyderabad - 500072",
                owner = "Neha Verma (Managing Director & Research Enthusiast)",
                experience = "12 years in academic publishing and professional book distribution",
                rating = "★★★☆☆ (3.9 from 700+ business professionals)",
                books = "Business, Economics, Political Science, Law, Management, Competitive Exam Guides",
                verified = true
            )
            else -> VendorDetails(
                name ?: "Unknown", "N/A", "N/A", "N/A", "N/A", "N/A", false
            )
        }
    }
}