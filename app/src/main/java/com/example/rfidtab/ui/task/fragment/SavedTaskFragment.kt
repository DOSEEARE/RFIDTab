package com.example.rfidtab.ui.task.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.KitOrderSavedAdapter
import com.example.rfidtab.adapter.kitorder.KitOrderSavedListener
import com.example.rfidtab.adapter.task.TaskSavedAdapter
import com.example.rfidtab.adapter.task.TaskSavedListener
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.model.enums.TaskTypeEnum
import com.example.rfidtab.ui.kitorder.KitOrderActivity
import com.example.rfidtab.ui.kitorder.KitOrderViewModel
import com.example.rfidtab.ui.task.TaskDetailActivity
import com.example.rfidtab.ui.task.TaskViewModel
import kotlinx.android.synthetic.main.fragment_saved_tasks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedTaskFragment : Fragment(), TaskSavedListener, KitOrderSavedListener {
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

        kitOrderViewModel.findKitOrder().observe(viewLifecycleOwner, Observer {
            saved_task_kit_rv.adapter = KitOrderSavedAdapter(this, it as ArrayList<KitOrderEntity>)
        })

    }

    override fun onItemClicked(model: TaskResultEntity) {
        if (model.taskTypeId == TaskTypeEnum.kitForOder) {
            val orderIntent = Intent(activity, KitOrderActivity::class.java)
            orderIntent.putExtra("data", model)
            orderIntent.putExtra("isOnline", false)
            startActivity(orderIntent)
        } else {
            val intent = Intent(activity, TaskDetailActivity::class.java)
            intent.putExtra("data", model)
            intent.putExtra("isOnline", false)
            startActivity(intent)
    }
}

    override fun onItemDeleted(id: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Удалить задание?")

        builder.setPositiveButton("Да, удалить") { dialog, which ->
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteTaskById(id)
                viewModel.deleteCardsById(id)
                withContext(Dispatchers.Main) {
                    initViews()
                }
        }
    }

    builder.setNegativeButton("Нет") { dialog, which ->
        dialog.dismiss()
    }
    builder.show()
}

    override fun onKitItemClicked(model: KitOrderEntity) {
        val intent = Intent(activity, KitOrderActivity::class.java)
        intent.putExtra("data", model)
        intent.putExtra("isOnline", false)
        startActivity(intent)
    }

}
