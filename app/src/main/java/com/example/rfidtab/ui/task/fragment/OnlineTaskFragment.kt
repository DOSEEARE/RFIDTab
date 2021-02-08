package com.example.rfidtab.ui.task.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.example.rfidtab.service.RetrofitClient
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.kitorder.*
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.model.TaskStatusModel
import com.example.rfidtab.service.model.enums.TaskStatusEnum
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import com.example.rfidtab.service.response.task.OverCardsResponse
import com.example.rfidtab.service.response.task.TaskResponse
import com.example.rfidtab.ui.task.MarkActivity
import com.example.rfidtab.ui.task.TaskDetailActivity
import com.example.rfidtab.ui.task.TaskViewModel
import com.example.rfidtab.ui.task.kitorder.KitOrderActivity
import com.example.rfidtab.ui.task.kitorder.KitOrderViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.alert_add.view.*
import kotlinx.android.synthetic.main.fragment_online_tasks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
                    online_task_rv.adapter = TaskOnlineAdapter(this, data as ArrayList<TaskResponse>)
                }
                Status.ERROR -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG)
                        .show()
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
            //Переход на Маркировка
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
                    saveTask(model)
                    alertDialog.dismiss()
                } else {
                    saveKitOrder(model)
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

    private fun saveTask(model: TaskResponse) { viewModel.taskStatusChange(TaskStatusModel(model.id, model.taskTypeId, TaskStatusEnum.takenForExecution))
        .observe(viewLifecycleOwner, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    toast("Вы взяли задание на исполнение")
                    saveItemToDb(model)
                    if (model.taskTypeId == TaskTypeEnum.inventory){
                        getOverCardsAndSave(model.id)
                    }
                }
                Status.ERROR -> {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG).show()
                }
            }


        })
    }

    private fun getOverCardsAndSave(taskId: Int) {
        /*       viewModel.getOverCards(taskId).observe(viewLifecycleOwner, Observer { result ->
                   val data = result.data
                   val msg = result.msg
                   when (result.status) {
                       Status.SUCCESS -> {
                           val overCardsList = ArrayList<OverCardsEntity>()
                           data?.forEach { it ->
                               overCardsList.add(OverCardsEntity(it.id, taskId, it.pipeSerialNumber, it.serialNoOfNipple, it.couplingSerialNumber, it.rfidTagNo, it.comment))
                           }
                           Log.d("TASKID", "taskid onlineFragment: $overCardsList")
                           viewModel.insertOverCardsList(overCardsList)
                       }
                       Status.ERROR -> {
                           Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                       }
                       Status.NETWORK -> {
                           Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG).show()
                       }
                   }
               })*/

        RetrofitClient.apiService().getOverCardsNoLive(taskId)
            .enqueue(object : Callback<List<OverCardsResponse>> {
                override fun onFailure(call: Call<List<OverCardsResponse>>, t: Throwable) {
                    println(t)
                }

                override fun onResponse(call: Call<List<OverCardsResponse>>, response: Response<List<OverCardsResponse>>) { println(response)
                    if (response.isSuccessful) {
                        if (!response.body().isNullOrEmpty()) {
                            val overCardsList = ArrayList<OverCardsEntity>()
                            response.body()?.forEach { it ->
                                overCardsList.add(
                                    OverCardsEntity(it.id, taskId, it.pipeSerialNumber, it.serialNoOfNipple, it.couplingSerialNumber, it.rfidTagNo, it.comment)
                                )
                            }
                            Log.d("TASKID", "taskid onlineFragment: $overCardsList")
                            viewModel.insertOverCardsList(overCardsList)
                        }
                    }
                }
            })
    }
    private fun saveKitOrder(model: TaskResponse) {
        kitOrderViewModel.kitOrder(model.id).observe(viewLifecycleOwner, Observer { result ->
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
                                    kitOrderKit.id,
                                    model.id,
                                    kitOrderKit.title
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
                                        "insertKitOrder",
                                        "insertKitOrderSpec ID: ${kitOrderKit.id}"
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
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                }
                Status.NETWORK -> {
                    Toast.makeText(context, "Проблемы с интернетом", Toast.LENGTH_LONG).show()
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
                viewModel.insertTaskToDb(item)

                withContext(Dispatchers.Main) {
                    toast("Успешно сохранён!")
                }
            }
        }
    }

    private fun allCardCount(kitType: String?, kitCardCount: String?, cardCount : String?): String {
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

    private fun initAccounting(boolean: Boolean): Int {
        return if (boolean) {
            1
        } else {
            0
        }
    }
}