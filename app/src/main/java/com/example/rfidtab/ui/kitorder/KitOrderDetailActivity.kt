package com.example.rfidtab.ui.kitorder

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.card.KitCardOnlineAdapter
import com.example.rfidtab.adapter.kitorder.card.KitCardSavedAdapter
import com.example.rfidtab.adapter.kitorder.card.KitCardSavedListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.db.entity.kitorder.KitOrderCardEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderKitEntity
import com.example.rfidtab.service.response.kitorder.KitOrderCard
import com.example.rfidtab.service.response.kitorder.KitOrderKit
import com.example.rfidtab.ui.task.TaskViewModel
import com.example.rfidtab.util.DataTransfer
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.senter.support.openapi.StUhf
import kotlinx.android.synthetic.main.activity_kit_detail_order.*
import kotlinx.android.synthetic.main.alert_scan.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class KitOrderDetailActivity : AppCompatActivity(), KitCardSavedListener {
    private val taskViewModel: TaskViewModel by viewModel()
    private val kitOrderViewModel: KitOrderViewModel by viewModel()
    private var tag = String()

    private lateinit var onlineData: KitOrderKit
    private lateinit var savedData: KitOrderKitEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kit_detail_order)
        supportActionBar?.title = "Комплектация в аренду"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
    }

    private fun initViews() {
        val isOnline = intent.getBooleanExtra("isOnline", true)

        if (isOnline) {
            onlineData = intent.getSerializableExtra("data") as KitOrderKit

            kit_order_detail_title.text = "Титул: ${onlineData.title}"
            kit_order_detail_rv.adapter =
                KitCardOnlineAdapter(onlineData.cards as ArrayList<KitOrderCard>)
        } else {
            savedData = intent.getSerializableExtra("data") as KitOrderKitEntity
            kit_order_detail_title.text = "Титул: ${savedData.title}"
            kitOrderViewModel.findKitCards(savedData.id).observe(this, Observer {
                kit_order_detail_rv.adapter =
                    KitCardSavedAdapter(this, it as ArrayList<KitOrderCardEntity>)
            })

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun scanBtnClicked(model: KitOrderCardEntity) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_scan, null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()
        view.scan_name.text = model.fullName

        //Сохранение карточки
        view.scan_access_btn.setOnClickListener {
            if (tag.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    kitOrderViewModel.updateKitCard(model.id, tag)
                    withContext(Dispatchers.Main) {
                        toast("Успешно сохранен!")
                        kitOrderViewModel.deleteKitTaskById(model.id)
                        alertDialog.dismiss()
                    }

                }
            }
        }

        //Чтение со сканера
        view.scan_scanner.setOnClickListener {
            readTag(view.scan_result_et, view.scan_comment_out)
        }

        view.scan_negative_btn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun readTag(resultView: TextInputEditText, commentView: TextInputLayout) {
        val bank = StUhf.Bank.TID
        val ptr = 0
        val cnt = 1
        val pwd = "00000000"
        val acsPass = StUhf.AccessPassword.getNewInstance(DataTransfer.getBytesByHexString(pwd))

        try {
            val iso18k6c: StUhf.InterrogatorModelDs.UmdOnIso18k6cRead =
                object : StUhf.InterrogatorModelDs.UmdOnIso18k6cRead() {
                    override fun onFailed(error: StUhf.InterrogatorModelDs.UmdErrorCode) {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Нет метки!!!", Toast.LENGTH_LONG)
                                .show()
                            commentView.visibility = View.VISIBLE
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

                        if (tag.isNotEmpty()) {
                            runOnUiThread {
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
            Toast.makeText(this, "Нет метки!", Toast.LENGTH_SHORT).show()
            Log.w("RFID SCANNER", e.message)
        }
    }

}