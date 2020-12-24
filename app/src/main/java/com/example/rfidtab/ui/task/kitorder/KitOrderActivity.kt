package com.example.rfidtab.ui.task.kitorder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.kit.KitOrderOnlineAdapter
import com.example.rfidtab.adapter.kitorder.kit.KitOrderOnlineListener
import com.example.rfidtab.adapter.kitorder.kit.KitOrderSavedAdapter
import com.example.rfidtab.adapter.kitorder.kit.KitOrderSavedListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderKitEntity
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
import org.koin.androidx.viewmodel.ext.android.viewModel

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
            task_kit_save_btn.isVisible = true
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
                        kit_card_counter.text = "Всего единиц оборудования для комплектации в аренду: ${allCardCount(data?.kitType, data?.kitCardCount)}"
                        kit_order_send_btn.visibility = View.GONE
                        kit_order_kits_rv.adapter = KitOrderOnlineAdapter(this, data?.kits as ArrayList<KitOrderKit>)
                    }
                    Status.ERROR -> {
                        toast(msg)
                    }
                    Status.NETWORK -> {
                        toast(msg)
                    }
                }
            })
            task_kit_save_btn.setOnClickListener {

            }

        } else {
            savedData = intent.getSerializableExtra("data") as KitOrderEntity
            task_kit_save_btn.isVisible = false
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
                        kitOrderViewModel.findKitCards(kit.id)
                            .observe(this, Observer { networkCards ->
                                if (networkCards.isEmpty()) {
                                    val createdCards = ArrayList<KitOrderCards>()
                                    // без каталога
                                    // Если карчтоки пусты значит с тех заданием, без карточек
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
                                                                        kitOrderViewModel.deleteAllKitOrderAddCards(kit.id)
                                                                        kitOrderViewModel.deleteKitTaskById(savedData.id)
                                                                        kitOrderViewModel.deleteAllKitOrderCards(savedData.id)
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

    private fun allCardCount(kitType: String?, kitCardCount: String?): String {
        val array = ArrayList<Int>()
        var result = 0
        array.add(0)
        if (kitType == "multi") {
            kitCardCount?.forEachIndexed { i: Int, c: Char ->
                if (c == ',') {
                    array.add(0)
                }
            }
            array.forEachIndexed { index, i ->
                result += kitCardCount!!.split(",")[index].toInt()
            }
        }
        return result.toString()
    }

}