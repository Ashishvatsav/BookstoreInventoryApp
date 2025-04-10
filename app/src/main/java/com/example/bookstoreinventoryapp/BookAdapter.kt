package com.example.bookstoreinventoryapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private var bookList: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>(), Filterable {

    private var filteredList = bookList.toMutableList()

    inner class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.bookTitle)
        val authorText: TextView = itemView.findViewById(R.id.bookAuthor)
        val priceText: TextView = itemView.findViewById(R.id.bookPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = filteredList[position]
        holder.titleText.text = book.title
        holder.authorText.text = book.author
        holder.priceText.text = book.price
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString().lowercase()
                filteredList = if (searchString.isEmpty()) {
                    bookList.toMutableList()
                } else {
                    bookList.filter {
                        it.title.lowercase().contains(searchString) ||
                                it.author.lowercase().contains(searchString)
                    }.toMutableList()
                }

                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as MutableList<Book>
                notifyDataSetChanged()
            }
        }
    }
}
