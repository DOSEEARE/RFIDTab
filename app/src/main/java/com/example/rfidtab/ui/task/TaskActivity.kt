package com.example.rfidtab.ui.task

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.rfidtab.R
import com.timelysoft.kainarcourierapp.adapter.viewpager.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        initTab()
    }

    private fun initTab() {
        val vpAdapter = FragmentVPAdapter(supportFragmentManager)
        vpAdapter.addFragment(OnlineTaskFragment(),"Задания")
        vpAdapter.addFragment(SavedTaskFragment(),"Сохарненные")
        task_vp.adapter = vpAdapter
        task_tabl.setupWithViewPager(task_vp)
    }
}
