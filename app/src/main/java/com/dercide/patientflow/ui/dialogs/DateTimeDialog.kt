package com.dercide.patientflow.ui.dialogs

import android.app.Activity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.datetime.datePicker
import java.util.Calendar

class DateTimeDialog {
    companion object {
        fun datePicker(activity: Activity, callback: (Calendar) -> Unit) {
            MaterialDialog(activity).show {
                datePicker { dialog, datetime ->
                    callback(datetime)
                }
            }
        }
    }
}