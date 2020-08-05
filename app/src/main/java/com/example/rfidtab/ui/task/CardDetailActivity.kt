package com.example.rfidtab.ui.task

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.adapter.card.CardDetailAdapter
import com.example.rfidtab.adapter.card.CardDetailListener
import com.example.rfidtab.service.db.entity.task.CardImagesEntity
import com.example.rfidtab.service.db.entity.task.TaskCardListEntity
import kotlinx.android.synthetic.main.activity_card_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardDetailActivity : AppCompatActivity(), CardDetailListener {
    private val viewModel: TaskViewModel by viewModel()
    private lateinit var model: TaskCardListEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_detail)
        supportActionBar?.title = "Подробнее о карточке"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        model = intent.getParcelableExtra("model") as TaskCardListEntity
        initViews()
        initImagesRv()
    }

    private fun initViews() {
        card_detail_comment.text = "Комментарии: ${model.comment}"
        card_detail_pipe.text = "№ трубы: ${model.pipeSerialNumber}"
        card_detail_nipple.text = "№ Ниппеля: ${model.serialNoOfNipple}"
        card_detail_name.text = "Наименование: ${model.fullName}"
        card_detail_bond.text = "№ муфты: ${model.couplingSerialNumber}"
        card_detail_rfid.text = "№ RFID: ${model.rfidTagNo}"
        card_detail_problem_comment.text = "Проблемы с меткой: ${model.commentProblemWithMark}"
    }

    private fun initImagesRv() {
        viewModel.findImagesById(model.cardId).observe(this, Observer {
            val adapter = CardDetailAdapter(this, this, it as ArrayList<CardImagesEntity>)
            if (adapter.itemCount != 0) {
                card_detail_rv.adapter = adapter
            } else {
                card_detail_rv.visibility = View.GONE
                card_detail_empty.visibility = View.VISIBLE
            }
          //  card_detail_rv.addItemDecoration(GridAutoFit(16))
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun imageClicked(model: CardImagesEntity) {
        val bitmap = BitmapFactory.decodeFile(model.imagePath)
        card_detail_image.setImageBitmap(bitmap)
        card_detail_image.visibility = View.VISIBLE
        card_detail_back_btn.visibility = View.VISIBLE
        supportActionBar!!.hide()
        card_detail_back_btn.setOnClickListener {
            card_detail_image.visibility = View.GONE
            card_detail_back_btn.visibility = View.GONE
            supportActionBar!!.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finish()
    }
}
