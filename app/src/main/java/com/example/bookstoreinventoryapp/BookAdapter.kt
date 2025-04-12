package com.example.bookstoreinventoryapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private val context: Context, private val books: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    // ViewHolder class to hold each item view
    class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookTitle: TextView = view.findViewById(R.id.itemBookTitle)
        val bookAuthor: TextView = view.findViewById(R.id.itemBookAuthor)
        val bookImage: ImageView = view.findViewById(R.id.itemBookImage)
    }

    // Create a new view holder when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    // Bind data to the view holder
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bookTitle.text = book.title
        holder.bookAuthor.text = book.author
        // Assuming you have some method to set the image resource (e.g., Glide or Picasso)
        holder.bookImage.setImageResource(book.imageResId)
    }

    // Return the number of items in the list
    override fun getItemCount(): Int {
        return books.size
    }
}
