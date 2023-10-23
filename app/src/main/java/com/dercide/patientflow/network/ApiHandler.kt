package com.dercide.patientflow.network

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.dercide.patientflow.models.ApiResponse
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class ApiHandler(val context: Context) {
    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val apiUrl = "https://patientflow.dercide.com"
    val gson = Gson()
    fun sendRequest(endPoint: String, postData: JSONObject, onSuccess: (ApiResponse) -> Unit, onError: (ApiResponse) -> Unit) {
        // Crear un objeto JSONObject con los datos que deseas enviar en la solicitud POST

        // Crear una solicitud POST
        val stringRequest = object : StringRequest(Request.Method.POST, "$apiUrl/$endPoint",
            Response.Listener<String> { response ->
                // Manejar la respuesta exitosa aquí
                println("Respuesta: $response")
                //val apiResponse = gson.fromJson(response, ApiResponse::class.java)
                onSuccess(ApiResponse(true, "", listOf()))
            },
            Response.ErrorListener { error ->
                // Manejar errores en la solicitud

                val responseBody = String(error.networkResponse.data)
                println("Respuesta: $responseBody")
                //val apiResponse = gson.fromJson(responseBody, ApiResponse::class.java)
                onError(ApiResponse(true, "", listOf()))
            }) {

            override fun getBody(): ByteArray {
                // Convierte el objeto JSONObject a una matriz de bytes
                return postData.toString().toByteArray()
            }
        }

        requestQueue.add(stringRequest)
    }
}
