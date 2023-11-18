package com.dercide.patientflow.utils

import android.content.Context
import android.widget.Toast
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.models.Patient
import com.dercide.patientflow.models.Prescription
import com.dercide.patientflow.models.Query
import com.dercide.patientflow.network.ApiHandler
import com.dercide.patientflow.ui.queries.QueriesFragment

class DataControllerUtil {
    companion object {
        fun getPatients(context: Context) {
            ApiHandler(context).sendRequestGet("/patients", "", {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                MainActivity.patiets.clear()
                for (element: List<String> in it.data as List<List<String>>) {

                    MainActivity.patiets.add(Patient(element[0].toInt(), element[1], element[2], element[3], element[4], if(element[5] == "null") {null} else {element[5]}))
                }
            },
                {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
        }

        fun getQueries(context: Context, params:String) {
            ApiHandler(context).sendRequestGet("/queries", params, {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                MainActivity.queries.clear()
                for (element: List<String> in it.data as List<List<String>>) {
                    MainActivity.queries.add(Query(element[0].toInt(), element[1], element[2].toDouble(), element[3], element[4].toDouble(), element[5] == "1", element[6], element[7], element[8].toInt(), if(!element[9].isNullOrEmpty()) { element[9].toInt() } else { null }, element[10].toInt()))
                }
                if(QueriesFragment.queriesAdapter != null) QueriesFragment.queriesAdapter.notifyDataSetChanged()
            },
                {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
        }

        fun getPrescriptions(context: Context, params:String) {
            ApiHandler(context).sendRequestGet("/prescriptions", params, {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                MainActivity.prescriptions.clear()
                for (element: List<String> in it.data as List<List<String>>) {
                    MainActivity.prescriptions.add(Prescription(element[0].toInt(), element[1], element[2], element[3]))
                }
            },
                {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
        }
    }
}