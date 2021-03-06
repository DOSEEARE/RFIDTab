package com.example.rfidtab

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidtab.ui.createkit.CreateKitActivity
import com.example.rfidtab.ui.profile.ProfileActivity
import com.example.rfidtab.ui.search.SearchActivity
import com.example.rfidtab.ui.task.TaskActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = getString(R.string.home)
        initViews()
    }

    private fun initViews() {
        main_task_btn.setOnClickListener {
            startActivity(Intent(this, TaskActivity::class.java))
        }
        main_scan_btn.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        main_task_manager_btn.setOnClickListener {
            startActivity(Intent(this, CreateKitActivity::class.java))
        }

        main_task_profile_btn.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        println("project started")
    }

}
