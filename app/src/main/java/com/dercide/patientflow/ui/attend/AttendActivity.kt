package com.dercide.patientflow.ui.attend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.models.Patient
import com.dercide.patientflow.models.Prescription
import com.dercide.patientflow.models.Query
import com.dercide.patientflow.network.ApiHandler
import com.dercide.patientflow.ui.queries.QueriesFragment
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.system.exitProcess

class AttendActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attend)

        val idQuery = intent.extras?.getInt("idQuery")

        val query:Query? = MainActivity.queries.firstOrNull { it.idQueries == idQuery }
        query ?: finish()
        val patient: Patient? = MainActivity.patiets.firstOrNull { it.idPatient == query!!.patients_idPatient }

        val tvName:TextView = findViewById(R.id.tvNameAttend)
        val tvAge:TextView = findViewById(R.id.tvAgeAttend)
        val tvPhone:TextView = findViewById(R.id.tvPhoneAttend)
        val tvWeight:TextView = findViewById(R.id.tvWeightAttend)
        val tvPressure:TextView = findViewById(R.id.tvPressureAttend)
        val tvTemperature:TextView = findViewById(R.id.tvTemperatureAttend)
        val tvSurgery:TextView = findViewById(R.id.tvSurgeryAttend)
        val tvSelfMedication:TextView = findViewById(R.id.tvSelfMedicationAttend)
        val tvIllnessesOrAllergies:TextView = findViewById(R.id.tvIllnessesOrAllergiesAttend)

        tvName.text = "${patient!!.name} ${patient.surnames}"
        tvAge.text = calcularEdad(patient.birthdate).toString()
        tvPhone.text = patient.phone
        tvWeight.text = query!!.weight.toString()
        tvPressure.text = query.pressure
        tvTemperature.text = query.temperature.toString()
        tvSurgery.text = if(query.currentsurgery) { "Si" } else { "No" }
        tvSelfMedication.text = query.selfmedication
        tvIllnessesOrAllergies.text = query.diseasesandallergies

        val tilRecommendations:TextInputLayout = findViewById(R.id.tilRecommendationsAttend)
        val tilMedicines:TextInputLayout = findViewById(R.id.tilMedicinesAttend)
        val btnAlta:Button = findViewById(R.id.btnAltaAttend)

        if(query.prescription_idprescription != null) {
            val prescription:Prescription? = MainActivity.prescriptions.firstOrNull { it.idPrescription == query.prescription_idprescription }
            tilRecommendations.editText!!.setText(prescription?.observations)
            tilRecommendations.editText!!.isEnabled = false
            tilRecommendations.editText!!.isFocusable = false
            tilMedicines.editText!!.setText(prescription?.medicines)
            tilMedicines.editText!!.isEnabled = false
            tilMedicines.editText!!.isFocusable = false
            btnAlta.text = "Salir"
        }

        btnAlta.setOnClickListener {
            if(query.prescription_idprescription != null) {
                finish()
            } else {
                val data = HashMap<String, String>()
                data["observations"] = tilRecommendations.editText!!.text.toString()
                data["medicines"] = tilMedicines.editText!!.text.toString()
                data["query_id"] = "${query.idQueries}"

                ApiHandler(applicationContext).sendRequestPost(data, "/prescriptions", {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()
                    val idPrescription = "${it.data.first()}".toInt()
                    if(idPrescription != -1) { //MainActivity.queries.add(Query(idQuerie, it.data.last().toString(), data["weight"]!!.toDouble(), data["pressure"]!!, data["temperature"]!!.toDouble(), data["currentsurgery"]!!.toBoolean(), data["selfmedication"]!!, data["diseasesandallergies"]!!, 1, null, data["idPatient"]!!.toInt()))
                        query.status = 3
                        query.prescription_idprescription = idPrescription
                        QueriesFragment.queriesAdapter?.notifyDataSetChanged()
                        MainActivity.prescriptions.add(Prescription(idPrescription, data["observations"]!!, data["medicines"]!!, SimpleDateFormat("yyyy-MM-dd").format(Date())))
                        finish()
                    }
                }, {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                })
            }
        }
    }

    fun calcularEdad(fechaNacimiento: String): Int {
        try {
            val formato = SimpleDateFormat("yyyy-MM-dd")
            val fechaNacimientoDate = formato.parse(fechaNacimiento)
            val fechaActual = Date()

            val edadMillis = fechaActual.time - fechaNacimientoDate.time
            val edadAnios = edadMillis / (1000L * 60 * 60 * 24 * 365)

            return edadAnios.toInt()
        } catch (e: Exception) {
            e.printStackTrace()
            return -1
        }
    }
}