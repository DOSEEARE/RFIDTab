package com.example.rfidtab.ui.task

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidtab.R
import com.example.rfidtab.adapter.taskDetail.TaskDetailOnlineAdapter
import com.example.rfidtab.service.db.entity.task.TaskResultEntity
import com.example.rfidtab.service.response.task.TaskCardList
import com.example.rfidtab.service.response.task.TaskResult
import kotlinx.android.synthetic.main.activity_task_detail_activity.*

class TaskDetailActivity : AppCompatActivity() {
    private lateinit var onlineData: TaskResult
    private lateinit var savedData: TaskResultEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail_activity)
        initViews()
    }

    private fun initViews() {
        val isOnline = intent.getBooleanExtra("isOnline", true)

        if (isOnline) {
            val model = intent.getParcelableExtra<TaskResult>("data")
            onlineData = model
            task_detail_createdby.text = "Постановщик: ${onlineData.createdByFio}"
            task_detail_executor.text = "Исполнитель: ${onlineData.executorFio}"
            task_detail_status.text = "Статус: ${onlineData.statusTitle}"
            task_detail_card_count.text = "Общее кол-во карточек: ${5}"
            task_detail_rv.adapter =
                TaskDetailOnlineAdapter(onlineData.cardList as ArrayList<TaskCardList>)
        } else {
            val model = intent.getParcelableExtra<TaskResultEntity>("data")
            savedData = model

            task_detail_createdby.text = savedData.createdByFio
            task_detail_executor.text = savedData.executorFio
            task_detail_status.text = savedData.statusTitle
        }
    }

}
