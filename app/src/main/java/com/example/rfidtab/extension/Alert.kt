package com.example.rfidtab.extension

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.rfidtab.R
import kotlinx.coroutines.*

private lateinit var dialog: AlertDialog

fun AppCompatActivity.loadingShow() {
    try {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_loading, null)
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    } catch (e: Exception) {
        println()
    }
}

fun AppCompatActivity.loadingHide(time: Long = 0) {
    try {
        println()
        CoroutineScope(Dispatchers.IO).launch {
            println()
            delay(time)
            withContext(Dispatchers.Main) {
                dialog.dismiss()
            }
            println()

        }
        println()


    } catch (e: Exception) {
        println()
    }

}


fun Fragment.loadingShow() {
    try {
        val builder = AlertDialog.Builder(activity!!)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_loading, null)
        builder.setView(view)
        builder.setCancelable(false)
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    } catch (e: Exception) {
        println()
    }
}

fun Fragment.loadingHide(time: Long = 0) {
    try {
        println()
        CoroutineScope(Dispatchers.IO).launch {
            println()
            delay(time)
            withContext(Dispatchers.Main) {
                dialog.dismiss()
            }
            println()

        }
        println()


    } catch (e: Exception) {
        println()
    }
}


