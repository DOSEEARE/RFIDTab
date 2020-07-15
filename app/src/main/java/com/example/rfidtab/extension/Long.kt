package com.example.rfidtab.extension

import java.text.SimpleDateFormat
import java.util.*

fun Long.toServerDate(): String {
    return try {
        val date = Date(this)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        format.format(date).replace(" ", "T")
    } catch (e: Exception) {
        ""
    }

}

