package com.example.bookstoreinventoryapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BookAdapter(
    private val context: Context,
    private var books: MutableList<Book>,
    private val dbHelper: BookDatabaseHelper
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.itemBookImage)
        val bookTitle: TextView = itemView.findViewById(R.id.itemBookTitle)
        val bookAuthor: TextView = itemView.findViewById(R.id.itemBookAuthor)
        val bookPublisher: TextView = itemView.findViewById(R.id.itemBookPublisher)
        val seeDetailsButton: Button = itemView.findViewById(R.id.btn_see_details)
        val deleteButton: Button = itemView.findViewById(R.id.btn_delete)

        init {
            seeDetailsButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val selectedBook = books[position]
                    val intent = Intent(context, DetailsActivity::class.java).apply {
                        putExtra("bookTitle", selectedBook.title)
                    }
                    context.startActivity(intent)
                }
            }

            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val bookToDelete = books[position]
                    val deleted = dbHelper.deleteBook(bookToDelete.id.toInt())

                    if (deleted) {
                        books.removeAt(position)
                        notifyItemRemoved(position)
                        Toast.makeText(context, "Book deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Failed to delete book", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bookTitle.text = book.title
        holder.bookAuthor.text = book.author
        holder.bookPublisher.text = book.publisher

        val imageUri = book.imageUri
        if (imageUri.isNotEmpty()) {
            Glide.with(context)
                .load(imageUri)
                .placeholder(R.drawable.ic_book_placeholder)
                .into(holder.bookImage)
        } else {
            holder.bookImage.setImageResource(R.drawable.ic_book_placeholder)
        }
    }

    override fun getItemCount(): Int = books.size

    fun updateBooks(newBooks: List<Book>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}
