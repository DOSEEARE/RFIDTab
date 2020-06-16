package com.example.rfidtab.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.MainActivity
import com.example.rfidtab.R
import com.example.rfidtab.service.model.AuthModel
import com.example.rfidtab.extension.toast
import com.timelysoft.kainarcourierapp.service.Status
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AuthActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        auth()

    }

    private fun auth() {
        login_enter.setOnClickListener {
            viewModel.auth(AuthModel(login_login.text.toString(), login_password.text.toString()))
                .observe(this, Observer { result ->
                    val data = result.data
                    val msg = result.msg
                    when (result.status) {
                        Status.SUCCESS -> {
                            toast("Успешно авторизованы!")
                            startActivity(Intent(this, MainActivity::class.java))
                        }
                        Status.ERROR -> {
                            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                        }
                        Status.NETWORK -> {
                            Toast.makeText(this, "Проблемы с интернетом", Toast.LENGTH_LONG)
                                .show()
                        }
                        else -> {
                            Toast.makeText(this, "Произошла ошибка", Toast.LENGTH_LONG).show()
                        }
                    }

                })
        }
    }
}
