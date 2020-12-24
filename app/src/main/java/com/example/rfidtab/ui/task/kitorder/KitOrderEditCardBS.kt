package com.example.rfidtab.ui.task.kitorder

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
import kotlinx.android.synthetic.main.fragment_task_edit_over.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class KitOrderEditCardBS(val kitId: Int, val item: KitOrderAddCardEntity) :
    BottomSheetDialogFragment(), RfidScannerListener {
    private val kitOrderViewModel: KitOrderViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_edit_over, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        over_tittle.setText("Изменить данные")
        initFields()
        initViews()
    }

    private fun initFields() {
        over_pipe_in.setText(item.pipeSerialNumber)
        over_nipple_in.setText(  item.serialNoOfNipple)
        over_couple_in.setText(item.couplingSerialNumber)
        over_rfid_in.setText(item.rfidTagNo)
        over_comment_in.setText(item.comment)
    }

    private fun initViews() {
        over_dismiss_btn.setOnClickListener {
            dismiss()
        }

        over_save_btn.setOnClickListener {
            val pipeText = " " + over_pipe_in.text
            val nippleText = " " + over_nipple_in.text
            val coupleText = " " + over_couple_in.text
            val rfidText = " " + over_rfid_in.text
            val commentText = " " + over_comment_in.text

            val model = KitOrderAddCardEntity(
                id = item.id,
                kitId = kitId,
                pipeSerialNumber = pipeText,
                serialNoOfNipple = nippleText,
                couplingSerialNumber = coupleText,
                rfidTagNo = rfidText,
                accounting = accounting(rfidText, commentText),
                comment = commentText
            )
            kitOrderViewModel.insertKitOrderAddCard(model)
            toast("Сохранён")
            dismiss()
        }

        over_scan_btn.setOnClickListener {
            RfidScannerUtil(this).show(childFragmentManager, "KitOrderAddCardsBS")
        }

    }

    override fun onAccessScan(tag: String) {
        over_rfid_in.setText(tag)
    }

    private fun accounting(rfid: String, comment: String): Int {
        return if (rfid.isNullOrBlank() && comment.isNullOrBlank() || rfid.contains("null") && comment.contains("null")) {
            0
        } else {
            1
        }
    }
}