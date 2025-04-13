package com.example.bookstoreinventoryapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private val context: Context, private var books: MutableList<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.itemBookImage)
        val bookTitle: TextView = itemView.findViewById(R.id.itemBookTitle)
        val bookAuthor: TextView = itemView.findViewById(R.id.itemBookAuthor)
        val seeDetailsButton: Button = itemView.findViewById(R.id.btn_see_details)

        init {
            seeDetailsButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedBook = books[position]
                    val intent = Intent(context, DetailsActivity::class.java).apply {
                        putExtra("bookTitle", selectedBook.title)
                        // Add other book details as needed
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val currentBook = books[position]
        holder.bookTitle.text = currentBook.title
        holder.bookAuthor.text = currentBook.author

        // Safely load image
        if (currentBook.imageResId != 0) {
            holder.bookImage.setImageResource(currentBook.imageResId)
        } else {
            holder.bookImage.setImageResource(R.drawable.ic_book_placeholder) // fallback image
        }
    }



    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}