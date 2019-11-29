package com.papuge.rssfeed.ui

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.papuge.rssfeed.R
import com.papuge.rssfeed.network.ConnectionReceiver
import com.papuge.rssfeed.ui.viewModel.FeedViewModel
import java.lang.Exception
import java.lang.IllegalArgumentException

class MainActivity : AppCompatActivity() {

    private lateinit var receiver: ConnectionReceiver
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        receiver = ConnectionReceiver()
        registerReceiver(receiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == R.id.action_change_url) {
            val alertDialog = androidx.appcompat.app.AlertDialog.Builder(this@MainActivity)
            val view = layoutInflater.inflate(R.layout.dialog_change_url, null)
            val urlEditText: EditText = view.findViewById(R.id.et_change_uri)
            alertDialog.setView(view)

            alertDialog.setPositiveButton(R.string.ok) { dialog, p1 ->
                val url = urlEditText.text.toString()
                if (!url.isBlank()) {
                    viewModel.url = url
                    viewModel.getArticles()
                }
            }

            alertDialog.setNegativeButton(R.string.cancel) { dialog, p1 ->
                dialog.cancel()
            }

            alertDialog.show()

        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
