package com.dercide.patientflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dercide.patientflow.R
import com.dercide.patientflow.models.Patient
import de.hdodenhof.circleimageview.CircleImageView

class PatientAdapter(patients:ArrayList<Patient>, val onItemClick: (Patient) -> Unit) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    var patients: ArrayList<Patient>
    init {
        this.patients = patients
    }

    class PatientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image:CircleImageView
        val name:TextView
        val surnames:TextView
        init {
            image = view.findViewById(R.id.civImagePatientCard)
            name = view.findViewById(R.id.tvNamePatientCard)
            surnames = view.findViewById(R.id.tvSurnamesPatientCard)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.patient_card, parent, false)
        return PatientViewHolder(view)
    }

    override fun getItemCount(): Int {
        return patients.size
    }

    override fun onBindViewHolder(holder: PatientAdapter.PatientViewHolder, position: Int) {
        holder.name.text = patients[position].name
        holder.surnames.text = patients[position].surnames
        holder.itemView.setOnClickListener {
            onItemClick(patients[position])
        }
    }
}