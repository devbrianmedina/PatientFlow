package com.dercide.patientflow.ui.dialogs

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.core.util.Pair
import androidx.core.view.setPadding
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.models.Patient
import com.dercide.patientflow.network.ApiHandler
import com.dercide.patientflow.utils.DataControllerUtil
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class PatientDialog {
    companion object {
        fun add(context: Activity, fragmentManager:FragmentManager, patient: Patient? = null, callback: (Boolean) -> Unit) {
            MaterialDialog(context).show {
                title(res = if(patient == null) { R.string.add_patient } else { R.string.update_patient } )
                customView(viewRes = R.layout.add_patient_view, scrollable = true)
                val root: View = getCustomView()
                val tilName: TextInputLayout = root.findViewById(R.id.tilNameAddPatient)
                val tilSurnames: TextInputLayout = root.findViewById(R.id.tilSurnamesAddPatient)
                val tilBirthdate: TextInputLayout = root.findViewById(R.id.tilBirthdateAddPatient)
                val tilPhone: TextInputLayout = root.findViewById(R.id.tilPhoneAddPatient)
                var imageUrl: String? = null

                tilBirthdate.editText!!.setOnClickListener {
                    /*DateTimeDialog.datePicker(context) {
                        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                    }*/
                    val constraintsBuilderRange = CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointBackward.now());
                    val dateRangePicker =
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Selecciona un rango")
                            .setCalendarConstraints(constraintsBuilderRange.build())
                            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                            .build()

                    dateRangePicker.addOnPositiveButtonClickListener {
                        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                        tilBirthdate.editText!!.setText(dateFormat.format(it))
                    }
                    dateRangePicker.show(fragmentManager, "tag")
                }

                //establecer datos si se va a actualizar (si patient no es null)
                if(patient != null) {
                    tilName.editText!!.setText(patient.name)
                    tilSurnames.editText!!.setText(patient.surnames)
                    tilBirthdate.editText!!.setText(patient.birthdate)
                    tilPhone.editText!!.setText(patient.phone)
                    imageUrl = patient.photourl
                }

                positiveButton {
                    val data = HashMap<String, String>()
                    data["name"] = tilName.editText!!.text.toString()
                    data["surnames"] = tilSurnames.editText!!.text.toString()
                    data["birthdate"] = tilBirthdate.editText!!.text.toString()
                    data["phone"] = tilPhone.editText!!.text.toString()
                    data["photo"] = imageUrl.toString()
                    //TODO abrir dialog de load
                    if(patient == null) { //agregar nuevo paciente
                        ApiHandler(context).sendRequestPost(data, "/patients", {
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            val idPatient = "${it.data.first()}".toInt()
                            if(idPatient > 0) {
                                MainActivity.patiets.add(Patient(idPatient, data["name"]!!, data["surnames"]!!, data["birthdate"]!!, data["phone"]!!, imageUrl))
                                callback(true)
                            }
                        }, {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        })
                    } else { //actualizar paciente
                        ApiHandler(context).sendRequestPut(data, "/patients/${patient.idPatient}", {
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            val res = "${it.data.first()}".toInt()
                            if(res == 1) {
                                MainActivity.patiets.forEachIndexed { index, element ->
                                    if(element.idPatient == patient.idPatient) {
                                        MainActivity.patiets[index] = Patient(patient.idPatient, data["name"]!!, data["surnames"]!!, data["birthdate"]!!, data["phone"]!!, imageUrl)
                                        callback(true)
                                        return@forEachIndexed
                                    }
                                }
                                callback(true)
                            }
                        }, {
                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        })
                    }
                    //TODO cerrar dialog de load
                }
                negativeButton {
                    callback(false)
                }
            }
        }
    }
}