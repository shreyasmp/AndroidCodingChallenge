package com.example.otchallenge.network

import android.net.ConnectivityManager
import android.net.Network
import com.example.otchallenge.contract.BookContract

/**
 * Custom NetworkCallback to handle network connectivity changes.
 *
 * @property presenter The presenter to notify about network changes.
 */
class NetworkCallback(private val presenter: BookContract.Presenter) : ConnectivityManager.NetworkCallback() {

    /**
     * Called when the network becomes available.
     *
     * @param network The network that became available.
     */
    override fun onAvailable(network: Network) {
        presenter.loadBooksList()
    }

    /**
     * Called when the network is lost.
     *
     * @param network The network that was lost.
     */
    override fun onLost(network: Network) {
        presenter.onNetworkLost()
    }
}