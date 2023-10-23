package com.dercide.patientflow.ui.dialogs

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.core.view.setPadding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.dercide.patientflow.R
import com.dercide.patientflow.network.ApiHandler
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.text.SimpleDateFormat

class PatientDialog {
    companion object {
        fun add(context: Activity, callback: (Boolean) -> Unit) {
            MaterialDialog(context).show {
                title(res = R.string.add_patient)
                customView(viewRes = R.layout.add_patient_view, scrollable = true)
                val root: View = getCustomView()
                val tilName: TextInputLayout = root.findViewById(R.id.tilNameAddPatient)
                val tilSurnames: TextInputLayout = root.findViewById(R.id.tilSurnamesAddPatient)
                val tilBirthdate: TextInputLayout = root.findViewById(R.id.tilBirthdateAddPatient)
                val tilPhone: TextInputLayout = root.findViewById(R.id.tilPhoneAddPatient)
                val imageUrl: String? = null
                positiveButton {
                    val dateFormat = SimpleDateFormat("dd-MM-yyyy")
                    val date = dateFormat.parse(tilBirthdate.editText!!.text.toString())
                    val timestamp = date?.time ?: 0
                    val postData = JSONObject()
                    postData.put("action", "insertPatient")
                    postData.put("name", tilName.editText!!.text)
                    postData.put("surnames", tilSurnames.editText!!.text)
                    postData.put("birthdate", timestamp)
                    postData.put("phone", tilPhone.editText!!.text)
                    postData.put("photourl", imageUrl)
                    //TODO abrir dialog de load
                    ApiHandler(context).sendRequest("patients", postData, {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        //code here
                    })
                    //TODO cerrar dialog de load
                    callback(true)
                }
                negativeButton {
                    callback(false)
                }
            }
        }
    }
}