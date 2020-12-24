package com.example.rfidtab.ui.task.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.rfidtab.R
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import com.example.rfidtab.ui.task.TaskViewModel
import com.example.rfidtab.util.scanrfid.RfidScannerListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_task_add_over.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel

class TaskEditOverBS(private val taskId: Int, private val overCardId: Int) : BottomSheetDialogFragment(),
    RfidScannerListener {
    private val viewModel: TaskViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dialog!!.setOnShowListener { dialog ->
            val d: BottomSheetDialog = dialog as BottomSheetDialog
            val bottomSheetInternal =
                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as View
            BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
        }

        dialog!!.setCancelable(false)
        return inflater.inflate(R.layout.fragment_task_edit_over, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }
    
    private fun initViews() {
        viewModel.findOverCardById(overCardId).observe(viewLifecycleOwner, Observer {
            over_pipe_in.setText(it.pipeSerialNumber)
            over_nipple_in.setText(it.serialNoOfNipple)
            over_couple_in.setText(it.couplingSerialNumber)
            over_rfid_in.setText(it.rfidTagNo)
            over_comment_in.setText(it.comment)
        })

        over_save_btn.setOnClickListener {
            val pipeText = " " + over_pipe_in.text
            val nippleText = " " + over_nipple_in.text
            val coupleText = " " + over_couple_in.text
            val rfidText = " " + over_rfid_in.text
            val commentText = " " + over_comment_in.text

            val item = OverCardsEntity(
                overCardId,
                taskId,
                pipeText,
                nippleText,
                coupleText,
                rfidText,
                commentText
            )
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.insertOverCard(item)
                withContext(Dispatchers.Main) {
                    dismiss()
                }
            }
        }

        over_dismiss_btn.setOnClickListener {
            dismiss()
        }
    }

    override fun onAccessScan(tag: String) {
        over_rfid_in.setText(tag)
    }

}