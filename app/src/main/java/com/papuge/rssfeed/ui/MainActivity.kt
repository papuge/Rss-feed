package com.papuge.rssfeed.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.papuge.rssfeed.R
import com.papuge.rssfeed.network.ConnectionReceiver

class MainActivity : AppCompatActivity() {

    private lateinit var receiver: ConnectionReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        receiver = ConnectionReceiver()
        registerReceiver(receiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
