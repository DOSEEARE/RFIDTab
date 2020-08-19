package com.example.rfidtab.ui.kitorder

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.KitOrderDetailAdapter
import com.example.rfidtab.adapter.kitorder.KitOrderOnlineAdapter
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.kitorder.OrderCardEntity
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.enums.TaskStatusEnum
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import com.example.rfidtab.service.model.kitorder.OrderCardList
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.ui.task.TaskViewModel
import kotlinx.android.synthetic.main.activity_kit_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class KitOrderActivity : AppCompatActivity() {
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
                        kit_order_executor.text = "Исполнитель: ${data?.executorFio}"
                        kit_order_createdby.text = "Автор: ${data?.createdByFio}"
                        kit_order_status.text = "Статус: ${data?.statusTitle}"
                        kit_order_send_btn.visibility = View.GONE
                        kit_order_kits_rv.adapter = KitOrderOnlineAdapter(data?.cardList as ArrayList<OrderCardList>)
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

            kitOrderViewModel.findKitCards(savedData.id).observe(this, Observer {
                kit_order_kits_rv.adapter =
                    KitOrderDetailAdapter(it as ArrayList<OrderCardEntity>)
            })

            kit_order_send_btn.setOnClickListener {
                taskViewModel.taskStatusChange(
                    TaskStatusModel(
                        savedData.id,
                        TaskTypeEnum.kitForOder,
                        TaskStatusEnum.savedToLocal
                    )
                ).observe(this, Observer { result ->
                    val data = result.data
                    when (result.status) {
                        Status.SUCCESS -> {
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
            }


        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}