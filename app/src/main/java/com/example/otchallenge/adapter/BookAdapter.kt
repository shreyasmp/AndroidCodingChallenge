package com.example.otchallenge.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.otchallenge.R
import com.example.otchallenge.databinding.ItemBookBinding
import com.example.otchallenge.models.Book

class BookAdapter(private var books: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding: ItemBookBinding = DataBindingUtil
            .inflate(
                LayoutInflater.from(
                    parent.context
                ),
                R.layout.item_book,
                parent,
                false
            )
        return BookViewHolder(binding)
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    internal fun getBooks() = books

    // Updates the list of books and notifies the adapter of data changes
    fun updateBooks(newBooks: List<Book>) {
        val oldSize = books.size
        books = newBooks
        val newSize = books.size

        // Notify the adapter of the changes in the data set
        if (oldSize < newSize) {
            notifyItemRangeInserted(oldSize, newSize - oldSize)
        } else if (oldSize > newSize) {
            notifyItemRangeRemoved(newSize, oldSize - newSize)
        } else {
            notifyItemRangeChanged(0, newSize)
        }
    }

    inner class BookViewHolder(private val binding: ItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.bookTitle.text = book.title
            binding.bookDescription.text = book.description
            Glide.with(binding.bookImage.context)
                .load(book.book_image)
                .placeholder(R.drawable.ic_launcher_background) // Place holder image
                .diskCacheStrategy(DiskCacheStrategy.ALL) // Enable disk caching
                .into(binding.bookImage)
        }
    }
}