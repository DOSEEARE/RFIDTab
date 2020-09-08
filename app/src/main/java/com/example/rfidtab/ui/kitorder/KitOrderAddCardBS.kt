package com.example.rfidtab.ui.kitorder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rfidtab.R
import com.example.rfidtab.extension.toast
import com.example.rfidtab.service.db.entity.kitorder.KitOrderAddCardEntity
import com.example.rfidtab.util.scanrfid.RfidScannerListener
import com.example.rfidtab.util.scanrfid.RfidScannerUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_kit_order_add_card.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class KitOrderAddCardBS(val kitId: Int) : BottomSheetDialogFragment(), RfidScannerListener {
    private val kitOrderViewModel: KitOrderViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_kit_order_add_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {
        add_cant_find.setOnClickListener {
            add_divider1.show()
            add_divider2.show()
            add_divider3.show()
            add_divider4.show()
            add_divider5.show()

            add_pipe_btn.show()
            add_pipe_out.show()

            add_nipple_btn.show()
            add_nipple_out.show()

            add_couple_btn.show()
            add_couple_out.show()

            add_comment_btn.show()
            add_comment_out.show()

            add_dismiss_btn.setOnClickListener {
                dismiss()
            }

        }

        add_save_btn.setOnClickListener {
            add_rfid_in.text.toString()
            val rfid = add_rfid_in.text.toString()

            val pipe = add_pipe_in?.text.toString().toInt()
            val nipple = add_nipple_in?.text.toString().toInt()
            val couple = add_couple_in?.text.toString().toInt()
            val comment = add_comment_in.text.toString()

            val model = KitOrderAddCardEntity(Random.nextInt(0, 100), kitId, pipe, nipple, couple, rfid, 0, comment)

            kitOrderViewModel.insertKitOrderAddCard(model)
            toast("Сохранён")
            dismiss()
        }

        add_scan_btn.setOnClickListener {
            RfidScannerUtil(this).show(childFragmentManager, "KitOrderAddCardsBS")
        }

    }

    private fun View.show() {
        visibility = View.VISIBLE
    }

    override fun onAccessScan(tag: String) {
        add_rfid_in.setText(tag)
    }
}