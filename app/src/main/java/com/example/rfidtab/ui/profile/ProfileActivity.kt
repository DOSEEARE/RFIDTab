package com.example.rfidtab.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.service.Status
import com.example.rfidtab.ui.auth.AuthActivity
import kotlinx.android.synthetic.main.activity_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Профиль"
        initViews()
    }

    private fun initViews() {
        profile_exit_btn.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Вы хотите выйти?")

            builder.setPositiveButton("Да, выйти") { _, _ ->
                AppPreferences.clear()
                startActivity(Intent(this, AuthActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK))
            }

            builder.setNegativeButton("Нет") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()

        }

        viewModel.userInfo(0).observe(this, Observer { result ->
            val data = result.data
            val msg = result.msg
            when (result.status) {
                Status.SUCCESS -> {
                    task_name.text = "Пользователь: ${data!!.fio} "
                    task_phone.text = "Номер тел: : ${data.phone} "
                    task_email.text = "Эл почта: ${data.email} "
                    task_role.text = "Обязанность: ${data.roleTitle} "
                }
                Status.ERROR -> {
                    toast(msg)
                }
                Status.NETWORK -> {
                    toast("Проблемы с интернетом")

                }
            }

        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
