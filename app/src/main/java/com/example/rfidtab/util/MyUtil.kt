package com.example.rfidtab.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File


class MyUtil {

    fun createCardFolder() {
        val folderPath = Environment.getExternalStorageDirectory().path + "/RFID cards"
        val folder = File(folderPath)
        if (!folder.exists()) {
            val cardsDirectory = File(folderPath)
            cardsDirectory.mkdirs()
        }
    }

    fun askPermissionForCamera(context: Activity, code: Int) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.CAMERA),
                code
            )
        }
    }

    fun askPermissionForStorage(context: Activity, code: Int) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                code
            )
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                code
            )
        }
    }

    fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view: View? = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun equalsNoSpace(firstText: String, secondText: String): Boolean {
        return firstText.replace(" ", "").equals(secondText.replace(" ", ""), true)
    }


}