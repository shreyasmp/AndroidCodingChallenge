package com.example.otchallenge

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otchallenge.adapter.BookAdapter
import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.databinding.ActivityMainBinding
import com.example.otchallenge.models.Book
import com.example.otchallenge.network.NetworkCallback
import com.example.otchallenge.presenter.BookPresenter
import com.example.otchallenge.repository.BookRepository
import javax.inject.Inject

class MainActivity : AppCompatActivity(), BookContract.View {

    @Inject
    lateinit var bookRepository: BookRepository

    @Inject
    lateinit var presenter: BookPresenter

    private lateinit var binding: ActivityMainBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var networkCallback: NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as MyApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        presenter = BookPresenter(this, bookRepository)

        networkCallback = NetworkCallback(presenter)
        connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        // Create a network request to monitor internet connectivity
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        // Register the network callback to handle network changes
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.booksList.layoutManager = LinearLayoutManager(this)
        bookAdapter = BookAdapter(emptyList())
        binding.booksList.adapter = bookAdapter

        if (savedInstanceState == null) {
            presenter.loadBooksList()
        }
    }

    override fun showBooksList(books: List<Book>) {
        runOnUiThread {
            bookAdapter.updateBooks(newBooks = books)
        }
    }

    override fun showError(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showLoading() {
        runOnUiThread {
            binding.progressIndicator.visibility = View.VISIBLE
        }
    }

    override fun hideLoading() {
        runOnUiThread {
            binding.progressIndicator.visibility = View.GONE
        }
    }

    /**
     * Saves the current state of the activity.
     *
     * @param outState The Bundle in which to place your saved state.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("books", ArrayList(bookAdapter.getBooks()))

        outState.putBoolean("isLoading", binding.progressIndicator.visibility == View.VISIBLE)
    }

    /**
     * Restores the previous state of the activity.
     *
     * @param savedInstanceState The Bundle containing the saved state.
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val bookList = savedInstanceState.getParcelableArrayList<Book>("books")
        bookList?.let {
            bookAdapter.updateBooks(newBooks = it)
        }
        val isLoading = savedInstanceState.getBoolean("isLoading")
        if (isLoading) showLoading() else hideLoading()
    }

    /**
     * Called when the activity is destroyed.
     * Unregisters the network callback.
     */
    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}
