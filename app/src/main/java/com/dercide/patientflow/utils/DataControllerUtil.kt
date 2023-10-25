package com.dercide.patientflow.utils

import android.content.Context
import android.widget.Toast
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.models.Patient
import com.dercide.patientflow.network.ApiHandler

class DataControllerUtil {
    companion object {
        fun getPatients(context: Context) {
            ApiHandler(context).sendRequestGet("/patients", "", {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                (it.data as List<List<String>>).first().first()
                MainActivity.patiets.clear()
                for (element: List<String> in it.data) {

                    MainActivity.patiets.add(Patient(element[0].toInt(), element[1], element[2], element[3], element[4], if(element[5] == "null") {null} else {element[5]}))
                }
            },
                {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    //on error
                })
        }
    }
}