package com.example.rfidtab.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Environment
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
}