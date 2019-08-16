package com.example.myinstapaper

import android.Manifest.permission.READ_PHONE_STATE
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    internal val TAG = MainActivity::class.java.simpleName
    internal lateinit var versionTextView: TextView
    internal lateinit var imeiTextView: TextView

    // request code can be any integer value
    private val REQUEST_READ_PHONE_STATE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var versionName: String = BuildConfig.VERSION_NAME
        val versionCode: Int = BuildConfig.VERSION_CODE
        Log.d(TAG, "Version: $versionName, Version Code: $versionCode")

        versionTextView = findViewById<TextView>(R.id.version_text_view)
        versionTextView.text = getString(R.string.version_name, versionName)

        showImeiPreview()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_READ_PHONE_STATE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showImei()
                    Log.i(TAG, "Permission has been granted by user")
                } else {
                    Snackbar.make(
                        findViewById(R.id.my_coordinator_layout),
                        R.string.imei_permission_denied,
                        Snackbar.LENGTH_SHORT
                    )
                    Log.i(TAG, "Permission has been denied by user")
                }
            }
        }
    }

    private fun showImeiPreview() {
        if (ContextCompat.checkSelfPermission(this, READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            requestReadPhoneStatePermission()
        } else {
            showImei()
        }
    }

    private fun requestReadPhoneStatePermission() {
        if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
            // permission has been denied
            val retrySnackbar = Snackbar.make(
                findViewById(R.id.my_coordinator_layout),
                R.string.imei_explaination,
                Snackbar.LENGTH_INDEFINITE
            )
            retrySnackbar.setAction(
                R.string.ok,
                View.OnClickListener {
                    ActivityCompat.requestPermissions(this, arrayOf(READ_PHONE_STATE), REQUEST_READ_PHONE_STATE)
                })
            retrySnackbar.show()

        } else {
            // ask for permission
            ActivityCompat.requestPermissions(this, arrayOf(READ_PHONE_STATE), REQUEST_READ_PHONE_STATE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showImei() {
        imeiTextView = findViewById<TextView>(R.id.imei_text_view)
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imeiNumber: String = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> telephonyManager.imei
            else -> telephonyManager.deviceId
        }
        imeiTextView.text = getString(R.string.imei_number, imeiNumber)
    }

}
