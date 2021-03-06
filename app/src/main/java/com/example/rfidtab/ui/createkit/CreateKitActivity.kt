package com.example.rfidtab.ui.createkit

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kit.CreateKitAdapter
import com.example.rfidtab.adapter.kit.CreateKitListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.AppPreferences
import com.example.rfidtab.service.db.entity.kit.KitItemEntity
import kotlinx.android.synthetic.main.activity_create_set.*
import kotlinx.android.synthetic.main.alert_kit_add.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class CreateKitActivity : AppCompatActivity(), CreateKitListener {
    private val viewModel: CreateKitViewModel by viewModel()
    private lateinit var kitAdapter : CreateKitAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_set)
        supportActionBar?.title = "Создание комплекта"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initKitRv()
        initFabBtn()
    }

    private fun initFabBtn() {
        kit_add_btn.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)
            val inflater = this.layoutInflater
            val view = inflater.inflate(R.layout.alert_kit_add, null)
            dialogBuilder.setView(view)
            val alertDialog = dialogBuilder.create()

            view.kit_item_access.setOnClickListener {
                val text = view.kit_item_et.text.toString()
                if (text.isNotEmpty()){
                    CoroutineScope(Dispatchers.IO).launch {
                        viewModel.insertKitItem(
                            KitItemEntity(
                                Random.nextInt(),
                                AppPreferences.userLogin!!,
                                text
                            )
                        )
                        alertDialog.dismiss()
                    }
                    kit_empty.visibility = View.GONE
                    toast("Комплект создан")
                }else{
                    view.kit_item_et.error = "Название не может быть пустым"
                }
            }
            view.kit_item_denied.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()

        }

    }

    private fun initKitRv() {
            viewModel.findKitItem(AppPreferences.userLogin!!).observe(this, Observer {
                kitAdapter = CreateKitAdapter(this, it as ArrayList<KitItemEntity>)
                kit_rv.adapter = kitAdapter
                if (it.isEmpty()) {
                    kit_empty.visibility = View.VISIBLE
                    kit_rv.visibility = View.GONE
                }else{
                    kit_empty.visibility = View.GONE
                    kit_rv.visibility = View.VISIBLE
                }
            })
    }

    override fun kitItemClicked(model: KitItemEntity) {
        val fm = CreateKitDetailBS(model)
        fm.show(supportFragmentManager, "CreateKitDetailFragment")
    }

    override fun kitItemDelete(kitId: Int) {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val view = inflater.inflate(R.layout.alert_kit_add, null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()

        view.kit_item_et.isVisible = false
        view.kit_item_title.text = "Вы действительно хотите удалить комплект?"

        view.kit_item_access.setOnClickListener {
            viewModel.deleteKitItem(kitId)
            viewModel.deleteKitAllRfid(kitId)
            alertDialog.dismiss()
        }

        view.kit_item_denied.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}