package com.example.rfidtab.ui.task.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.rfidtab.R
import com.example.rfidtab.service.db.entity.task.OverCardsEntity
import com.example.rfidtab.ui.task.TaskViewModel
import com.example.rfidtab.util.DataTransfer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.TextInputEditText
import com.senter.support.openapi.StUhf
import kotlinx.android.synthetic.main.fragment_task_add_over.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.random.Random

class TaskAddOverFragment(val taskId: Int) : BottomSheetDialogFragment() {
    private val viewModel: TaskViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

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
                readTag(over_rfid_in)
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

    // чтение RFID метки на кнопку скан
    private fun readTag(resultView: TextInputEditText) {
        var tag = String()
        val bank = StUhf.Bank.TID
        val ptr = 0
        val cnt = 1
        val pwd = "00000000"
        val acsPass = StUhf.AccessPassword.getNewInstance(DataTransfer.getBytesByHexString(pwd))

        try {
            val iso18k6c: StUhf.InterrogatorModelDs.UmdOnIso18k6cRead =
                object : StUhf.InterrogatorModelDs.UmdOnIso18k6cRead() {
                    override fun onFailed(error: StUhf.InterrogatorModelDs.UmdErrorCode) {
                        activity!!.runOnUiThread {
                            Toast.makeText(context, "Нет метки!!!", Toast.LENGTH_LONG)
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

                        activity!!.runOnUiThread {
                            if (tag.isNotEmpty()) {
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
            Toast.makeText(context, "Нет метки!", Toast.LENGTH_SHORT).show()
            Log.w("RFID SCANNER", e.message)
        }
    }

}