package com.dercide.patientflow.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ApiHandler(context: Context) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://192.168.100.4/index.php"

    fun sendRequestGet(endPoint: String, params:String = "", response: (String) -> Unit, error: (String) -> Unit) {
        val stringRequest = StringRequest(
            Request.Method.GET, "$url$endPoint$params",
            {
                response(it)
            },
            {
                val responseError = String(it.networkResponse.data)
                error(responseError)
            }
        )

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    fun sendRequestPost(data: HashMap<String, String>, endPoint: String, response: (String) -> Unit, error: (String) -> Unit) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$url$endPoint",
            {
                response(it)
            },
            {
                val responseError = String(it.networkResponse.data)
                error(responseError)
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return data
            }
        }

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}