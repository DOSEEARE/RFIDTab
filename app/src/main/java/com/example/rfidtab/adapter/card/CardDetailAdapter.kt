package com.example.rfidtab.adapter.card

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.view.Display
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.rfidtab.R
import com.example.rfidtab.base.GenericRecyclerAdapter
import com.example.rfidtab.base.ViewHolder
import com.example.rfidtab.service.db.entity.task.CardImagesEntity
import kotlinx.android.synthetic.main.item_image.view.*

class CardDetailAdapter(
    private val listener: CardDetailListener,
    private val context: AppCompatActivity,
    items: ArrayList<CardImagesEntity> = ArrayList()
) :
    GenericRecyclerAdapter<CardImagesEntity>(items) {
    private val display: Display = context.windowManager.defaultDisplay
    private val size = Point()

    override fun bind(item: CardImagesEntity, holder: ViewHolder) {
        display.getSize(size)
        holder.itemView.card_item_image.layoutParams.height = size.x / 3
        holder.itemView.card_item_image.layoutParams.width = size.x / 3

        val bitmap = BitmapFactory.decodeFile(item.imagePath)
        val bitmapResized =
            Bitmap.createScaledBitmap(bitmap, bitmap.width / 10, bitmap.height / 10, true)

        Glide
            .with(holder.itemView)
            .load(bitmapResized)
            .into(holder.itemView.card_item_image)

        holder.itemView.setOnClickListener {
            listener.imageClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_image)
    }

}