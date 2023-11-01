package com.dercide.patientflow.ui.attend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.models.Patient
import com.dercide.patientflow.models.Query
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
        var patient: Patient? = MainActivity.patiets.firstOrNull { it.idPatient == query!!.patients_idPatient }

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