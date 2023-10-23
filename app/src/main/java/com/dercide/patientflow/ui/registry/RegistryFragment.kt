package com.dercide.patientflow.ui.registry

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dercide.patientflow.R
import com.dercide.patientflow.ui.dialogs.PatientDialog

class RegistryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registry, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addPatient:Button = view.findViewById(R.id.btnAddPatient)
        addPatient.setOnClickListener {
            PatientDialog.add(requireActivity()) {
            }
        }
    }
}