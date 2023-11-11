package com.dercide.patientflow.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.models.Patient
import com.dercide.patientflow.models.Query
import de.hdodenhof.circleimageview.CircleImageView

class QueryAdapter(queries:ArrayList<Query>, val onItemEdit: (Query) -> Unit, val onItemClick: (Query) -> Unit) : RecyclerView.Adapter<QueryAdapter.QueryViewHolder>() {

    var queries: ArrayList<Query>
    init {
        this.queries = queries
    }

    class QueryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name:TextView
        val surnames:TextView
        val edit:ImageView
        val status:TextView
        init {
            name = view.findViewById(R.id.tvNameQueryCard)
            surnames = view.findViewById(R.id.tvSurnamesQueryCard)
            edit = view.findViewById(R.id.ivEditQueryCard)
            status = view.findViewById(R.id.tvStatusQueryCard)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueryViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.query_card, parent, false)
        return QueryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return queries.size
    }

    override fun onBindViewHolder(holder: QueryAdapter.QueryViewHolder, position: Int) {
        val patient:Patient = MainActivity.patiets.first { it.idPatient == queries[position].patients_idPatient }
        holder.name.text = patient.name
        holder.surnames.text = patient.surnames
        holder.status.text = when(queries[position].status) {
            1 -> "En espera"
            2 -> "Abandono"
            3 -> "Atendido"
            else -> "Desconocido"
        }
        if(queries[position].status == 3) {
            holder.edit.visibility = View.GONE
        } else {
            holder.edit.visibility = View.VISIBLE
            holder.edit.setOnClickListener {
                onItemEdit(queries[position])
            }
        }
        if(queries[position].status == 2) {
            holder.edit.setImageResource(R.drawable.ic_return)
        } else {
            holder.edit.setImageResource(R.drawable.ic_exit)
        }
        holder.itemView.setOnClickListener {
            onItemClick(queries[position])
        }
    }
}