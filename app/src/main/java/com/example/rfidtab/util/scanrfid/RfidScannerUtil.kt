package com.example.rfidtab.util.scanrfid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.example.rfidtab.R
import com.example.rfidtab.util.DataTransfer
import com.senter.support.openapi.StUhf
import kotlinx.android.synthetic.main.fragment_show_loading.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RfidScannerUtil(val onScanListener: RfidScannerListener) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_loading, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading_dismiss_btn.setOnClickListener {
            dismiss()
        }
        scanRfidTag(requireActivity())
    }


    fun scanRfidTag(context: FragmentActivity) {
        var tag = String()
        val bank = StUhf.Bank.TID
        val ptr = 0
        val cnt = 1
        val pwd = "00000000"
        val acsPass = StUhf.AccessPassword.getNewInstance(
            DataTransfer.getBytesByHexString(pwd)
        )

        try {
            CoroutineScope(Dispatchers.IO).launch {
                while (tag.isEmpty()) {
                    val iso18k6c: StUhf.InterrogatorModelDs.UmdOnIso18k6cRead =
                        object : StUhf.InterrogatorModelDs.UmdOnIso18k6cRead() {
                            override fun onFailed(error: StUhf.InterrogatorModelDs.UmdErrorCode) {

                            }

                            override fun onTagRead(
                                tagCount: Int,
                                uii: StUhf.UII,
                                data: ByteArray,
                                frequencyPoint: StUhf.InterrogatorModelDs.UmdFrequencyPoint,
                                antennaId: Int,
                                readCount: Int
                            ) {
                                tag = DataTransfer.xGetString(uii.bytes)
                                context.runOnUiThread {
                                    if (tag.isNotEmpty()) {
                                        onScanListener.onAccessScan(tag)
                                        dismiss()
                                    }
                                }
                            }
                        }

                    val uhf: StUhf =
                        StUhf.getUhfInstance(StUhf.InterrogatorModel.InterrogatorModelD1).apply {
                            init()
                        }

                    uhf.getInterrogatorAs(StUhf.InterrogatorModelDs.InterrogatorModelD1::class.java)
                        .iso18k6cRead(
                            acsPass,
                            bank,
                            ptr,
                            cnt,
                            iso18k6c
                        )
                }
            }
        } catch (e: Exception) {
            dismiss()
            Toast.makeText(context, "Ошибка сканера", Toast.LENGTH_SHORT).show()
            Log.w("RFID SCANNER", e.message)
        }
    }

}