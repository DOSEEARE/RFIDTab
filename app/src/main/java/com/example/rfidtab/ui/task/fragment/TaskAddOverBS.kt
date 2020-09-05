package com.example.rfidtab.ui.task.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.rfidtab.R
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import com.example.rfidtab.ui.task.TaskViewModel
import com.example.rfidtab.util.scanrfid.RfidScannerListener
import com.example.rfidtab.util.scanrfid.RfidScannerUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_task_add_over.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class TaskAddOverBS(val taskId: Int) : BottomSheetDialogFragment(), RfidScannerListener {
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
        return inflater.inflate(R.layout.fragment_task_add_over, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideAndShow()
        initViews()
    }

    private fun hideAndShow() {
        hideView(over_pipe_out)
        hideView(over_nipple_out)
        hideView(over_couple_out)
        hideView(over_rfid_out)
        hideView(over_comment_out)

        over_pipe_btn.setOnClickListener {
            showView(over_pipe_out)
        }

        over_nipple_btn.setOnClickListener {
            showView(over_nipple_out)

        }
        over_couple_btn.setOnClickListener {
            showView(over_couple_out)

        }
        over_rfid_btn.setOnClickListener {
            showView(over_rfid_out)
            over_scan_btn.setOnClickListener {
                RfidScannerUtil(this).run {
                    isCancelable = false
                    show(this@TaskAddOverBS.childFragmentManager, "RfidScannerUtil")
                }
            }

        }
        over_comment_btn.setOnClickListener {
            showView(over_comment_out)

        }
    }

    private fun initViews() {
        over_save_btn.setOnClickListener {
            val pipeText = " " + over_pipe_in.text
            val nippleText = " " + over_nipple_in.text
            val coupleText = " " + over_couple_in.text
            val rfidText = " " + over_rfid_in.text
            val commentText = " " + over_comment_in.text

            val item = OverCardsEntity(
                Random.nextInt(0, 1000),
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

    private fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    private fun hideView(view: View) {
        view.visibility = View.GONE
    }

    override fun onAccessScan(tag: String) {
        over_rfid_in.setText(tag)
    }

}