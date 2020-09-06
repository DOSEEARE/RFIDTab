package com.example.rfidtab.adapter.kitorder.card

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.kitorder.KitOrderCardEntity
import kotlinx.android.synthetic.main.item_cards.view.card_bond
import kotlinx.android.synthetic.main.item_cards.view.card_camera_btn
import kotlinx.android.synthetic.main.item_cards.view.card_name
import kotlinx.android.synthetic.main.item_cards.view.card_nipple
import kotlinx.android.synthetic.main.item_cards.view.card_pipe
import kotlinx.android.synthetic.main.item_cards.view.card_rfid
import kotlinx.android.synthetic.main.item_cards.view.card_scan_btn
import kotlinx.android.synthetic.main.item_cards_confirm.view.*

class KitCardSavedAdapter(
    val listener: KitCardSavedListener,
    items: ArrayList<KitOrderCardEntity> = ArrayList()
) :
    GenericRecyclerAdapter<KitOrderCardEntity>(items) {

    override fun bind(item: KitOrderCardEntity, holder: ViewHolder) {
        holder.itemView.card_rfid.text = "№ RFID: ${item.rfidTagNo}"
        holder.itemView.card_name.visibility = View.GONE
        holder.itemView.card_pipe.text = "№ трубы: ${item.pipeSerialNumber}"
        holder.itemView.card_nipple.text = "№ ниппеля: ${item.serialNoOfNipple}"
        holder.itemView.card_bond.text = "№ муфты: ${item.couplingSerialNumber}"

        holder.itemView.card_camera_btn.visibility = View.GONE
        holder.itemView.card_scan_btn.visibility = View.VISIBLE

        holder.itemView.card_confirm_comment.text = "Комментарии: ${item.comment}"


        holder.itemView.card_confirm_problem_mark.visibility = View.GONE

        holder.itemView.card_scan_btn.setOnClickListener {
            listener.scanBtnClicked(item)
        }

        if (item.isConfirmed) {
            holder.itemView.card_confirmed_tv.text = "Подтверждён"
            holder.itemView.card_confirmed_tv.setBackgroundColor(Color.GREEN)
        } else {
            holder.itemView.card_confirmed_tv.text = "Не подтверждён"
            holder.itemView.card_confirmed_tv.setBackgroundColor(Color.RED)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_cards_confirm)
    }

}