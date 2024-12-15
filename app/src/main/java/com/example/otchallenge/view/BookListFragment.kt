package com.example.otchallenge.view

import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.CONNECTIVITY_SERVICE
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.otchallenge.MyApplication
import com.example.otchallenge.adapter.BookAdapter
import com.example.otchallenge.contract.BookContract
import com.example.otchallenge.databinding.FragmentBookListBinding
import com.example.otchallenge.models.Book
import com.example.otchallenge.network.NetworkCallback
import com.example.otchallenge.presenter.BookPresenter
import com.example.otchallenge.repository.BookRepositoryImpl
import javax.inject.Inject

class BookListFragment : Fragment(), BookContract.View {

    @Inject
    lateinit var bookRepository: BookRepositoryImpl

    @Inject
    lateinit var presenter: BookPresenter

    private lateinit var binding: FragmentBookListBinding
    private lateinit var bookAdapter: BookAdapter
    private lateinit var networkCallback: NetworkCallback
    private lateinit var connectivityManager: ConnectivityManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as MyApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = BookPresenter(this, bookRepository)
        binding.booksList.layoutManager = LinearLayoutManager(requireContext())
        bookAdapter = BookAdapter(emptyList())
        binding.booksList.adapter = bookAdapter

        if (savedInstanceState == null) {
            presenter.loadBooksList()
        }
    }

    override fun showBooksList(books: List<Book>) {
        bookAdapter.updateBooks(newBooks = books)
    }

    override fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun showLoading() {
        binding.progressIndicator.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        binding.progressIndicator.visibility = View.GONE
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
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        @Suppress("Deprecated")
        val bookList = savedInstanceState?.getParcelableArrayList<Book>("books")
        bookList?.let {
            bookAdapter.updateBooks(newBooks = it)
        }
        val isLoading = savedInstanceState?.getBoolean("isLoading")
        isLoading?.let {
            if (it) showLoading() else hideLoading()
        }
    }

    /**
     *  It is best to register and unregister the network callback in onStart()
     *  and onStop() methods respectively. This ensures that the network callback is
     *  active only when the activity is in the foreground, which can help conserve resources.
     */
    override fun onStart() {
        super.onStart()
        networkCallback = NetworkCallback(presenter)
        connectivityManager =
            requireContext().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        // Create a network request to monitor internet connectivity
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        // Register the network callback to handle network changes
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    override fun onStop() {
        super.onStop()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    /**
     * Called when the activity is destroyed.
     * Unregisters the network callback.
     */
    override fun onDestroy() {
        super.onDestroy()
        presenter.cleanup()
    }
}