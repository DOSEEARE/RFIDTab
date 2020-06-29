package com.example.rfidtab.ui.task

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.task.TaskOnlineAdapter
import com.example.rfidtab.adapter.task.TaskOnlineListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.db.entity.task.TaskWithCards
import com.example.rfidtab.service.response.task.TaskResult
import kotlinx.android.synthetic.main.fragment_online_tasks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnlineTaskFragment : Fragment(), TaskOnlineListener {
    private val viewModel: TasksViewModel by viewModel()

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

    private fun iniViews() {
        viewModel.taskNetwork(true).observe(viewLifecycleOwner, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    online_task_rv.adapter =
                        TaskOnlineAdapter(this, data?.result as ArrayList<TaskResult>)
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

    override fun onItemClicked(model: TaskResult) {
        val intent = Intent(activity, TaskDetailActivity::class.java)
        intent.putExtra("data", model)
        intent.putExtra("isOnline", true)
        startActivity(intent)
    }


    override fun onItemSaved(model: TaskResult) {
        CoroutineScope(Dispatchers.IO).launch {
            val cards = ArrayList<TaskCardListEntity>()

            model.cardList.forEachIndexed { index, taskCardList ->
                val a = model.cardList[index]
                cards.add(
                    TaskCardListEntity(
                        a.id,
                        a.fullName,
                        a.pipeSerialNumber,
                        a.serialNoOfNipple,
                        a.couplingSerialNumber,
                        a.rfidTagNo,
                        a.comment
                    )
                )
            }
            val item = TaskWithCards(
                TaskResultEntity(
                    model.id,
                    model.statusId,
                    model.statusTitle,
                    model.taskTypeId,
                    model.taskTypeTitle,
                    model.createdByFio,
                    model.executorFio
                ), cards
            )
            viewModel.insertTaskToDb(item)

            withContext(Dispatchers.Main) {
                toast("Успешно сохранён!")
            }
        }
    }


}
