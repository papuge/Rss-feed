package com.example.myinstapaper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar

class AboutActivity : AppCompatActivity() {

    internal val TAG = AboutActivity::class.java.simpleName
    internal lateinit var versionTextView: TextView
    internal lateinit var imeiTextView: TextView

    // request code can be any integer value
    private val REQUEST_READ_PHONE_STATE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        var versionName: String = BuildConfig.VERSION_NAME
        val versionCode: Int = BuildConfig.VERSION_CODE
        Log.d(TAG, "Version: $versionName, Version Code: $versionCode")

        versionTextView = findViewById(R.id.version_text_view)
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED) {
            requestReadPhoneStatePermission()
        } else {
            showImei()
        }
    }

    private fun requestReadPhoneStatePermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_PHONE_STATE)) {
            // permission has been denied
            val retrySnackbar = Snackbar.make(
                findViewById(R.id.my_coordinator_layout),
                R.string.imei_explaination,
                Snackbar.LENGTH_INDEFINITE
            )
            retrySnackbar.setAction(
                R.string.ok,
                View.OnClickListener {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_PHONE_STATE),
                        REQUEST_READ_PHONE_STATE)
                })
            retrySnackbar.show()

        } else {
            // ask for permission
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_PHONE_STATE), REQUEST_READ_PHONE_STATE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun showImei() {
        imeiTextView = findViewById(R.id.imei_text_view)
        val telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val imeiNumber: String = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> telephonyManager.imei
            else -> telephonyManager.deviceId
        }
        imeiTextView.text = getString(R.string.imei_number, imeiNumber)
    }

}
