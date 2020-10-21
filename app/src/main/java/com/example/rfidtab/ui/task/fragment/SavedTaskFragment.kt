package com.example.rfidtab.ui.task.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.task.TaskKitSavedAdapter
import com.example.rfidtab.adapter.task.TaskKitSavedListener
import com.example.rfidtab.adapter.task.TaskSavedAdapter
import com.example.rfidtab.adapter.task.TaskSavedListener
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import com.example.rfidtab.ui.task.kitorder.KitOrderActivity
import com.example.rfidtab.ui.task.kitorder.KitOrderViewModel
import com.example.rfidtab.ui.task.TaskDetailActivity
import com.example.rfidtab.ui.task.TaskViewModel
import com.example.rfidtab.ui.task.MarkActivity
import kotlinx.android.synthetic.main.fragment_saved_tasks.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedTaskFragment : Fragment(), TaskSavedListener,
    TaskKitSavedListener {
    private val viewModel: TaskViewModel by viewModel()
    private val kitOrderViewModel: KitOrderViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_saved_tasks, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        viewModel.findTasksByLogin(AppPreferences.userLogin!!)
            .observe(viewLifecycleOwner, Observer {
                val taskAdapter = TaskSavedAdapter(this, it as ArrayList<TaskResultEntity>)
                saved_task_rv.adapter = taskAdapter
            })

        kitOrderViewModel.findKitOrderByLogin(AppPreferences.userLogin!!)
            .observe(viewLifecycleOwner, Observer {
                saved_task_kit_rv.adapter =
                    TaskKitSavedAdapter(
                        this,
                        it as ArrayList<KitOrderEntity>
                    )
            })

    }

    override fun onItemClicked(model: TaskResultEntity) {
        when (model.taskTypeId) {
            //Переход на Комплектация в аренду
            TaskTypeEnum.kitForOder -> {
                val orderIntent = Intent(activity, KitOrderActivity::class.java)
                orderIntent.putExtra("data", model)
                orderIntent.putExtra("isOnline", false)
                startActivity(orderIntent)
            }
            //Переход на Маркировка и инвенторизация
            TaskTypeEnum.marking -> {
                val intent = Intent(activity, MarkActivity::class.java)
                intent.putExtra("data", model)
                intent.putExtra("isOnline", false)
                startActivity(intent)
            }
            //Переход на Комплектация на ремонт Инспекция
            else -> {
                val intent = Intent(activity, TaskDetailActivity::class.java)
                intent.putExtra("data", model)
                intent.putExtra("isOnline", false)
                startActivity(intent)
            }
        }
    }

    override fun onKitItemClicked(model: KitOrderEntity) {
        val intent = Intent(activity, KitOrderActivity::class.java)
        intent.putExtra("data", model)
        intent.putExtra("isOnline", false)
        startActivity(intent)
    }

}
