package com.example.rfidtab.adapter.status

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rfidtab.R
import com.timelysoft.kainarapp.base.GenericRecyclerAdapter
import com.timelysoft.kainarapp.base.ViewHolder
import com.timelysoft.kainarcourierapp.service.response.StatusResponse


class StatusAdapter(
    items: ArrayList<StatusResponse> = ArrayList()
) :
    GenericRecyclerAdapter<StatusResponse>(items) {

    override fun bind(item: StatusResponse, holder: ViewHolder) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return super.onCreateViewHolder(parent, R.layout.item_status)
    }
}