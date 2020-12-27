package com.example.rfidtab.ui.task.kitorder

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.card.KitCardOnlineAdapter
import com.example.rfidtab.adapter.kitorder.card.KitCardSavedAdapter
import com.example.rfidtab.adapter.kitorder.card.KitCardSavedListener
import com.example.rfidtab.adapter.kitorder.kitdetail.KitDetailAddCardAdapter
import com.example.rfidtab.adapter.kitorder.kitdetail.KitDetailAddCardClickListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.kitorder.KitOrderAddCardEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderCardEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderKitEntity
import com.example.rfidtab.service.response.kitorder.KitOrderCard
import com.example.rfidtab.service.response.kitorder.KitOrderKit
import com.example.rfidtab.util.MyUtil
import com.example.rfidtab.util.scanrfid.RfidScannerListener
import com.example.rfidtab.util.scanrfid.RfidScannerUtil
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kit_detail_order.*
import kotlinx.android.synthetic.main.alert_add.view.*
import kotlinx.android.synthetic.main.alert_scan.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class KitOrderDetailActivity : AppCompatActivity(), KitCardSavedListener, RfidScannerListener,
    KitDetailAddCardClickListener {
    private val kitOrderViewModel: KitOrderViewModel by viewModel()
    private lateinit var scanDialog: AlertDialog
    private lateinit var currentModel: KitOrderCardEntity

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
        val kitPosition = intent.getIntExtra("kitPosition", 0)
        val taskId = intent.getIntExtra("taskId", 0)
        val withKit = intent.getStringExtra("withKit")

        if (isOnline) {
            onlineData = intent.getSerializableExtra("data") as KitOrderKit
            kit_order_add_btn.isVisible = false
            if (withKit == "withCatalog") {
                Log.d("AddCardsCheck", "Добавленные карточки withCatalog  $withKit}: ")
                kit_order_spec_card.visibility = View.GONE
                kit_order_detail_rv.adapter =
                    KitCardOnlineAdapter(onlineData.cards as ArrayList<KitOrderCard>)
                kit_order_count_card.isVisible = false
            } else {
                Log.d("AddCardsCheck", "Добавленные карточки withOutCatalog $withKit}: ")
                kit_order_count_card.isVisible = false
                kit_order_rv_card.visibility = View.GONE
                val info = onlineData.specification

                kit_order_tv_1.text = "Тип оборудования: ${info.refTypeEquipment?.value}"
                kit_order_tv_2.text = "Тип высадки: ${info.refTypeDisembarkation?.value}"
                kit_order_tv_3.text = "Наружный диаметр трубы, (мм): ${info.outerDiameterOfThePipe}"
                kit_order_tv_4.text = "Толщина стенки трубы, (мм): ${info.pipeWallThickness}"
                kit_order_tv_5.text = "Тип резьбы:${info.refTypeThreadTitle}"
                kit_order_tv_6.text = "O.D. Замка ниппель (мм): ${info.odlockNipple}"
                kit_order_tv_7.text = "I.D. Замка ниппель (мм): ${info.idlockNipple}"
                kit_order_tv_8.text = "Длина трубы: ${info.pipeLength}"
                kit_order_tv_9.text = "Угол заплетчика:${info.shoulderAngle}"
                kit_order_tv_10.text ="Длина под ключ ниппель, (мм): ${ info.turnkeyLengthNipple}"
                kit_order_tv_11.text ="Длина под ключ муфта, (мм): ${ info.turnkeyLengthCoupling}"
                kit_order_tv_12.text ="Покрытие резьбы: ${ info.refThreadCoatingTitle}"
                kit_order_tv_13.text ="Внутреннее покрытие: ${ info.refInnerCoatingTitle}"
                kit_order_tv_14.text ="Хардбендинг (муфта): ${ info.refHardbandingCouplingTitle}"
                kit_order_tv_15.text ="Комментарий: ${ info.comment}"


                kitOrderViewModel.kitOrder(taskId).observe(this, Observer { result ->
                    val data = result.data

                    when (result.status) {
                        Status.SUCCESS -> {
                            val listRe = ArrayList<KitOrderAddCardEntity>()
                            data!!.kits[kitPosition].cardListNotConfirmed.forEach {
                                listRe.add(
                                    KitOrderAddCardEntity(
                                        id = it.id,
                                        serialNoOfNipple = it.serialNoOfNipple,
                                        pipeSerialNumber = it.pipeSerialNumber,
                                        couplingSerialNumber = it.couplingSerialNumber,
                                        rfidTagNo = it.rfidTagNo,
                                        comment = it.comment,
                                        accounting = initAccounting(it.accounting),
                                        kitId = onlineData.id,
                                        taskId = taskId
                                    )
                                )
                            }

                            data.kits[kitPosition].cardListConfirmed.forEach {
                                listRe.add(
                                    KitOrderAddCardEntity(
                                        id = it.id,
                                        serialNoOfNipple = it.serialNoOfNipple,
                                        pipeSerialNumber = it.pipeSerialNumber,
                                        couplingSerialNumber = it.couplingSerialNumber,
                                        rfidTagNo = it.rfidTagNo,
                                        comment = it.comment,
                                        accounting = initAccounting(it.accounting),
                                        kitId = onlineData.id,
                                        taskId = taskId
                                    )
                                )
                            }

                            kit_order_detail_rv.visibility = View.VISIBLE
                            Log.d("AddCardsCheck", "Добавленные карточки показано в сохраненных ${Gson().toJson(listRe)}: ")
                            kit_order_rv_card.isVisible = true
                            kit_order_added_rv.adapter = KitDetailAddCardAdapter(listRe, true, this)
                        }
                    }
                })
            }

        } else {
            savedData = intent.getSerializableExtra("data") as KitOrderKitEntity
            if (withKit == "withoutCatalog") {
                kit_order_count_card.isVisible = false

                Log.d("AddCardsCheck", "Добавленные карточки withoutCatalog $withKit}: ")
                Log.d("insertKitOrder", "insertKitOrderSpec ID: ${savedData.id}")
                kitOrderViewModel.findKitOrderSpecByKitId(savedData.id)
                    .observe(this, Observer { spec ->

                        kit_order_tv_1.text = "Тип оборудования: ${spec.refTypeEquipment}"
                        kit_order_tv_2.text = "Тип высадки: ${spec.refTypeDisembarkation}"
                        kit_order_tv_3.text = "Наружный диаметр трубы, (мм): ${spec.outerDiameterOfThePipe}"
                        kit_order_tv_4.text = "Толщина стенки трубы, (мм): ${spec.pipeWallThickness}"
                        kit_order_tv_5.text = "Тип резьбы: ${spec.refTypeThreadTitle}"
                        kit_order_tv_6.text = "O.D. Замка ниппель (мм): ${spec.odlockNipple}"
                        kit_order_tv_7.text = "I.D. Замка ниппель (мм): ${spec.idlockNipple}"
                        kit_order_tv_8.text = "Длина трубы: ${spec.pipeLength}"
                        kit_order_tv_9.text = "Угол заплетчика: ${spec.shoulderAngle}"
                        kit_order_tv_10.text ="Длина под ключ ниппель, (мм): ${ spec.turnkeyLengthNipple}"
                        kit_order_tv_11.text ="Длина под ключ муфта, (мм): ${ spec.turnkeyLengthCoupling}"
                        kit_order_tv_12.text ="Покрытие резьбы: ${ spec.refThreadCoatingTitle}"
                        kit_order_tv_13.text ="Внутреннее покрытие: ${ spec.refInnerCoatingTitle}"
                        kit_order_tv_14.text ="Хардбендинг (муфта): ${ spec.refHardbandingCouplingTitle}"
                        kit_order_tv_15.text ="Комментарий: ${ spec.comment}"

                        kitOrderViewModel.findAddCardByKitId(savedData.id).observe(this, Observer {
                            kit_order_added_rv.adapter = KitDetailAddCardAdapter(
                                it as ArrayList<KitOrderAddCardEntity>,
                                false,
                                this
                            )
                        })
                        addCardsToKit(savedData.id)
                    })
            }else {
                kitOrderViewModel.findKitCards(savedData.id).observe(this, Observer {
                    kit_order_detail_rv.adapter = KitCardSavedAdapter(this, it as ArrayList<KitOrderCardEntity>)
                    Log.d("AddCardsCheck", "Добавленные карточки withCatalog $withKit}: ")
                    kit_order_detail_title.text = "Количество единиц оборудования: ${it.size}"
                    kit_order_add_btn.isVisible = false
                    kit_order_spec_card.isVisible = false
                })

            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun scanBtnClicked(model: KitOrderCardEntity) {
        currentModel = model
        createScanDialog(model, "")
    }

    private fun createScanDialog(model: KitOrderCardEntity, accessTag: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_scan, null)
        dialogBuilder.setView(view)
        scanDialog = dialogBuilder.create()

        view.scan_name.text = model.fullName

        view.scan_problem_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> view.scan_comment_out.visibility = View.VISIBLE
                false -> view.scan_comment_out.visibility = View.GONE
            }
        }
        view.scan_result_et.setText(accessTag)
        //Сохранение карточки
        view.scan_access_btn.setOnClickListener {

            if (accessTag.isNotEmpty()) {
                if (MyUtil().equalsNoSpace(model.rfidTagNo!!, accessTag)) {
                    kitOrderViewModel.kitOrderCardConfirm(model.id, true)
                    toast("Подтверждён")
                    scanDialog.dismiss()
                } else {
                    kitOrderViewModel.kitOrderCardConfirm(model.id, false)
                    toast("Не совпадает!")
                    scanDialog.dismiss()
                }

            } else {
                if (view.scan_comment_et.text.toString().isNotEmpty()) {
                    kitOrderViewModel.updateProblemCommentKitCard(
                        model.id,
                        view.scan_comment_et.text.toString()
                    )
                    toast("Успешно сохранён!")
                    scanDialog.dismiss()
                } else {
                    view.scan_comment_et.error = "Не может быть пустым"
                }
            }

        }

        //Чтение со сканера
        view.scan_scanner.setOnClickListener {
            RfidScannerUtil(this).run {
                isCancelable = false
                show(supportFragmentManager, "RfidScannerUtil")
            }
        }

        view.scan_negative_btn.setOnClickListener {
            scanDialog.dismiss()
        }

        scanDialog.show()
    }

    override fun onAccessScan(tag: String) {
        scanDialog.dismiss()
        createScanDialog(currentModel, tag)
    }

    private fun addCardsToKit(kitId: Int) {
        kit_order_add_btn.setOnClickListener {
            KitOrderAddCardBS(kitId, savedData.taskId).run {
                show(supportFragmentManager, "KitOrderAddCardBS")
            }
        }
    }

    private fun initAccounting(boolean: Boolean): Int {
        return if (boolean) {
            1
        } else {
            0
        }
    }

    override fun editBtnClicked(item: KitOrderAddCardEntity) {
        KitOrderEditCardBS(savedData.id, item).show(supportFragmentManager, "KitOrderEditCardBS")
    }

    override fun deleteBtnClicked(cardId: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_add, null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()

        view.add_tv.text = "Вы хотите удалить?"
        view.add_positive_btn.setOnClickListener {
            kitOrderViewModel.deleteKitOrderAddCard(cardId)
            toast("Удалено")
            alertDialog.dismiss()
        }

        view.add_negative_btn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}