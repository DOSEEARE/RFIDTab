package com.example.rfidtab.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.search.SearchAdapter
import com.example.rfidtab.adapter.search.SearchListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.model.search.SearchCard
import com.example.rfidtab.service.model.search.SearchModel
import com.example.rfidtab.util.MyUtil
import com.example.rfidtab.util.scanrfid.RfidScannerListener
import com.example.rfidtab.util.scanrfid.RfidScannerUtil
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), SearchListener, RfidScannerListener {
    private var tag = String()
    private val viewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Идентификация метки"
        initViews()
        initScanner()
    }

    private fun initScanner() {
        search_scan_btn.setOnClickListener {
            RfidScannerUtil(this).run {
                isCancelable = false
                show(supportFragmentManager, "RfidScannerUtil")
            }
        }
    }

    private fun getText(view: TextInputEditText): Int {
        return if (view.text.toString().isEmpty()) {
            0
        } else {
            view.text.toString().toInt()
        }
    }

    private fun initViews() {
        search_send_btn.setOnClickListener {
            val nipple = getText(search_nipple)
            val couple = getText(search_couple)
            val pipe = getText(search_number)
            val rfid = search_rfid.text.toString()

            val searchModel = SearchModel(pipe, couple, nipple, rfid)

            viewModel.searchCard(searchModel).observe(this, Observer { result ->
                val data = result.data
                val msg = result.msg
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.cardList.isNotEmpty()) {
                            search_result_rv.adapter =
                                SearchAdapter(this, data.cardList as ArrayList<SearchCard>)
                        } else {
                            val cardList = arrayListOf(data.card)
                            search_result_rv.adapter =  SearchAdapter(this, cardList)

                        }
                        MyUtil().hideKeyboard(this)
                    }
                    Status.ERROR -> {
                        toast(msg)
                    }
                    Status.NETWORK -> {
                        toast("Проблемы с интернетом")

                    }
                    else -> {
                        toast("Произшла ошибка")
                    }
                }


            })

        }

    }

    override fun onItemClicked(model: SearchCard) {
        val intent = Intent(this, SearchDetailActivity::class.java)
        intent.putExtra("data", model)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAccessScan(tag: String) {
        search_rfid.setText(tag)
    }
}