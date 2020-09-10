package com.example.rfidtab.ui.task.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.task.TaskOnlineAdapter
import com.example.rfidtab.adapter.task.TaskOnlineListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.kitorder.KitOrderCardEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderKitEntity
import com.example.rfidtab.service.db.entity.kitorder.KitOrderSpecificationEntity
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.enums.TaskStatusEnum
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.ui.kitorder.KitOrderActivity
import com.example.rfidtab.ui.kitorder.KitOrderViewModel
import com.example.rfidtab.ui.task.MarkActivity
import com.example.rfidtab.ui.task.TaskDetailActivity
import com.example.rfidtab.ui.task.TaskViewModel
import kotlinx.android.synthetic.main.alert_add.view.*
import kotlinx.android.synthetic.main.fragment_online_tasks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

class OnlineTaskFragment : Fragment(), TaskOnlineListener {
    private val viewModel: TaskViewModel by viewModel()
    private val kitOrderViewModel: KitOrderViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_online_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniViews()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && view != null) {
            iniViews()
        }
    }

    private fun iniViews() {
        viewModel.taskNetwork(true).observe(viewLifecycleOwner, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    online_task_rv.adapter =
                        TaskOnlineAdapter(this, data as ArrayList<TaskResponse>)
                }
                Status.ERROR -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    override fun onItemClicked(model: TaskResponse) {
        when (model.taskTypeId) {
            //Переход на Комплектация в аренду
            TaskTypeEnum.kitForOder -> {
                val orderIntent = Intent(activity, KitOrderActivity::class.java)
                orderIntent.putExtra("data", model)
                orderIntent.putExtra("isOnline", true)
                startActivity(orderIntent)
            }
            //Переход на Маркировка и инвенторизация
            TaskTypeEnum.marking -> {
                val intent = Intent(activity, MarkActivity::class.java)
                intent.putExtra("data", model)
                intent.putExtra("isOnline", true)
                startActivity(intent)
            }
            //Переход на Комплектация на ремонт Инспекция
            else -> {
                val intent = Intent(activity, TaskDetailActivity::class.java)
                intent.putExtra("data", model)
                intent.putExtra("isOnline", true)
                startActivity(intent)
            }
        }
    }

    override fun onItemSaved(model: TaskResponse) {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_add, null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()

        view.add_positive_btn.setOnClickListener {
            if (model.statusId == TaskStatusEnum.sentToExecutor) {
                if (model.taskTypeId != TaskTypeEnum.kitForOder) {
                    changeTaskStatus(model)
                    alertDialog.dismiss()
                } else {
                    saveKitOrder(model.id)
                    changeTaskStatus(model)
                    alertDialog.dismiss()
                }
            } else {
                toast("Уже принят на исполнение")
                alertDialog.dismiss()
            }

        }

        view.add_negative_btn.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()

    }

    private fun changeTaskStatus(model: TaskResponse) {
        viewModel.taskStatusChange(
            TaskStatusModel(
                model.id,
                model.taskTypeId,
                TaskStatusEnum.takenForExecution
            )
        ).observe(viewLifecycleOwner, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    toast("Вы взяли задание на исполнение")
                    saveItemToDb(model)
                }
                Status.ERROR -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
                }
            }


        })
    }

    @SuppressLint("NewApi")
    private fun saveKitOrder(taskId: Int) {
        kitOrderViewModel.kitOrder(taskId).observe(viewLifecycleOwner, Observer { result ->
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
                            allCardCount(data.kitType, data.kitCardCount),
                            data.kitCardCount
                        )

                        val listCard = ArrayList<KitOrderCardEntity>()
                        val listKit = ArrayList<KitOrderKitEntity>()

                        data.kits.forEachIndexed { indexKit, kitOrderKit ->
                            listKit.add(
                                KitOrderKitEntity(
                                    kitOrderKit.id,
                                    taskId,
                                    kitOrderKit.title
                                )
                            )

                            kitOrderKit.cards.forEachIndexed { indexCard, kitOrderCard ->
                                listCard.add(
                                    KitOrderCardEntity(
                                        kitOrderCard.id,
                                        kitOrderKit.id,
                                        kitOrderCard.rfidTagNo,
                                        kitOrderCard.pipeSerialNumber,
                                        kitOrderCard.serialNoOfNipple,
                                        kitOrderCard.couplingSerialNumber,
                                        kitOrderCard.fullName,
                                        kitOrderCard.comment,
                                        false
                                    )
                                )

                            }
                            if (data.kits[indexKit].cards.isEmpty()) {
                                val spec = data.kits[indexKit].specification
                                val kitCardCount: String = data.kitCardCount?.split(",")!![indexKit]

                                if (!Objects.isNull(spec)) {
                                    val kitOrderSpec = KitOrderSpecificationEntity(
                                        spec.id,
                                        kitOrderKit.id,
                                        spec.outerDiameterOfThePipe,
                                        spec.pipeWallThickness,
                                        spec.odlockNipple,
                                        spec.idlockNipple,
                                        spec.pipeLength,
                                        spec.shoulderAngle,
                                        spec.turnkeyLengthNipple,
                                        spec.turnkeyLengthCoupling,
                                        spec.comment,
                                        kitCardCount

                                    )
                                    kitOrderViewModel.insertKitOrderSpec(kitOrderSpec)
                                }
                                //                                //3,3,3
                            }
                        }

                        kitOrderViewModel.insertKitOrder(entity)
                        kitOrderViewModel.insertKitItem(listKit)
                        kitOrderViewModel.insertKitCards(listCard)

                    }
                }
                Status.ERROR -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG)
                        .show()
                }
                else -> {
                    Toast.makeText(context, "Произошла ошибка", Toast.LENGTH_LONG).show()
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