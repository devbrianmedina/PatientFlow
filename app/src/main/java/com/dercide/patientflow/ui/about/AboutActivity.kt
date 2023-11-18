package com.dercide.patientflow.ui.about

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.dercide.patientflow.R

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Enlace a GitHub
        val imageViewGitHub: ImageView = findViewById(R.id.imageViewGitHub)
        imageViewGitHub.setOnClickListener {
            abrirEnlace("https://github.com/devbrianmedina") // Reemplaza con tu URL de GitHub
        }

        // Enlace a tu web personal
        val imageViewPersonalWeb: ImageView = findViewById(R.id.imageViewPersonalWeb)
        imageViewPersonalWeb.setOnClickListener {
            abrirEnlace("https://www.dercide.com/brian") // Reemplaza con tu URL personal
        }

        // Enlace a la web de tu empresa de desarrollo
        val imageViewCompanyWeb: ImageView = findViewById(R.id.imageViewCompanyWeb)
        imageViewCompanyWeb.setOnClickListener {
            abrirEnlace("https://www.dercide.com") // Reemplaza con la URL de tu empresa
        }
    }

    private fun abrirEnlace(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            // Manejar la excepción aquí, por ejemplo, mostrar un mensaje de error
            e.printStackTrace()
        }
    }
}