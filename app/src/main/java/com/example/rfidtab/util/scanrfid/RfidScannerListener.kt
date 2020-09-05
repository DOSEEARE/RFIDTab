package com.example.rfidtab.util.scanrfid

interface RfidScannerListener {
    fun onAccessScan(tag: String)
}