package com.example.rfidtab.ui.task

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
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
import com.example.rfidtab.service.model.TaskStatusEnum
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.response.task.TaskCardResponse
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.util.DataTransfer
import com.google.android.material.textfield.TextInputEditText
import com.senter.support.openapi.StUhf
import kotlinx.android.synthetic.main.activity_task_detail_activity.*
import kotlinx.android.synthetic.main.alert_add.view.*
import kotlinx.android.synthetic.main.alert_scan.*
import kotlinx.android.synthetic.main.alert_scan.view.*
import kotlinx.android.synthetic.main.alert_scan.view.add_negative_btn
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
    private var tag: String = String()


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
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.alert_add, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()

            view.add_positive_btn.setOnClickListener {
                if (changeTaskStatus(onlineData)) {
                    alertDialog.dismiss()
                }

            }

            view.add_negative_btn.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()

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

            viewModel.taskStatusChange(
                TaskStatusModel(
                    savedData.id,
                    savedData.taskTypeId,
                    TaskStatusEnum.createdEdited
                )
            )
                .observe(this, Observer { result ->
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
        val view = inflater.inflate(R.layout.alert_scan, null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()
        view.scan_name.text = model.fullName

        view.scan_access.setOnClickListener {
            if (tag.isNotEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.updateCard(model.cardId, tag)
                    withContext(Dispatchers.Main) {
                        toast("Успешно сохранен!")
                        alertDialog.dismiss()
                    }
                }
            } else {
                scan_edit_out.error = "Пусто"
            }
        }

        view.scan_scanner.setOnClickListener {
            readTag(view.scan_result_et)
        }

        view.add_negative_btn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

//изменить статус
    private fun changeTaskStatus(model: TaskResponse): Boolean {
        var isSucces = false
        viewModel.taskStatusChange(
            TaskStatusModel(
                model.id,
                model.taskTypeId,
                TaskStatusEnum.takenForExecution
            )
        ).observe(this, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    isSucces = true
                    toast("Вы взяли задание на исполнение")
                    saveItemToDb(model)
                }
                Status.ERROR -> {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(this, "Проблемы с интернетом", Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_LONG).show()
                }
            }

        })
        return isSucces
    }

    //сохранить задачи на кнопку сохранить
    private fun saveItemToDb(model: TaskResponse) {
        CoroutineScope(Dispatchers.IO).launch {
            val cards = ArrayList<TaskCardListEntity>()

            model.cardList.forEachIndexed { index, taskCardList ->
                val a = model.cardList[index]
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
                    model.id,
                    model.statusId,
                    model.taskTypeId,
                    model.statusTitle,
                    model.taskTypeTitle,
                    model.createdByFio,
                    model.executorFio,
                    model.comment
                ), cards
            )
            viewModel.insertTaskToDb(item)

            withContext(Dispatchers.Main) {
                toast("Успешно сохранён!")
            }
        }
    }

    // чтение RFID метки на кнопку скан
    private fun readTag(view: TextInputEditText) {
        val bank = StUhf.Bank.TID
        val ptr = 0
        val cnt = 1
        val pwd = "00000000"
        val acsPass = StUhf.AccessPassword.getNewInstance(DataTransfer.getBytesByHexString(pwd))

        val iso18k6c: StUhf.InterrogatorModelDs.UmdOnIso18k6cRead =
            object : StUhf.InterrogatorModelDs.UmdOnIso18k6cRead() {
                override fun onFailed(error: StUhf.InterrogatorModelDs.UmdErrorCode) {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Нет метки!!!", Toast.LENGTH_LONG).show()
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
                            view.setText(tag)
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


        /*} catch (e: Exception) {
            Toast.makeText(this, "Нет метки!", Toast.LENGTH_SHORT).show()
            Log.w("RFID SCANNER", e.message)
        }*/
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
