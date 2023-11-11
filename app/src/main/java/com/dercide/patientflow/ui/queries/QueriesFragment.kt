package com.dercide.patientflow.ui.queries

import android.content.Intent
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
import androidx.core.util.Pair
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dercide.patientflow.MainActivity
import com.dercide.patientflow.R
import com.dercide.patientflow.adapters.PatientAdapter
import com.dercide.patientflow.adapters.QueryAdapter
import com.dercide.patientflow.models.Patient
import com.dercide.patientflow.network.ApiHandler
import com.dercide.patientflow.ui.attend.AttendActivity
import com.dercide.patientflow.ui.dialogs.DateTimeDialog
import com.dercide.patientflow.ui.dialogs.PatientDialog
import com.dercide.patientflow.utils.DataControllerUtil
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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
            val constraintsBuilderRange = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointBackward.now());
            val dateRangePicker =
                MaterialDatePicker.Builder.dateRangePicker()
                    .setTitleText("Selecciona un rango")
                    .setSelection(
                        Pair(
                            MaterialDatePicker.todayInUtcMilliseconds(),
                            MaterialDatePicker.todayInUtcMilliseconds()
                        )
                    )
                    .setCalendarConstraints(constraintsBuilderRange.build())
                    .build()

            dateRangePicker.addOnPositiveButtonClickListener {
                val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                dateFormat.timeZone = TimeZone.getTimeZone("UTC")
                tilFrom.editText?.setText("${dateFormat.format(it.first)} - ${dateFormat.format(it.second)}")
                DataControllerUtil.getQueries(requireContext(), "?get=range&date=${URLEncoder.encode("${dateFormat.format(it.first)} 00:00:00", "UTF-8")}&date2=${URLEncoder.encode("${dateFormat.format(it.second)} 23:59:59", "UTF-8")}")
                DataControllerUtil.getPrescriptions(requireContext(), "?get=range&date=${URLEncoder.encode("${dateFormat.format(it.first)} 00:00:00", "UTF-8")}&date2=${URLEncoder.encode("${dateFormat.format(it.second)} 23:59:59", "UTF-8")}")
            }
            dateRangePicker.show(childFragmentManager, "tag")
        }

        //recycler
        val rvQueries: RecyclerView = view.findViewById(R.id.rvQueries)

        queriesAdapter = QueryAdapter(MainActivity.queries, { query ->
            if(query.status != 3) {
                val data = HashMap<String, String>()
                data["status"] = if(query.status == 2) { "1" } else { "2" }
                ApiHandler(requireContext()).sendRequestPut(data, "/queries/${query.idQueries}", {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    val res = "${it.data.first()}".toInt()
                    if(res == 1) {
                        query.status = if(query.status == 2) { 1 } else { 2 }
                        queriesAdapter.notifyDataSetChanged()
                    }
                }, {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                })
            }
        }, {
            val intent:Intent = Intent(requireContext(), AttendActivity::class.java)
            intent.putExtra("idQuery", it.idQueries)
            startActivity(intent)
        })
        val linearLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        rvQueries.layoutManager = linearLayoutManager
        rvQueries.adapter = queriesAdapter

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