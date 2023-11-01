package com.dercide.patientflow.ui.patient

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.adapters.PatientAdapter
import com.dercide.patientflow.ui.dialogs.PatientDialog

class PatientsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patients, container, false)
    }

    lateinit var patientsAdapter: PatientAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvPatients:RecyclerView = view.findViewById(R.id.rvPatientsFragmentPatients)

        patientsAdapter = PatientAdapter(MainActivity.patiets) {
            PatientDialog.add(requireActivity(), childFragmentManager, it) {
                patientsAdapter.notifyDataSetChanged()
            }
        }
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvPatients.layoutManager = linearLayoutManager
        rvPatients.adapter = patientsAdapter
    }
}