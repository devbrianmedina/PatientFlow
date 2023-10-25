package com.dercide.patientflow.ui.registry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.ui.dialogs.PatientDialog
import com.google.android.material.textfield.TextInputLayout


class RegistryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addPatient:Button = view.findViewById(R.id.btnAddPatientRegistry)
        addPatient.setOnClickListener {
            PatientDialog.add(requireActivity()) {
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


    }
}