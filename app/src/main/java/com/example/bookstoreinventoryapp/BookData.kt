package com.example.bookstoreinventoryapp

object BookData {
    val sampleBooks = listOf(
        Book(
            title = "The Great Gatsby",
            author = "F. Scott Fitzgerald",
            price = "10.99",
            category = "Fiction",
            quantity = 5,
            publisher = "Scribner",
            edition = "1st Edition",
            vendor = "Scribner",
            imageResId = R.drawable.book_cover1
        ),
        Book(
            title = "1984",
            author = "George Orwell",
            price = "9.99",
            category = "Dystopian",
            quantity = 8,
            publisher = "Secker & Warburg",
            edition = "1st Edition",
            vendor = "Harcourt",
            imageResId = R.drawable.book_cover2
        ),
        Book(
            title = "To Kill a Mockingbird",
            author = "Harper Lee",
            price = "12.99",
            category = "Fiction",
            quantity = 3,
            publisher = "J.B. Lippincott & Co.",
            edition = "1st Edition",
            vendor = "HarperCollins",
            imageResId = R.drawable.book_cover3
        )
    )
}
