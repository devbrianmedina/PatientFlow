package com.dercide.patientflow.ui.queries

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R

class QueriesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_queries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addRegistry: Button = view.findViewById(R.id.btnAddRegistry)
        addRegistry.setOnClickListener {
            MainActivity.navController?.popBackStack()
            MainActivity.navController?.navigate(R.id.nav_registry)
        }
    }
}