package app.example.imc.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.example.imc.R
import app.example.imc.data.IMC
import app.example.imc.data.User
import app.example.imc.databinding.ActivityCalcularImcBinding
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.net.URLEncoder

class CalcularIMC : AppCompatActivity() {
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private lateinit var currentUser: User

    private lateinit var binding: ActivityCalcularImcBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCalcularImcBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getCurrentUser()
        binding.btnCalcularIMC.setOnClickListener { calcularIMC() }
        binding.btnBuscar.setOnClickListener { buscarGoogle() }
        binding.btnBuscarYoutube.setOnClickListener { buscarYoutube() }



        binding.navgadorImagenes.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            loadWithOverviewMode = true
            useWideViewPort = true
            loadsImagesAutomatically = true // Asegura la carga automática de imágenes
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // Permite contenido mixto
        }

        binding.navgadorImagenes.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false // Todas las URLs se manejan dentro del WebView
            }
        }
        binding.imageSearch.setOnClickListener { seleccionarImagen() }
        binding.salir.setOnClickListener{salir()}

    }
    private fun salir(){
        firebaseAuth.signOut()
        val googleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)
        googleSignInClient.signOut().addOnCompleteListener {
            Toast.makeText(this, "Sesión de Google cerrada", Toast.LENGTH_SHORT).show()
        }
        startActivity(Intent(this,IniciarSesion::class.java))
        finish()
    }

    private fun calcularIMC() {
        binding.layoutCalcular.visibility = View.VISIBLE
        val peso = binding.peso.text.toString().trim()
        val estatura = binding.estatura.text.toString().trim()
        if (!peso.isNullOrEmpty() && !estatura.isNullOrEmpty()) {
            val ca = Math.pow(estatura.toDouble(), 2.0)
            val calculo = peso.toDouble() / ca

            val imc = IMC(calculo.toFloat())
            val estado = imc.estado()
            val sugerencia = imc.sugerencias()
            binding.estadoImc.setText(estado)
            binding.descripcionImc.setText(sugerencia)
            val consulta = "imagenes sobre el IMC estado $estado"

            val encodedValue = URLEncoder.encode(consulta, "UTF-8")
            val url = "https://www.google.com/search?tbm=isch&q=$encodedValue"
            binding.navgadorImagenes.loadUrl(url)


        } else {
            Toast.makeText(this, "Error completa los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentUser() {
        val id = firebaseAuth.currentUser?.uid ?: ""
        val userRef = databaseReference.child("users")

        userRef.child(id!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Mapear los datos a un objeto User
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        currentUser = it
                    }

                    user?.let {
                        binding.tvNombreUsuario.setText("Hola ${it.nombre}")
                        Glide.with(this@CalcularIMC).load(it.imagen).into(binding.imageUser)
                    }
                } else {
                    Log.d("Firebase", "El usuario no existe")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al leer datos: ${error.message}")
            }
        })

    }


    private fun buscarGoogle() {
        val buscar = binding.buscarGoogle.text.toString().trim()
        if (buscar.isNullOrEmpty()) return

        val intent = Intent(this, Navgador::class.java)
        intent.putExtra("key", buscar)
        startActivity(intent)
    }

    private fun buscarYoutube() {
        val buscar = binding.buscarYoutube.text.toString().trim()
        if (buscar.isNullOrEmpty()) return

        val intent = Intent(this, Navgador::class.java)
        intent.putExtra("youtube", buscar)
        startActivity(intent)
    }

    override fun onBackPressed() {
        val webView: WebView = findViewById(R.id.navgadorImagenes)
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private fun seleccionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        register.launch(intent)
    }

    val register = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val intent = it.data
            val img = intent?.data
            Glide.with(this).load(img).into(binding.imageUser)
            val newUser = currentUser.copy(imagen = img.toString())

            val userRef = databaseReference.child("users").child(newUser.id)
            userRef.setValue(newUser).addOnSuccessListener {
                Toast.makeText(this, "imagen actualizda", Toast.LENGTH_SHORT)
                    .show()

            }

        }
    }

}