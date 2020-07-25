package com.example.rfidtab.ui.task

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidtab.R
import com.example.rfidtab.ui.task.fragment.OnlineTaskFragment
import com.example.rfidtab.ui.task.fragment.SavedTaskFragment
import com.timelysoft.kainarcourierapp.adapter.viewpager.FragmentVPAdapter
import kotlinx.android.synthetic.main.activity_task.*

class TaskActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        supportActionBar?.title = getString(R.string.task)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initTab()
    }

    private fun initTab() {
        val vpAdapter = FragmentVPAdapter(supportFragmentManager)
        vpAdapter.addFragment(OnlineTaskFragment(), "Задания")
        vpAdapter.addFragment(SavedTaskFragment(), "Сохраненные")
        task_vp.adapter = vpAdapter
        task_tabl.setupWithViewPager(task_vp)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
