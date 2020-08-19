package com.example.rfidtab.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.search.SearchAdapter
import com.example.rfidtab.adapter.search.SearchListener
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.Status
import com.example.rfidtab.service.model.search.SearchCard
import com.example.rfidtab.service.model.search.SearchModel
import com.example.rfidtab.util.DataTransfer
import com.google.android.material.textfield.TextInputEditText
import com.senter.support.openapi.StUhf
import kotlinx.android.synthetic.main.activity_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchActivity : AppCompatActivity(), SearchListener {
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
            readTag(search_rfid)
        }
    }

    private fun readTag(resultView: TextInputEditText) {
        val bank = StUhf.Bank.TID
        val ptr = 0
        val cnt = 1
        val pwd = "00000000"
        val acsPass = StUhf.AccessPassword.getNewInstance(DataTransfer.getBytesByHexString(pwd))

        try {
            val iso18k6c: StUhf.InterrogatorModelDs.UmdOnIso18k6cRead =
                object : StUhf.InterrogatorModelDs.UmdOnIso18k6cRead() {
                    override fun onFailed(error: StUhf.InterrogatorModelDs.UmdErrorCode) {
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Нет метки!!!", Toast.LENGTH_LONG)
                                .show()
                        }
                    }

                    override fun onTagRead(
                        tagCount: Int,
                        uii: StUhf.UII,
                        data: ByteArray,
                        frequencyPoint: StUhf.InterrogatorModelDs.UmdFrequencyPoint,
                        antennaId: Int,
                        readCount: Int
                    ) {
                        tag = DataTransfer.xGetString(uii.bytes)

                        if (tag.isNotEmpty()) {
                            runOnUiThread {
                                resultView.setText(tag)
                            }
                        }
                    }
                }

            val uhf: StUhf =
                StUhf.getUhfInstance(StUhf.InterrogatorModel.InterrogatorModelD1).apply {
                    init()

                }

            uhf.getInterrogatorAs(StUhf.InterrogatorModelDs.InterrogatorModelD1::class.java)
                .iso18k6cRead(
                    acsPass,
                    bank,
                    ptr,
                    cnt,
                    iso18k6c
                )

        } catch (e: Exception) {
            Toast.makeText(this, "Нет метки!", Toast.LENGTH_SHORT).show()
            Log.w("RFID SCANNER", e.message)
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
            val number = getText(search_number)
            val rfid = search_rfid.text.toString()

            val searchModel = SearchModel(nipple, couple, number, rfid)

            viewModel.searchCard(searchModel).observe(this, Observer { result ->
                val data = result.data
                val msg = result.msg
                when (result.status) {
                    Status.SUCCESS -> {
                        if (data!!.multiple) {
                            search_result_rv.adapter =
                                SearchAdapter(this, data.cardList as ArrayList<SearchCard>)
                        } else {
                            val cardList = arrayListOf(data.card)
                            SearchAdapter(this, cardList)

                        }
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
}