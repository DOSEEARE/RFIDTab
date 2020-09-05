@file:Suppress("NAME_SHADOWING")

package com.example.rfidtab.util

object DataTransfer {
    fun xGetString(bs: ByteArray?): String {
        if (bs != null) {
            val sBuffer = StringBuffer()
            for (i in bs.indices) {
                sBuffer.append(String.format("%02x ", bs[i]))
            }
            return sBuffer.toString()
        }
        return "null"
    }

    fun getBytesByHexString(string: String): ByteArray? {
        var stringLocal = string.replace(" ".toRegex(), "") // delete spaces
        val len = stringLocal.length
        if (len % 2 == 1) {
            return null
        }
        val ret = ByteArray(len / 2)
        for (i in ret.indices) {
            ret[i] = (Integer.valueOf(
                stringLocal.substring(i * 2, i * 2 + 2),
                16
            ) and 0xff).toByte()
        }
        return ret
    }
}