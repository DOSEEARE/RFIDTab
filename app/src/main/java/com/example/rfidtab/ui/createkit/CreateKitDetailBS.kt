package com.example.rfidtab.ui.createkit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
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
import com.example.rfidtab.util.scanrfid.RfidScannerListener
import com.example.rfidtab.util.scanrfid.RfidScannerUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.alert_scan.view.*
import kotlinx.android.synthetic.main.fragment_kit_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class CreateKitDetailBS(private val model: KitItemEntity) : BottomSheetDialogFragment(),
    CreateKitDetailListener, RfidScannerListener {
    private val viewModel: CreateKitViewModel by viewModel()
    private lateinit var adapter: CreateKitDetailAdapter
    private lateinit var scanDialog: AlertDialog

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
            viewModel.findKitRfid(model.kitId).observe(viewLifecycleOwner, Observer {
                val rfidList = ArrayList<KitRfidCards>()
                it.forEach {
                    rfidList.add(KitRfidCards(it.rfid))
                }
                val body = CreateKitModel(model.comment, rfidList)
                viewModel.createKit(body).observe(viewLifecycleOwner, Observer { result ->
                    val data = result.data
                    val msg = result.msg
                    when (result.status) {
                        Status.SUCCESS -> {
                            toast("Успешно!")
                            viewModel.deleteKitItem(model.kitId)
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
            })

        }
    }

    private fun addRfidTags() {
        kit_detail_add_button.setOnClickListener {
            createScanDialog("")
        }
    }

    private fun initViews() {
        kit_detail_title.text = model.comment
        viewModel.findKitRfid(model.kitId).observe(viewLifecycleOwner, Observer {
            adapter = CreateKitDetailAdapter(this, it as ArrayList<KitRfidEntity>)
            kit_detail_rv.adapter = adapter
        })

    }

    private fun createScanDialog(accesTag: String) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_scan, null)
        dialogBuilder.setView(view)
        scanDialog = dialogBuilder.create()

        view.scan_result_et.setText(accesTag)

        view.scan_problem_checkbox.isVisible = false

        view.scan_scanner.setOnClickListener {
            scanDialog.dismiss()
            RfidScannerUtil(this).run {
                isCancelable = false
                show(this@CreateKitDetailBS.childFragmentManager, "ShowScanning")
            }
            //  RfidScanner().scanRfidTag(view.scan_result_et, requireActivity())
            //  readTag(view.scan_result_et)
        }

        view.scan_access_btn.setOnClickListener {
            val tag = view.scan_result_et.text.toString()
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.insertKitRfid(
                    KitRfidEntity(
                        model.kitId, Random.nextInt(0, 1000), tag)
                )
                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
            scanDialog.dismiss()
        }
        view.scan_negative_btn.setOnClickListener {
            scanDialog.dismiss()
        }
        scanDialog.show()
    }

    //Удаление найденных меток
    override fun rfidItemDelete(rfid: KitRfidEntity, position: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.deleteKitRfid(rfid.rfidId)
        }
        toast("Удалён!")
    }

    //Пересоздание алерт при успешном скане
    override fun onAccessScan(tag: String) {
        scanDialog.dismiss()
        createScanDialog(tag)
    }

}