package com.dercide.patientflow.ui.patient

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.core.view.setPadding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.dercide.patientflow.R

class PatientDialog {
    companion object {
        fun add(context: Activity, callback: (Boolean) -> Unit) {
            MaterialDialog(context).show {
                title(res = R.string.add_patient)
                customView(viewRes = R.layout.add_patient_view, scrollable = true)
                val root: View = getCustomView()
                positiveButton {
                    callback(true)
                }
                negativeButton {
                    callback(false)
                }
            }
        }
    }
}