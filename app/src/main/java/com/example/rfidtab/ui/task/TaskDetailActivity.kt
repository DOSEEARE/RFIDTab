package com.example.rfidtab.ui.task

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.example.rfidtab.BuildConfig
import com.example.rfidtab.R
import com.example.rfidtab.adapter.taskDetail.TaskDetailListener
import com.example.rfidtab.adapter.taskDetail.TaskDetailOnlineAdapter
import com.example.rfidtab.adapter.taskDetail.TaskDetailOverAdapter
import com.example.rfidtab.adapter.taskDetail.TaskDetailSavedAdapter
import com.example.rfidtab.extension.loadingHide
import com.example.rfidtab.extension.loadingShow
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.task.*
import com.example.rfidtab.service.model.CardModel
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.enums.TaskStatusEnum
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import com.example.rfidtab.service.model.overlist.OverCards
import com.example.rfidtab.service.model.overlist.TaskOverCards
import com.example.rfidtab.service.response.task.TaskCardResponse
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.ui.task.fragment.TaskAddOverBS
import com.example.rfidtab.util.MyUtil
import com.example.rfidtab.util.scanrfid.RfidScannerListener
import com.example.rfidtab.util.scanrfid.RfidScannerUtil
import kotlinx.android.synthetic.main.activity_task_detail.*
import kotlinx.android.synthetic.main.activity_task_detail.task_detail_createdby
import kotlinx.android.synthetic.main.activity_task_detail.task_detail_executor
import kotlinx.android.synthetic.main.activity_task_detail.task_detail_rv
import kotlinx.android.synthetic.main.activity_task_detail.task_detail_save_btn
import kotlinx.android.synthetic.main.activity_task_detail.task_detail_send_btn
import kotlinx.android.synthetic.main.activity_task_detail.task_detail_status
import kotlinx.android.synthetic.main.activity_task_detail.task_detail_type
import kotlinx.android.synthetic.main.activity_task_mark_inventory.task_detail_add_over
import kotlinx.android.synthetic.main.activity_task_mark_inventory.task_detail_over_rv
import kotlinx.android.synthetic.main.alert_add.view.*
import kotlinx.android.synthetic.main.alert_scan.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class TaskDetailActivity : AppCompatActivity(), TaskDetailListener, RfidScannerListener {
    private lateinit var onlineData: TaskResponse
    private lateinit var savedData: TaskResultEntity
    private var cardList = ArrayList<TaskCardListEntity>()
    private val viewModel: TaskViewModel by viewModel()
    private lateinit var filePath: File
    private var CAMERA_REQUEST_CODE = 1
    private var cardId = 0
    private var mTaskId = 0
    private lateinit var currentCardEntity: TaskCardListEntity
    private lateinit var scanDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)
        supportActionBar?.title = "Задание"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MyUtil().askPermissionForCamera(this, CAMERA_REQUEST_CODE)
        initViews()
        sendToCheck()
    }

    // 1 активити на Online и Saved здесь идёт определение
    // telegram: @doseeare. If u have some problem, i can help you.

    private fun initViews() {
        val isOnline = intent.getBooleanExtra("isOnline", true)

        if (isOnline) {
            val model = intent.getSerializableExtra("data") as TaskResponse
            onlineData = model
            task_detail_createdby.text = "Постановщик: ${onlineData.createdByFio}"
            task_detail_executor.text = "Исполнитель: ${onlineData.executorFio}"
            task_detail_status.text = "Статус: ${onlineData.statusTitle}"
            task_detail_type.text = "Тип задания: ${onlineData.taskTypeTitle}"
            task_detail_send_btn.visibility = View.GONE
            task_detail_rv.adapter =
                TaskDetailOnlineAdapter(onlineData.cardList as ArrayList<TaskCardResponse>)
        } else {
            val model = intent.getParcelableExtra<TaskResultEntity>("data")
            savedData = model

            if (savedData.taskTypeId == TaskTypeEnum.inventory) {
                initOverCards()
                task_detail_add_over.visibility = View.VISIBLE
                task_detail_add_over.setOnClickListener {
                    addOverCards()
                }
            }

            task_detail_save_btn.visibility = View.GONE
            task_detail_createdby.text = "Постановщик: ${savedData.createdByFio}"
            task_detail_executor.text = "Исполнитель: ${savedData.executorFio}"
            task_detail_status.text = "Статус: ${savedData.statusTitle}"
            task_detail_type.text = "Тип задания: ${savedData.taskTypeTitle}"

            viewModel.findCardsById(savedData.id).observe(this, Observer {
                cardList = it as ArrayList<TaskCardListEntity>
                task_detail_rv.adapter = TaskDetailSavedAdapter(this, savedData.taskTypeId, it)

                viewModel.getConfirmedCardCount(savedData.id).observe(this, Observer { confirmed ->
                    task_detail_counter.text = "Подтверждено $confirmed/${it.size}"

                })

            })

        }

        task_detail_save_btn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.alert_add, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()

            alertDialog.setCancelable(false)

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
        var cardListIndex = 0
        task_detail_send_btn.setOnClickListener {
            loadingShow()
            //Изменение карточек по циклу
            cardList.forEach { card ->
                cardListIndex++
                val model = CardModel(
                    card.cardId,
                    savedData.id,
                    card.taskTypeId,
                    card.rfidTagNo,
                    accounting(card.isConfirmed, card.commentProblemWithMark),
                    card.comment,
                    card.commentProblemWithMark
                )
                viewModel.changeCard(model)
                    .observe(this@TaskDetailActivity, Observer { result ->
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

                when (savedData.taskTypeId) {
                    TaskTypeEnum.inspection, TaskTypeEnum.kitForFix -> sendCardsImages(card.cardId, card.taskId, card.taskTypeId)
                }
            }
            // После цикла измнение статуса
            sendOverCards()

        }

    }

    private fun sendCardsImages(cardId: Int, taskId: Int, taskTypId: Int) {
        loadingShow()
        viewModel.findImagesById(cardId, taskId).observe(this, Observer {
            if (it.isNotEmpty()) {
                viewModel.sendImage(it, cardId, taskTypId, taskId)
                    .observe(this, Observer { result ->
                        val msg = result.msg
                        when (result.status) {
                            Status.SUCCESS -> {
                                toast("Фото отправлен!")
                                CoroutineScope(Dispatchers.IO).launch {
                                    viewModel.deleteTaskById(savedData.id)
                                    viewModel.deleteCardsById(savedData.id)
                                }
                                viewModel.taskStatusChange(
                                    TaskStatusModel(
                                        savedData.id,
                                        savedData.taskTypeId,
                                        TaskStatusEnum.savedToLocal
                                    )
                                ).observe(this, Observer { result ->
                                    val data = result.data
                                    when (result.status) {
                                        Status.SUCCESS -> {
                                            toast("$data")
                                            startActivity(Intent(this, TaskActivity::class.java))
                                            finish()
                                            loadingHide()
                                        }
                                    }

                                })
                            }
                            Status.ERROR -> {
                                toast(msg)
                                loadingHide()
                            }
                            Status.NETWORK -> {
                                toast(msg)
                                loadingHide()
                            }
                            else -> {
                                toast(msg)
                                }
                            }

                        })


            }
        })

    }

    //Сканирование Rfid метки
    override fun scantBtnClicked(model: TaskCardListEntity) {
        currentCardEntity = model
        createScanDialog(model, "")
    }

    private fun createScanDialog(model: TaskCardListEntity, accessTag: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_scan, null)
        dialogBuilder.setView(view)
        scanDialog = dialogBuilder.create()

        view.scan_name.text = model.fullName

        view.scan_result_et.setText(accessTag)

        //Сохранение карточки
        view.scan_access_btn.setOnClickListener {
            if (accessTag.isNotEmpty()) {
                if (MyUtil().equalsNoSpace(model.rfidTagNo!!, accessTag)) {
                    viewModel.updateConfirmTaskCard(model.cardId, true)
                    toast("Подтверждён!")
                    scanDialog.dismiss()
                } else {
                    toast("Не совпадает!")
                    scanDialog.dismiss()
                }

            } else {
                if (view.scan_comment_et.text.toString().isNotEmpty()) {
                    viewModel.updateErrorComment(model.cardId, view.scan_comment_et.text.toString())
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
            //readTag(view.scan_result_et, view.scan_comment_out)
        }

        view.scan_negative_btn.setOnClickListener {
            scanDialog.dismiss()
        }
        //тагскан
        view.scan_problem_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            when (isChecked) {
                true -> view.scan_comment_out.visibility = View.VISIBLE
                false -> view.scan_comment_out.visibility = View.GONE
            }
        }

        scanDialog.show()

    }
    override fun cameraBtnClicked(model: TaskCardListEntity) {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        MyUtil().createCardFolder()
        filePath = File(
            Environment.getExternalStorageDirectory().path + "/RFID cards",
            "/card ${Calendar.getInstance().time}.jpg"
        )
        cardId = model.cardId
        mTaskId = savedData.id

        val uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", filePath)
        //      val uri = Uri.fromFile(filePath)

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    //detail card
    override fun cardClicked(model: TaskCardListEntity) {
        val intent = Intent(this, CardDetailActivity::class.java)
        intent.putExtra("model", model)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                toast("Фото добавлено!")
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.insertImage(
                        CardImagesEntity(
                            Random.nextInt(1, 100000),
                            cardId,
                            savedData.id,
                            filePath.absolutePath
                        )
                    )
                }
            } else {
                toast("Доступ к камере запрещён")
            }
        }
    }

    //изменить статус
    private fun changeTaskStatus(model: TaskResponse): Boolean {
        var isSuccess = false
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
                    isSuccess = true
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
        return isSuccess
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
                        a.taskTypeId,
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
            viewModel.insertTaskToDb(item)

            withContext(Dispatchers.Main) {
                toast("Успешно сохранён!")
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAccessScan(tag: String) {
        scanDialog.dismiss()
        createScanDialog(currentCardEntity, tag)
    }

    private fun initOverCards() {
        viewModel.findOverCardById(savedData.id).observe(this, Observer {
            task_detail_over_rv.adapter = TaskDetailOverAdapter(it as ArrayList<OverCardsEntity>)
        })
    }

    private fun addOverCards() {
        val fragment =
            TaskAddOverBS(savedData.id)
        fragment.show(supportFragmentManager, "AddOverCard")
    }

    private fun sendOverCards() {
        if (savedData.taskTypeId == TaskTypeEnum.inventory) {
            val list = ArrayList<OverCards>()
            viewModel.findOverCardById(savedData.id).observe(this, Observer {
                it.forEach {
                    list.add(
                        OverCards(
                            it.pipeSerialNumber,
                            it.serialNoOfNipple,
                            it.couplingSerialNumber,
                            it.rfidTagNo,
                            it.comment
                        )
                    )
                }
                val model = TaskOverCards(savedData.id, list)

                viewModel.sendOverCards(model).observe(this, Observer { result ->
                    val data = result.data
                    when (result.status) {
                        Status.SUCCESS -> {
                            toast("$data")
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.deleteTaskById(savedData.id)
                                viewModel.deleteCardsById(savedData.id)
                                withContext(Dispatchers.Main){
                                    startActivity(Intent(applicationContext, TaskActivity::class.java))
                                    finish()
                                    loadingHide()
                                }
                            }
                        }
                        else -> {
                            toast("$data")
                            CoroutineScope(Dispatchers.IO).launch {
                                viewModel.deleteTaskById(savedData.id)
                                viewModel.deleteCardsById(savedData.id)
                                withContext(Dispatchers.Main){
                                    startActivity(Intent(applicationContext, TaskActivity::class.java))
                                    finish()
                                    loadingHide()
                                }
                            }
                            loadingHide()
                        }
                    }


                })
            })
        }
    }

    private fun accounting(isConfirm: Boolean, problemComment: String?): Int {
        return if (isConfirm || problemComment != null) {
            1
        } else {
            0
        }
    }

}
