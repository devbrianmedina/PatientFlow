package com.dercide.patientflow.network

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dercide.patientflow.models.ApiResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URLEncoder

class ApiHandler(context: Context) {
    val queue = Volley.newRequestQueue(context)
    val url = "https://patientflow.dercide.com/index.php"
    val gson = Gson()

    fun sendRequestGet(endPoint: String, params:String = "", response: (ApiResponse) -> Unit, error: (String) -> Unit) {
        // Define un TypeToken para ApiResponse
        val type = object : TypeToken<ApiResponse>() {}.type
        val stringRequest = StringRequest(
            Request.Method.GET, "$url$endPoint$params",
            {
                println("### $it")
                response(gson.fromJson(it, type))
            },
            {
                println("### $it")
                val responseError = String(it.networkResponse.data)
                error(responseError)
            }
        )
        queue.add(stringRequest)
    }

    fun sendRequestPost(data: HashMap<String, String>, endPoint: String, response: (ApiResponse) -> Unit, error: (String) -> Unit) {
        // Define un TypeToken para ApiResponse
        val type = object : TypeToken<ApiResponse>() {}.type
        val stringRequest = object : StringRequest(
            Request.Method.POST, "$url$endPoint",
            {
                println("### $it")
                response(gson.fromJson(it, type))
            },
            {
                println("### $it")
                val responseError = String(it.networkResponse.data)
                error(responseError)
            }
        ) {
            override fun getParams(): MutableMap<String, String> {
                return data
            }
        }
        queue.add(stringRequest)
    }

    fun sendRequestPut(data: HashMap<String, String>, endPoint: String, response: (ApiResponse) -> Unit, error: (String) -> Unit) {
        // Define un TypeToken para ApiResponse
        val type = object : TypeToken<ApiResponse>() {}.type
        val stringRequest = object : StringRequest(
            Request.Method.PUT, "$url$endPoint",
            {
                println("#### $it")
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
        queue.add(stringRequest)
    }
}