package com.dercide.patientflow.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dercide.patientflow.models.ApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ApiHandler(context: Context) {
    val queue = Volley.newRequestQueue(context)
    val url = "http://192.168.3.86/index.php"
    val gson = Gson()

    fun sendRequestGet(endPoint: String, params:String = "", response: (ApiResponse) -> Unit, error: (String) -> Unit) {
        // Define un TypeToken para ApiResponse
        val type = object : TypeToken<ApiResponse>() {}.type
        val stringRequest = StringRequest(
            Request.Method.GET, "$url$endPoint$params",
            {
                response(gson.fromJson(it, type))
            },
            {
                val responseError = String(it.networkResponse.data)
                error(responseError)
            }
        )

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    fun sendRequestPost(data: HashMap<String, String>, endPoint: String, response: (ApiResponse) -> Unit, error: (String) -> Unit) {
        // Define un TypeToken para ApiResponse
        val type = object : TypeToken<ApiResponse>() {}.type
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$url$endPoint",
            {
                response(gson.fromJson(it, type))
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

    fun sendRequestPut(data: HashMap<String, String>, endPoint: String, response: (ApiResponse) -> Unit, error: (String) -> Unit) {
        // Define un TypeToken para ApiResponse
        val type = object : TypeToken<ApiResponse>() {}.type
        val stringRequest = object : StringRequest(
            Request.Method.PUT, "$url$endPoint",
            {
                response(gson.fromJson(it, type))
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