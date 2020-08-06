package com.example.rfidtab.ui.createkit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kit.CreateKitDetailAdapter
import com.example.rfidtab.adapter.kit.CreateKitDetailListener
import com.example.rfidtab.extension.loadingHide
import com.example.rfidtab.extension.loadingShow
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import com.example.rfidtab.service.db.entity.kit.KitRfidEntity
import com.example.rfidtab.service.model.kit.CreateKitModel
import com.example.rfidtab.service.model.kit.KitRfidCards
import com.example.rfidtab.util.DataTransfer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.senter.support.openapi.StUhf
import kotlinx.android.synthetic.main.alert_scan.view.*
import kotlinx.android.synthetic.main.fragment_kit_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class CreateKitDetailFragment(private val model: KitItemEntity) : BottomSheetDialogFragment(),
    CreateKitDetailListener {
    private val viewModel: CreateKitViewModel by viewModel()
    private lateinit var adapter: CreateKitDetailAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        dialog!!.setOnShowListener { dialog ->
            val d: BottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
            BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        dialog!!.setCancelable(false)

        return inflater.inflate(R.layout.fragment_kit_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        addRfidTags()
        sendToCheck()
    }

    private fun sendToCheck() {
        kit_detail_send_button.setOnClickListener {
            loadingShow()
            val rfidCards = ArrayList<KitRfidCards>()
            viewModel.findKitRfid(model.kitId).observe(viewLifecycleOwner, Observer {
                it.forEach {
                    rfidCards.add(KitRfidCards(it.rfid))
                }
            })
            val body = CreateKitModel(model.comment, rfidCards)
            viewModel.createKit(body).observe(viewLifecycleOwner, Observer { result ->
                val data = result.data
                val msg = result.msg
                when (result.status) {
                    Status.SUCCESS -> {
                        loadingHide()
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                        loadingHide()
                    }
                    Status.NETWORK -> {
                        Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG)
                            .show()
                        loadingHide()
                    }
                    else -> {
                        Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
                        loadingHide()
                    }
                }


            })
        }
    }

    private fun addRfidTags() {
        kit_detail_add_button.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireContext())
            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.alert_scan, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()

            view.scan_access_btn.setOnClickListener {
                readTag(view.scan_result_et)
                val tag = view.scan_result_et.text.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertKitRfid(
                        KitRfidEntity(
                            model.kitId,
                            Random.nextInt(0, 1000),
                            tag
                        )
                    )
                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
            view.scan_negative_btn.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    private fun initViews() {
        kit_detail_title.text = model.comment
        viewModel.findKitRfid(model.kitId).observe(viewLifecycleOwner, Observer {
            adapter = CreateKitDetailAdapter(this, it as ArrayList<KitRfidEntity>)
            kit_detail_rv.adapter = adapter
        })

    }

    // чтение RFID метки на кнопку скан
    private fun readTag(resultView: TextInputEditText) {
        var tag = String()
        val bank = StUhf.Bank.TID
        val ptr = 0
        val cnt = 1
        val pwd = "00000000"
        val acsPass = StUhf.AccessPassword.getNewInstance(DataTransfer.getBytesByHexString(pwd))

        try {
            val iso18k6c: StUhf.InterrogatorModelDs.UmdOnIso18k6cRead =
                object : StUhf.InterrogatorModelDs.UmdOnIso18k6cRead() {
                    override fun onFailed(error: StUhf.InterrogatorModelDs.UmdErrorCode) {
                        activity!!.runOnUiThread {
                            Toast.makeText(context, "Нет метки!!!", Toast.LENGTH_LONG)
                                .show()
                        }
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

                        activity!!.runOnUiThread {
                            if (tag.isNotEmpty()) {
                                resultView.setText(tag)
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

        } catch (e: Exception) {
            Toast.makeText(context, "Нет метки!", Toast.LENGTH_SHORT).show()
            Log.w("RFID SCANNER", e.message)
        }
    }

    override fun rfidItemClicked(rfid: KitRfidEntity, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteKitRfid(rfid.rfidId)
        }
        toast("Удалён!")
    }

}