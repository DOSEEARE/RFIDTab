package com.example.rfidtab.ui.kitorder

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidtab.R
import com.example.rfidtab.adapter.kitorder.card.KitCardOnlineAdapter
import com.example.rfidtab.service.db.entity.kitorder.KitOrderEntity
import com.example.rfidtab.service.response.kitorder.KitOrderCard
import com.example.rfidtab.service.response.kitorder.KitOrderKit
import com.example.rfidtab.ui.task.TaskViewModel
import kotlinx.android.synthetic.main.activity_kit_detail_order.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class KitOrderDetailActivity : AppCompatActivity() {
    private val taskViewModel: TaskViewModel by viewModel()
    private val kitOrderViewModel: KitOrderViewModel by viewModel()

    private lateinit var onlineData: KitOrderKit
    private lateinit var savedData: KitOrderEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kit_detail_order)
        supportActionBar?.title = "Комплектация в аренду"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initViews()
    }

    private fun initViews() {
        val isOnline = intent.getBooleanExtra("isOnline", true)

        if (isOnline) {
            onlineData = intent.getSerializableExtra("data") as KitOrderKit

            kit_order_detail_title.text = "Титул: ${onlineData.title}"
            kit_order_detail_rv.adapter =
                KitCardOnlineAdapter(onlineData.cards as ArrayList<KitOrderCard>)
        } else {
            savedData = intent.getSerializableExtra("data") as KitOrderEntity

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }


}