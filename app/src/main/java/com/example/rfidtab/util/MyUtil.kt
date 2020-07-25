package com.example.rfidtab.util

import android.os.Environment
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
}