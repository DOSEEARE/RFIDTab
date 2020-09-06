package com.example.rfidtab.ui.kitorder

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.ui.task.TaskViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_kit_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call

class KitOrderActivity : AppCompatActivity(),
    KitOrderOnlineListener, KitOrderSavedListener {
    private val taskViewModel: TaskViewModel by viewModel()
    private val kitOrderViewModel: KitOrderViewModel by viewModel()

    private lateinit var onlineData: TaskResponse
    private lateinit var savedData: KitOrderEntity

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

            kitOrderViewModel.kitOrder(onlineData.id).observe(this, Observer { result ->
                val data = result.data
                when (result.status) {
                    Status.SUCCESS -> {
                        kit_order_executor.text = "Исплнитель: ${data?.executorFio}"
                        kit_order_createdby.text = "Автор: ${data?.createdByFio}"
                        kit_order_status.text = "Статус: ${data?.statusTitle}"
                        kit_order_send_btn.visibility = View.GONE
                        kit_order_kits_rv.adapter =
                            KitOrderOnlineAdapter(
                                this,
                                data?.kits as ArrayList<KitOrderKit>
                            )
                    }
                    Status.ERROR -> {
                        toast("Проблемы с интернетом!")
                    }
                    Status.NETWORK -> {
                        toast("Проблемы с интернетом!")
                    }
                    else -> {
                        toast("Неизвестная ошибка!")

                    }
                }

            })

        } else {
            savedData = intent.getSerializableExtra("data") as KitOrderEntity

            kit_order_executor.text = "Исполнитель: ${savedData?.executorFio}"
            kit_order_createdby.text = "Автор: ${savedData?.createdByFio}"
            kit_order_status.text = "Статус: ${savedData?.statusTitle}"

            kitOrderViewModel.findKitItem(savedData.id).observe(this, Observer {
                kit_order_kits_rv.adapter =
                    KitOrderSavedAdapter(this, it as ArrayList<KitOrderKitEntity>)
            })

            kit_order_send_btn.setOnClickListener {
                kitOrderViewModel.findKitItem(savedData.id).observe(this, Observer {
                    var model: KitOrderModel
                    val cardList = ArrayList<KitOrderCards>()
                    it.forEach { kit ->
                        kitOrderViewModel.findKitCards(kit.id).observe(this, Observer {networkCards ->
                            if (networkCards.isEmpty()) {
                                val createdCards = ArrayList<KitOrderCards>()
                                // Если карчтоки пусты значит с тех заданием, без карточек
                                kitOrderViewModel.findAddCardByKitId(kit.id).observe(this, Observer { addedCards ->
                                    addedCards.forEach {
                                        createdCards.add(KitOrderCards(it.pipeSerialNumber, it.couplingSerialNumber, it.serialNoOfNipple, it.rfidTagNo))
                                    }
                                    model = KitOrderModel(kit.id, createdCards)
                                    val log = Gson().toJson(model)
                                    Log.e("SUKA BLYA", log)
                                    kitOrderViewModel.sendKitOrderCards(model).observe(this, Observer { result ->
                                        val data = result.data
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
                                                            kitOrderViewModel.deleteKitTaskById(savedData.id)
                                                            toast("Успешно отправлен!")
                                                        }
                                                        Status.ERROR -> {
                                                            toast("Проблемы с интернетом!")
                                                        }
                                                        Status.NETWORK -> {
                                                            toast("Проблемы с интернетом!")
                                                        }
                                                        else -> {
                                                            toast("Неизвестная ошибка!")

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
                                            else -> {
                                                toast("Неизвестная ошибка!")
                                            }
                                        }

                                    })

                                })
                            } else {
                                //С корточками, без тех задании
                                val body: ConfirmCardModel
                                val confirmCard = ArrayList<ConfirmCards>()

                                networkCards.forEach {
                                    if (it.isConfirmed) {
                                        confirmCard.add(ConfirmCards(it.rfidTagNo))
                                    }
                                }
                                body = ConfirmCardModel(savedData.id, confirmCard)
                                kitOrderViewModel.confirmCards(body).observe(this, Observer { result ->
                                    val data = result.data
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
                                                        kitOrderViewModel.deleteKitTaskById(savedData.id)
                                                        toast("Успешно отправлен!")
                                                    }
                                                    Status.ERROR -> {
                                                        toast("Проблемы с интернетом!")
                                                    }
                                                    Status.NETWORK -> {
                                                        toast("Проблемы с интернетом!")
                                                    }
                                                    else -> {
                                                        toast("Неизвестная ошибка!")

                                                    }
                                                }
                                            })
                                            toast("Подтверждены!!")
                                        }
                                        Status.ERROR -> {
                                            toast("Карточка уже в комплекте!")
                                        }
                                        Status.NETWORK -> {
                                            toast("Карточка уже в комплекте!")
                                        }
                                        else -> {
                                            toast("Неизвестная ошибка!")

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

    override fun onOnlineKitClicked(model: KitOrderKit) {
        val json = Gson().toJson(model)
        Log.e("kow", json)
        val detailIntent = Intent(this, KitOrderDetailActivity::class.java)
        detailIntent.putExtra("data", model)
        detailIntent.putExtra("isOnline", true)
        startActivity(detailIntent)
    }

    override fun onSavedKitClicked(model: KitOrderKitEntity) {
        val detailIntent = Intent(this, KitOrderDetailActivity::class.java)
        detailIntent.putExtra("data", model)
        detailIntent.putExtra("isOnline", false)
        startActivity(detailIntent)
    }


}