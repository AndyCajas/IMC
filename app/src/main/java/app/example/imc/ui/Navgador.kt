package app.example.imc.ui

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.example.imc.R
import java.net.URLEncoder

class Navgador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_navgador)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Obtener la referencia del WebView
        val webView: WebView = findViewById(R.id.navegador)

        // Habilitar JavaScript
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true

        // Configurar WebViewClient
        webView.webViewClient = WebViewClient()
        // Cambiar el nombre de la variable para evitar confusión
        val extraValue =
            intent.getStringExtra("key") ?: ""  // Obtén el valor del Intent con la clave "key"

        val buscarYoutube=intent.getStringExtra("youtube")?:""

        // Verificar si el valor obtenido no está vacío
        if (buscarYoutube.isNotEmpty()) {
            val encodedValue = URLEncoder.encode(buscarYoutube, "UTF-8")

            // Crear la URL para la búsqueda en YouTube
            val url = "https://www.youtube.com/results?search_query=$encodedValue"

            // Cargar la URL en WebView
            webView.loadUrl(url)
        }

        // Verificar si el valor obtenido no está vacío
        if (extraValue.isNotEmpty()) {
            // Cargar la URL en WebView
            val url = "https://www.google.com/search?q=$extraValue"

            // Cargar la URL en WebView
            webView.loadUrl(url)
        }


        // Cargar la URL

    }
    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.navegador)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}