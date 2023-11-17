package com.dercide.patientflow.ui.registry

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.models.Query
import com.dercide.patientflow.network.ApiHandler
import com.dercide.patientflow.ui.dialogs.PatientDialog
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RegistryFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    lateinit var tvDateTime: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvDateTime = view.findViewById(R.id.tvDateTimeRegistry)
        handler.post(updateDateTime)

        val tilPatient:TextInputLayout = view.findViewById(R.id.tilPatientRegistry)
        val actvPatient:AutoCompleteTextView = view.findViewById(R.id.actvPatientRegistry)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, MainActivity.patiets.map { "${it.name} ${it.surnames} #${it.idPatient}" }.toList())
        actvPatient.setAdapter(adapter)
        var idP:Int = -1
        actvPatient.setOnItemClickListener { parent, _, position, id ->
            val selectedItem = parent.adapter.getItem(position) as String
            idP = selectedItem.split(" ").last().replace("#", "").toInt()
            actvPatient.setText(selectedItem.replaceAfterLast("#", "").replace(" #", ""))
            Toast.makeText(requireContext(), "Paciente #$idP seleccionado", Toast.LENGTH_SHORT).show()
        }

        actvPatient.setOnFocusChangeListener { v, hasFocus ->
            if(idP != -1 && hasFocus) {
                idP = -1
                Toast.makeText(requireContext(), "Selecciona un paciente", Toast.LENGTH_SHORT).show()
                actvPatient.setText("")
                actvPatient.requestFocus()
            }
        }

        val addPatient:Button = view.findViewById(R.id.btnAddPatientRegistry)
        addPatient.setOnClickListener {
            PatientDialog.add(requireActivity(), childFragmentManager) {
                if(it) {
                    val lPatient = MainActivity.patiets.last()
                    adapter.add("${lPatient.name} ${lPatient.surnames} #${lPatient.idPatient}")
                    adapter.notifyDataSetChanged()
                }
            }
        }

        val tilWeight:TextInputLayout = view.findViewById(R.id.tilWeightRegistry)
        val tilSystolicPressure:TextInputLayout = view.findViewById(R.id.tilSystolicPressureRegistry)
        val tilDiastolicPressure:TextInputLayout = view.findViewById(R.id.tilDiastolicPressureRegistry)
        val tilTemperature:TextInputLayout = view.findViewById(R.id.tilTemperatureRegistry)
        val cbSurgery:CheckBox = view.findViewById(R.id.cbSurgeryRegistry)
        val tilSelfMedication:TextInputLayout = view.findViewById(R.id.tilSelfMedicationRegistry)
        val tilIllnessesOrAllergies:TextInputLayout = view.findViewById(R.id.tilIllnessesOrAllergiesRegistry)
        val btnRegister:Button = view.findViewById(R.id.btnRegisterRegistry)

        actvPatient.addTextChangedListener {
            if(idP == -1) {
                tilPatient.error = "Seleccione un paciente válido"
            } else {
                tilPatient.error = null
                tilWeight.requestFocus()
            }
        }

        tilWeight.editText!!.addTextChangedListener {
            if(!Regex("""^\d{1,3}(\.\d{1,2})?${'$'}""").matches(it.toString())) {
                tilWeight.error = "Introduce un peso válido"
            } else {
                tilWeight.error = null
            }
        }

        tilSystolicPressure.editText!!.addTextChangedListener {
            if(!Regex("""^[1-9]\d{0,2}${'$'}""").matches(it.toString())) {
                tilSystolicPressure.error = "Introduzca una presión válida"
            } else {
                tilSystolicPressure.error = null
            }
        }

        tilDiastolicPressure.editText!!.addTextChangedListener {
            if(!Regex("""^[1-9]\d{0,2}${'$'}""").matches(it.toString())) {
                tilDiastolicPressure.error = "Introduzca una presión válida"
            } else {
                tilDiastolicPressure.error = null
            }
        }

        tilTemperature.editText!!.addTextChangedListener {
            if(!Regex("""^\d{1,3}(\.\d)?${'$'}|^\.\d{1,3}${'$'}""").matches(it.toString())) {
                tilTemperature.error = "Introduzca una temperatura válida"
            } else {
                tilTemperature.error = null
            }
        }

        btnRegister.setOnClickListener {
            var returnBool = false
            if(idP == -1) {
                Toast.makeText(requireContext(), "Selecciona un paciente", Toast.LENGTH_SHORT).show()
                tilPatient.error = "Seleccione un paciente válido"
                actvPatient.requestFocus()
                returnBool = true
            }
            if(!Regex("""^\d{1,3}(\.\d{1,2})?${'$'}""").matches(tilWeight.editText!!.text)) {
                tilWeight.error = "Introduce un peso válido"
                returnBool = true
            }
            if(!Regex("""^[1-9]\d{0,2}${'$'}""").matches(tilSystolicPressure.editText!!.text)) {
                tilSystolicPressure.error = "Introduzca una presión válida"
                returnBool = true
            }
            if(!Regex("""^[1-9]\d{0,2}${'$'}""").matches(tilDiastolicPressure.editText!!.text)) {
                tilDiastolicPressure.error = "Introduzca una presión válida"
                returnBool = true
            }
            if(!Regex("""^\d{1,3}(\.\d)?${'$'}|^\.\d{1,3}${'$'}""").matches(tilTemperature.editText!!.text)) {
                tilTemperature.error = "Introduzca una temperatura válida"
                returnBool = true
            }
            if(returnBool) {
                return@setOnClickListener
            }

            val data = HashMap<String, String>()
            data["idPatient"] = idP.toString()
            data["weight"] = tilWeight.editText!!.text.toString()
            data["pressure"] = "${tilSystolicPressure.editText!!.text}/${tilDiastolicPressure.editText!!.text}"
            data["temperature"] = tilTemperature.editText!!.text.toString()
            data["currentsurgery"] = cbSurgery.isChecked.toString()
            data["selfmedication"] = tilSelfMedication.editText!!.text.toString()
            data["diseasesandallergies"] = tilIllnessesOrAllergies.editText!!.text.toString()

            ApiHandler(requireContext()).sendRequestPost(data, "/queries", {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                val idQuerie = "${it.data.first()}".toInt()
                if(idQuerie > 0) MainActivity.queries.add(Query(idQuerie, it.data.last().toString(), data["weight"]!!.toDouble(), data["pressure"]!!, data["temperature"]!!.toDouble(), data["currentsurgery"]!!.toBoolean(), data["selfmedication"]!!, data["diseasesandallergies"]!!, 1, null, data["idPatient"]!!.toInt()))
            }, {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            })
            idP = -1
            actvPatient.setText("")
            tilWeight.editText!!.setText("")
            tilSystolicPressure.editText!!.setText("")
            tilDiastolicPressure.editText!!.setText("")
            tilTemperature.editText!!.setText("")
            cbSurgery.isChecked = false
            tilSelfMedication.editText!!.setText("")
            tilIllnessesOrAllergies.editText!!.setText("")
            tilPatient.error = null
            tilWeight.error = null
            tilSystolicPressure.error = null
            tilDiastolicPressure.error = null
            tilTemperature.error = null
            actvPatient.requestFocus()
        }
    }

    private val updateDateTime = object : Runnable {
        override fun run() {
            val currentDateAndTime = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault()).format(
                Date()
            )
            tvDateTime.text = currentDateAndTime
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateDateTime)
    }
}