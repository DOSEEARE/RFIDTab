package com.example.rfidtab.ui.task.kitorder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.kit.KitOrderOnlineAdapter
import com.example.rfidtab.adapter.kitorder.kit.KitOrderOnlineListener
import com.example.rfidtab.adapter.kitorder.kit.KitOrderSavedAdapter
import com.example.rfidtab.adapter.kitorder.kit.KitOrderSavedListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.kitorder.*
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.confirm.ConfirmCardModel
import com.example.rfidtab.service.model.confirm.ConfirmCards
import com.example.rfidtab.service.model.enums.TaskStatusEnum
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import com.example.rfidtab.service.model.kitorder.KitOrderCards
import com.example.rfidtab.service.model.kitorder.KitOrderModel
import com.example.rfidtab.service.response.kitorder.KitOrderKit
import com.example.rfidtab.service.response.kitorder.KitOrderResponse
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.ui.task.TaskActivity
import com.example.rfidtab.ui.task.TaskViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kit_order.*
import kotlinx.android.synthetic.main.alert_add.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class KitOrderActivity : AppCompatActivity(),
    KitOrderOnlineListener, KitOrderSavedListener {
    private val taskViewModel: TaskViewModel by viewModel()
    private val kitOrderViewModel: KitOrderViewModel by viewModel()

    private lateinit var onlineData: TaskResponse
    private lateinit var savedData: KitOrderEntity
    private lateinit var kitOrder : KitOrderResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kit_order)
        supportActionBar?.title = "Комплектация в аренду"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
    }

    private fun initViews() {
        val isOnline = intent.getBooleanExtra("isOnline", true)
        if (isOnline) {
            onlineData = intent.getSerializableExtra("data") as TaskResponse
            Log.d("AddCardsCheck", "Добавленные карточки айди задании ${Gson().toJson(onlineData.id)}:")
            kitOrderViewModel.kitOrder(onlineData.id).observe(this, Observer { result ->
                val data = result.data
                val msg = result.msg
                when (result.status) {
                    Status.SUCCESS -> {
                        kitOrder = data!!
                        kit_order_executor.text = "Исполнитель: ${data?.executorFio}"
                        kit_order_createdby.text = "Автор: ${data?.createdByFio}"
                        kit_order_status.text = "Статус: ${data?.statusTitle}"
                        kit_comment.text = "Комментарии: ${data?.comment}"
                        kit_card_counter.text =
                            "Всего единиц оборудования для комплектации в аренду: ${allCardCount(
                                data?.kitType, data?.kitCardCount, data.cardCount
                            )}"
                        kit_order_send_btn.visibility = View.GONE
                        kit_order_kits_rv.adapter =
                            KitOrderOnlineAdapter(this, data?.kits as ArrayList<KitOrderKit>)
                    }
                    Status.ERROR -> {
                        toast(msg)
                    }
                    Status.NETWORK -> {
                        toast(msg)
                    }
                }
            })
            kit_order_save_btn.isVisible = true
            kit_order_save_btn.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(this)
                val inflater = this.layoutInflater
                val view = inflater.inflate(R.layout.alert_add, null)
                dialogBuilder.setView(view)
                val alertDialog = dialogBuilder.create()
                view.add_positive_btn.setOnClickListener {
                    saveKitOrder(onlineData)
                    alertDialog.dismiss()
                }

                view.add_negative_btn.setOnClickListener {
                    alertDialog.dismiss()
                }

                alertDialog.show()
            }

        } else {
            savedData = intent.getSerializableExtra("data") as KitOrderEntity
            kit_order_executor.text = "Исполнитель: ${savedData?.executorFio}"
            kit_order_createdby.text = "Автор: ${savedData?.createdByFio}"
            kit_order_status.text = "Статус: ${savedData?.statusTitle}"
            kit_comment.text = "Комментарии: ${savedData?.comment}"
            kit_card_counter.text = "Всего единиц оборудования для комплектации в аренду: ${savedData?.cardCount}"
            kitOrderViewModel.findKitItem(savedData.id).observe(this, Observer {
                kit_order_kits_rv.adapter =
                    KitOrderSavedAdapter(this, it as ArrayList<KitOrderKitEntity>)
            })

            kit_order_send_btn.setOnClickListener {
                kitOrderViewModel.findKitItem(savedData.id).observe(this, Observer {
                    var model: KitOrderModel
                    it.forEach { kit ->
                        Log.d("insertKitOrder", "delete add cards kit id : ${kit.id}")
                        kitOrderViewModel.findKitCards(kit.id)
                            .observe(this, Observer { networkCards ->
                                if (savedData.withKit == "withoutCatalog") {
                                    val createdCards = ArrayList<KitOrderCards>()
                                    // без каталога
                                    kitOrderViewModel.findAddCardByKitId(kit.id)
                                        .observe(this, Observer { addedCards ->
                                            addedCards.forEach {
                                                createdCards.add(
                                                    KitOrderCards(
                                                        it.id,
                                                        it.pipeSerialNumber,
                                                        it.couplingSerialNumber,
                                                        it.serialNoOfNipple,
                                                        it.rfidTagNo,
                                                        it.comment,
                                                        it.comment
                                                    )
                                                )
                                            }

                                            model = KitOrderModel(kit.id, createdCards)

                                            val modelJson = Gson().toJson(model)
                                            Log.d("AddCardsCheck", "Добавленные карточки при отправке $modelJson")
                                            kitOrderViewModel.sendKitOrderCards(model).observe(this, Observer { result ->
                                                when (result.status) {
                                                    Status.SUCCESS -> {
                                                            taskViewModel.taskStatusChange(
                                                                TaskStatusModel(
                                                                    savedData.id,
                                                                    TaskTypeEnum.kitForOder,
                                                                    TaskStatusEnum.savedToLocal
                                                                )
                                                            ).observe(this, Observer { result ->
                                                                when (result.status) {
                                                                    Status.SUCCESS -> {
                                                                        toast("Успешно отправлен!")
                                                                        kitOrderViewModel.deleteKitTaskById(savedData.id)
                                                                        kitOrderViewModel.deleteAllKitOrderCards(savedData.id)
                                                                        kitOrderViewModel.deleteAllKitOrderAddCards(savedData.id)
                                                                        startActivity(Intent(this, TaskActivity::class.java))
                                                                        finish()
                                                                    }
                                                                }
                                                            })
                                                        toast("Успешно добавлен в комплект!")
                                                    }
                                                    Status.ERROR -> {
                                                        toast("Карточка уже в комплекте!")
                                                    }
                                                    Status.NETWORK -> {
                                                        toast("Карточка уже в комплекте!")
                                                    }
                                                }
                                            })
                                        })
                                } else {
                                    // c каталога
                                    //С корточками, без тех задании
                                    val body: ConfirmCardModel
                                    val confirmCard = ArrayList<ConfirmCards>()
                                    val cards = ArrayList<KitOrderCards>()

                                    networkCards.forEach {
                                        cards.add(
                                            KitOrderCards(
                                                it.id,
                                                it.pipeSerialNumber,
                                                it.couplingSerialNumber,
                                                it.serialNoOfNipple,
                                                it.rfidTagNo,
                                                it.comment,
                                                it.problemComment
                                            )
                                        )
                                        model = KitOrderModel(kit.id, cards)

                                        kitOrderViewModel.sendKitOrderCards(model).observe(this, Observer { result ->
                                            when (result.status) {
                                                Status.SUCCESS -> {

                                                }
                                            }
                                        })

                                        if (it.isConfirmed) {
                                            confirmCard.add(ConfirmCards(it.rfidTagNo))
                                        }
                                    }

                                        //comment
                                    body = ConfirmCardModel(savedData.id, confirmCard)
                                    kitOrderViewModel.confirmCards(body).observe(this, Observer { result ->
                                        when (result.status) {
                                            Status.SUCCESS -> {
                                                //Измнетть статус задании
                                                    taskViewModel.taskStatusChange(
                                                        TaskStatusModel(
                                                            savedData.id,
                                                            TaskTypeEnum.kitForOder,
                                                            TaskStatusEnum.savedToLocal
                                                        )
                                                    ).observe(this, Observer { result ->
                                                        when (result.status) {
                                                            Status.SUCCESS -> {
                                                                toast("Успешно отправлен!")
                                                                kitOrderViewModel.deleteKitTaskById(savedData.id)
                                                                kitOrderViewModel.deleteAllKitOrderCards(savedData.id)
                                                                startActivity(Intent(this, TaskActivity::class.java))
                                                                finish()
                                                            }
                                                        }
                                                    })
                                            }
                                            Status.ERROR -> {
                                                toast("Карточка уже в комплекте!")
                                            }
                                            Status.NETWORK -> {
                                                toast("Карточка уже в комплекте!")
                                            }
                                        }
                                    })

                                }

                            })
                    }
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

    override fun onOnlineKitClicked(model: KitOrderKit, position : Int) {
        val json = Gson().toJson(model)
        Log.e("kow", json)
        val detailIntent = Intent(this, KitOrderDetailActivity::class.java)
        detailIntent.putExtra("data", model)
        detailIntent.putExtra("isOnline", true)
        detailIntent.putExtra("kitPosition", position)
        detailIntent.putExtra("taskId", onlineData.id)
        detailIntent.putExtra("withKit", kitOrder.withKit)
        startActivity(detailIntent)
    }

    override fun onSavedKitClicked(model: KitOrderKitEntity, position: Int) {
        val detailIntent = Intent(this, KitOrderDetailActivity::class.java)
        detailIntent.putExtra("data", model)
        detailIntent.putExtra("isOnline", false)
        detailIntent.putExtra("kitPosition", position)
        detailIntent.putExtra("taskId", savedData.id)
        detailIntent.putExtra("withKit", savedData.withKit)
        startActivity(detailIntent)
    }

    private fun allCardCount(kitType: String?, kitCardCount: String?, cardCount: String?): String {
        val array = ArrayList<Int>()
        var result = 0
        array.add(0)
        return if (kitType == "multi") {
            kitCardCount?.forEachIndexed { i: Int, c: Char ->
                if (c == ',') {
                    array.add(0)
                }
            }
            array.forEachIndexed { index, i ->
                result += kitCardCount!!.split(",")[index].toInt()
            }
            result.toString()
        } else {
            cardCount!!
        }
    }

    private fun saveKitOrder(model: TaskResponse) {
        kitOrderViewModel.kitOrder(model.id).observe(this, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        val entity = KitOrderEntity(
                            data!!.id,
                            AppPreferences.userLogin,
                            data.mainReason,
                            data.tenantName,
                            data.createAt,
                            data.comment,
                            TaskStatusEnum.takenForExecutionString,
                            TaskStatusEnum.takenForExecution.toString(),
                            data.createdByFio,
                            data.executorFio,
                            data.kitCount,
                            data.withKit,
                            allCardCount(data.kitType, data.kitCardCount, data.cardCount),
                            data.kitCardCount
                        )
                        val listCard = ArrayList<KitOrderCardEntity>()
                        val listKit = ArrayList<KitOrderKitEntity>()
                        val addedCardList = ArrayList<KitOrderAddCardEntity>()

                        data.kits.forEachIndexed { indexKit, kitOrderKit ->
                            listKit.add(
                                KitOrderKitEntity(
                                    kitOrderKit.id, model.id, kitOrderKit.title
                                )
                            )

                            kitOrderKit.cardListConfirmed.forEach {
                                addedCardList.add(
                                    KitOrderAddCardEntity(
                                        Random.nextInt(),
                                        serialNoOfNipple = it.serialNoOfNipple,
                                        pipeSerialNumber = it.pipeSerialNumber,
                                        couplingSerialNumber = it.couplingSerialNumber,
                                        rfidTagNo = it.rfidTagNo,
                                        comment = it.comment,
                                        accounting = initAccounting(it.accounting),
                                        kitId = kitOrderKit.id,
                                        taskId = model.id
                                    )
                                )
                            }

                            kitOrderKit.cardListNotConfirmed.forEach {
                                addedCardList.add(
                                    KitOrderAddCardEntity(
                                        Random.nextInt(),
                                        serialNoOfNipple = it.serialNoOfNipple,
                                        pipeSerialNumber = it.pipeSerialNumber,
                                        couplingSerialNumber = it.couplingSerialNumber,
                                        rfidTagNo = it.rfidTagNo,
                                        comment = it.comment,
                                        accounting = initAccounting(it.accounting),
                                        kitId = kitOrderKit.id,
                                        taskId = model.id
                                    )
                                )
                            }

                            kitOrderKit.cards.forEachIndexed { indexCard, kitOrderCard ->
                                listCard.add(
                                    KitOrderCardEntity(
                                        kitOrderCard.id,
                                        model.id,
                                        kitOrderKit.id,
                                        kitOrderCard.rfidTagNo,
                                        kitOrderCard.pipeSerialNumber,
                                        kitOrderCard.serialNoOfNipple,
                                        kitOrderCard.couplingSerialNumber,
                                        kitOrderCard.fullName,
                                        kitOrderCard.comment,
                                        " ",
                                        false
                                    )
                                )
                            }
                            Log.d("insertKitOrder", "insertKitOrderSpec withKit: ${data.withKit}")
                            if (data.withKit == "withoutCatalog") {
                                val spec = data.kits[indexKit].specification
                                if (data.kitType == "multi" && data.kitCardCount!!.length > 1) {
                                    val kitCardCount: String =
                                        data.kitCardCount.split(",")[indexKit]

                                    val kitOrderSpec = KitOrderSpecificationEntity(
                                        id = spec.id,
                                        kitId = kitOrderKit.id,
                                        outerDiameterOfThePipe = spec.outerDiameterOfThePipe,
                                        pipeWallThickness = spec.pipeWallThickness,
                                        odlockNipple = spec.odlockNipple,
                                        idlockNipple = spec.idlockNipple,
                                        pipeLength = spec.pipeLength,
                                        shoulderAngle = spec.shoulderAngle,
                                        turnkeyLengthNipple = spec.turnkeyLengthNipple,
                                        turnkeyLengthCoupling = spec.turnkeyLengthCoupling,
                                        refTypeEquipmentTitle = spec.refTypeEquipmentTitle,
                                        refTypeDisembarkationTitle = spec.refTypeDisembarkationTitle,
                                        refTypeThreadTitle = spec.refTypeThreadTitle,
                                        refInnerCoatingTitle = spec.refInnerCoatingTitle,
                                        refThreadCoatingTitle = spec.refThreadCoatingTitle,
                                        refHardbandingCouplingTitle = spec.refHardbandingCouplingTitle,
                                        refHardbandingCoupling = spec.refHardbandingCoupling?.value,
                                        refInnerCoating = spec.refInnerCoating?.value,
                                        refThreadCoating = spec.refThreadCoating?.value,
                                        refTypeDisembarkation = spec.refTypeDisembarkation?.value,
                                        refTypeEquipment = spec.refTypeEquipment?.value,
                                        refTypeThread = spec.refTypeThread?.value,
                                        comment = spec.comment,
                                        cardCount = kitCardCount
                                    )
                                    Log.d(
                                        "insertKitOrder", "insertKitOrderSpec ID: ${kitOrderKit.id}"
                                    )
                                    kitOrderViewModel.insertKitOrderSpec(kitOrderSpec)
                                } else {
                                    val kitOrderSpec = KitOrderSpecificationEntity(
                                        id = spec.id,
                                        kitId = kitOrderKit.id,
                                        outerDiameterOfThePipe = spec.outerDiameterOfThePipe,
                                        pipeWallThickness = spec.pipeWallThickness,
                                        odlockNipple = spec.odlockNipple,
                                        idlockNipple = spec.idlockNipple,
                                        pipeLength = spec.pipeLength,
                                        shoulderAngle = spec.shoulderAngle,
                                        turnkeyLengthNipple = spec.turnkeyLengthNipple,
                                        turnkeyLengthCoupling = spec.turnkeyLengthCoupling,
                                        refTypeEquipmentTitle = spec.refTypeEquipmentTitle,
                                        refTypeDisembarkationTitle = spec.refTypeDisembarkationTitle,
                                        refTypeThreadTitle = spec.refTypeThreadTitle,
                                        refInnerCoatingTitle = spec.refInnerCoatingTitle,
                                        refThreadCoatingTitle = spec.refThreadCoatingTitle,
                                        refHardbandingCouplingTitle = spec.refHardbandingCouplingTitle,
                                        refHardbandingCoupling = spec.refHardbandingCoupling?.value,
                                        refInnerCoating = spec.refInnerCoating?.value,
                                        refThreadCoating = spec.refThreadCoating?.value,
                                        refTypeDisembarkation = spec.refTypeDisembarkation?.value,
                                        refTypeEquipment = spec.refTypeEquipment?.value,
                                        refTypeThread = spec.refTypeThread?.value,
                                        comment = spec.comment,
                                        cardCount = data.cardCount!!
                                    )
                                    kitOrderViewModel.insertKitOrderSpec(kitOrderSpec)
                                }
                            }
                        }
                        kitOrderViewModel.insertKitOrderAddCardList(addedCardList)
                        kitOrderViewModel.insertKitOrder(entity)
                        kitOrderViewModel.insertKitItem(listKit)
                        kitOrderViewModel.insertKitCards(listCard)
                    }
                    saveTask(model)
                }
                Status.ERROR -> {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(this, "Проблемы с интернетом", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun initAccounting(boolean: Boolean): Int {
        return if (boolean) {
            1
        } else {
            0
        }
    }

    private fun saveTask(model: TaskResponse) {
        kitOrderViewModel.taskStatusChange(
            TaskStatusModel(model.id, model.taskTypeId, TaskStatusEnum.takenForExecution)
        ).observe(this, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    toast("Вы взяли задание на исполнение")
                    saveItemToDb(model)
                }
                Status.ERROR -> {
                    toast(msg)
                }
                Status.NETWORK -> {
                    toast("Проблемы с интернетом")
                }
            }


        })
    }

    private fun saveItemToDb(model: TaskResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            if (model.taskTypeId != TaskTypeEnum.kitForOder) {
                val cards = ArrayList<TaskCardListEntity>()

                model.cardList.forEachIndexed { index, taskCardList ->
                    val a = model.cardList[index]
                    cards.add(
                        TaskCardListEntity(
                            Random.nextInt(),
                            a.cardId,
                            a.fullName,
                            a.pipeSerialNumber,
                            a.serialNoOfNipple,
                            a.couplingSerialNumber,
                            a.rfidTagNo,
                            a.comment,
                            a.accounting,
                            a.commentProblemWithMark,
                            a.taskId,
                            a.taskTypeId,
                            a.sortOrder,
                            false,
                            a.cardImgRequired
                        )
                    )
                }

                val item = TaskWithCards(
                    TaskResultEntity(
                        model.id,
                        AppPreferences.userLogin,
                        TaskStatusEnum.takenForExecution,
                        model.taskTypeId,
                        "Принято на исполнение",
                        model.taskTypeTitle,
                        model.createdByFio,
                        model.executorFio,
                        model.comment
                    ), cards
                )
                kitOrderViewModel.insertTaskToDb(item)

                withContext(Dispatchers.Main) {
                    toast("Успешно сохранён!")
                }
            }
        }
    }


}