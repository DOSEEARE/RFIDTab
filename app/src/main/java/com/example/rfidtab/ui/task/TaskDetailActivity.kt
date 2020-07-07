package com.example.rfidtab.ui.task

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.taskDetail.TaskDetailListener
import com.example.rfidtab.adapter.taskDetail.TaskDetailOnlineAdapter
import com.example.rfidtab.adapter.taskDetail.TaskDetailSavedAdapter
import com.example.rfidtab.extension.loadingHide
import com.example.rfidtab.extension.loadingShow
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.response.task.TaskCardResponse
import com.example.rfidtab.service.response.task.TaskResponse
import kotlinx.android.synthetic.main.activity_task_detail_activity.*
import kotlinx.android.synthetic.main.fragment_scan.*
import kotlinx.android.synthetic.main.fragment_scan.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class TaskDetailActivity : AppCompatActivity(), TaskDetailListener {
    private lateinit var onlineData: TaskResponse
    private lateinit var savedData: TaskResultEntity
    private var cardList = ArrayList<TaskCardListEntity>()
    private val viewModel: TaskViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail_activity)
        supportActionBar?.title = "Задание"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
        sendToCheck()
    }


    private fun initViews() {
        val isOnline = intent.getBooleanExtra("isOnline", true)

        if (isOnline) {
            val model = intent.getParcelableExtra<TaskResponse>("data")
            onlineData = model
            task_detail_createdby.text = "Постановщик: ${onlineData.createdByFio}"
            task_detail_executor.text = "Исполнитель: ${onlineData.executorFio}"
            task_detail_status.text = "Статус: ${onlineData.statusTitle}"
            task_detail_send_btn.visibility = View.GONE
            task_detail_rv.adapter =
                TaskDetailOnlineAdapter(onlineData.cardList as ArrayList<TaskCardResponse>)
        } else {
            val model = intent.getParcelableExtra<TaskResultEntity>("data")
            savedData = model

            task_detail_save_btn.visibility = View.GONE
            task_detail_createdby.text = "Постановщик: ${savedData.createdByFio}"
            task_detail_executor.text = "Исполнитель: ${savedData.executorFio}"
            task_detail_status.text = "Статус: ${savedData.statusTitle}"
            viewModel.findCardsById(savedData.id).observe(this, Observer {
                cardList = it as ArrayList<TaskCardListEntity>
                task_detail_rv.adapter =
                    TaskDetailSavedAdapter(this, it)
            })

        }

        task_detail_save_btn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val cards = ArrayList<TaskCardListEntity>()

                onlineData.cardList.forEachIndexed { index, taskCardList ->
                    val a = onlineData.cardList[index]
                    cards.add(
                        TaskCardListEntity(
                            Random.nextInt(0, 1000),
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
                            a.taskTypeId
                        )
                    )
                }
                val item = TaskWithCards(
                    TaskResultEntity(
                        onlineData.id,
                        onlineData.statusId,
                        onlineData.taskTypeId,
                        onlineData.statusTitle,
                        onlineData.taskTypeTitle,
                        onlineData.createdByFio,
                        onlineData.executorFio,
                        onlineData.comment
                    ), cards
                )
                viewModel.insertTaskToDb(item)

                withContext(Dispatchers.Main) {
                    toast("Успешно сохранён!")
                }
            }

        }

    }

    private fun sendToCheck() {
        task_detail_send_btn.setOnClickListener {
            loadingShow()
            cardList.forEach {
                val model = CardModel(
                    it.cardId,
                    it.taskTypeId,
                    it.rfidTagNo,
                    it.comment,
                    it.commentProblemWithMark
                )
                viewModel.changeCard(model)
                    .observe(this@TaskDetailActivity, Observer { result ->
                        val data = result.data
                        val msg = result.msg
                        when (result.status) {
                            Status.SUCCESS -> {
                                toast("Успешно!")
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

            viewModel.taskStatusChange(TaskStatusModel(savedData.id, savedData.taskTypeId, 5))
                .observe(this, Observer {
                        result ->
                    val data = result.data
                    val msg = result.msg
                    when (result.status) {
                        Status.SUCCESS -> {
                            loadingHide()
                            toast("$data")
                        }
                        Status.ERROR -> {
                            loadingHide()
                            toast("$data")
                        }
                        Status.NETWORK -> {
                            loadingHide()
                            toast("$data")

                        }
                        else -> {
                            loadingHide()
                            toast("$data")
                        }
                    }

                })

        }
    }

    override fun scantBtnClicked(model: TaskCardListEntity) {

        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.fragment_scan, null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()
        view.scan_name.text = model.fullName

        view.scan_access.setOnClickListener {
            if (view.scan_edit.text!!.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.updateCard(model.cardId, view.scan_edit.text.toString().toLong())
                    withContext(Dispatchers.Main) {
                        toast("Успешно сохранен!")
                        alertDialog.dismiss()
                    }
                }
            } else {
                scan_edit_out.error = "Пусто"
            }
        }

        view.scan_dismiss.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
