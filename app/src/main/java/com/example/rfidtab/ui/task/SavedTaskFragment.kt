package com.example.rfidtab.ui.task

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.task.TaskSavedAdapter
import com.example.rfidtab.adapter.task.TaskSavedListener
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import kotlinx.android.synthetic.main.fragment_saved_tasks.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedTaskFragment : Fragment(), TaskSavedListener {
    private val viewModel: TasksViewModel by viewModel()

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
        viewModel.findAllTasks().observe(viewLifecycleOwner, Observer {
            val taskAdapter = TaskSavedAdapter(this, it as ArrayList<TaskResultEntity>)
            saved_task_rv.adapter = taskAdapter
            /* saved_empty_tv.visibility = View.GONE
             saved_empty_tv.visibility = View.VISIBLE*/
        })
    }

    override fun onItemClicked(model: TaskResultEntity) {
        val intent = Intent(activity, TaskDetailActivity::class.java)
        intent.putExtra("data", model)
        intent.putExtra("isOnline", false)
        startActivity(intent)
    }

    override fun onItemDeleted(id: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Удалить задание?")

        builder.setPositiveButton("Да, удалить") { dialog, which ->
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.deleteTaskById(id)
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

}
