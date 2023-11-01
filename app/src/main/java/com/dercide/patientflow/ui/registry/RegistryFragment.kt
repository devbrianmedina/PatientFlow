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

        val addPatient:Button = view.findViewById(R.id.btnAddPatientRegistry)
        addPatient.setOnClickListener {
            PatientDialog.add(requireActivity(), childFragmentManager) {
            }
        }

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

        val tilWeight:TextInputLayout = view.findViewById(R.id.tilWeightRegistry)
        val tilSystolicPressure:TextInputLayout = view.findViewById(R.id.tilSystolicPressureRegistry)
        val tilDiastolicPressure:TextInputLayout = view.findViewById(R.id.tilDiastolicPressureRegistry)
        val tilTemperature:TextInputLayout = view.findViewById(R.id.tilTemperatureRegistry)
        val cbSurgery:CheckBox = view.findViewById(R.id.cbSurgeryRegistry)
        val tilSelfMedication:TextInputLayout = view.findViewById(R.id.tilSelfMedicationRegistry)
        val tilIllnessesOrAllergies:TextInputLayout = view.findViewById(R.id.tilIllnessesOrAllergiesRegistry)
        val btnRegister:Button = view.findViewById(R.id.btnRegisterRegistry)

        btnRegister.setOnClickListener {
            if(idP != -1) {
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
            } else {
                Toast.makeText(requireContext(), "Selecciona un paciente", Toast.LENGTH_SHORT).show()
            }
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