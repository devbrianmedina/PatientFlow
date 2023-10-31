package com.dercide.patientflow.ui.queries

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.adapters.PatientAdapter
import com.dercide.patientflow.adapters.QueryAdapter
import com.dercide.patientflow.ui.dialogs.DateTimeDialog
import com.dercide.patientflow.ui.dialogs.PatientDialog
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class QueriesFragment : Fragment() {

    private val handler = Handler(Looper.getMainLooper())
    lateinit var tvDateTime:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_queries, container, false)
    }

    companion object {
        lateinit var queriesAdapter: QueryAdapter
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addRegistry: Button = view.findViewById(R.id.btnAddRegistry)
        addRegistry.setOnClickListener {
            MainActivity.navController?.popBackStack()
            MainActivity.navController?.navigate(R.id.nav_registry)
        }

        tvDateTime = view.findViewById(R.id.tvDateTimeQueries)
        handler.post(updateDateTime)

        val tilFrom:TextInputLayout = view.findViewById(R.id.tilFromQueries)
        tilFrom.editText?.setOnClickListener {
            DateTimeDialog.datePicker(requireActivity()) {
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                DateTimeDialog.datePicker(requireActivity()) { it2 ->
                    tilFrom.editText?.setText("${dateFormat.format(it.time)} / ${dateFormat.format(it2.time)}")
                }
                updateQueries()
            }
        }

        //recycler
        val rvQueries: RecyclerView = view.findViewById(R.id.rvQueries)

        queriesAdapter = QueryAdapter(MainActivity.queries, {}, {})
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvQueries.layoutManager = linearLayoutManager
        rvQueries.adapter = queriesAdapter

    }

    fun updateQueries() {
    }

    private val updateDateTime = object : Runnable {
        override fun run() {
            val currentDateAndTime = SimpleDateFormat("dd/MM/yyyy - HH:mm:ss", Locale.getDefault()).format(Date())
            tvDateTime.text = currentDateAndTime
            handler.postDelayed(this, 1000)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(updateDateTime)
    }
}