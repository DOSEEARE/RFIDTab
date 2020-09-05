package com.example.rfidtab.ui.search

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.rfidtab.R
import com.example.rfidtab.adapter.search.SearchDetailAdapter
import com.example.rfidtab.service.model.search.SearchCard
import kotlinx.android.synthetic.main.activity_search_detail.*

class SearchDetailActivity : AppCompatActivity() {
    private lateinit var data: SearchCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Идентификация метки"
        setContentView(R.layout.activity_search_detail)

        data = intent.getSerializableExtra("data") as SearchCard

        search_detail_fullname.text = "Наименование: ${data.fullName}"
        search_detail_nipple.text = "№ Ниппель: ${data.serialNoOfNipple}"
        search_detail_bond.text = "№ Муфта: ${data.couplingSerialNumber}"
        search_detail_pipe.text = "№ Трубы: ${data.pipeSerialNumber}"
        search_detail_rfid.text = "Rfid: ${data.rfidTagNo}"
        search_detail_comment.text = "Комментарии: ${data.comment}"

        if (!data.imagesLink.isNullOrEmpty()) {
            search_detail_rv.adapter =
                SearchDetailAdapter(this, data.imagesLink as ArrayList<String>)
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}