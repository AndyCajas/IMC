package app.example.imc.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.example.imc.R
import app.example.imc.data.User
import app.example.imc.databinding.ActivityRegistroUsuarioBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistroUsuario : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUsuarioBinding
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
   private val user by lazy { User() }
    val database = FirebaseDatabase.getInstance()
    val databaseReference = database.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.btnRegistrar.setOnClickListener {
            registerUser()
        }
        binding.imageSearch.setOnClickListener { buscarImagen() }
        binding.tvTengoCuenta.setOnClickListener {
            startActivity(Intent(this,IniciarSesion::class.java))
        }
    }
    private fun buscarImagen(){
        val intent=Intent(Intent.ACTION_GET_CONTENT)
        intent.type="image/*"
        register.launch(intent)
    }
    val register=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode ==Activity.RESULT_OK){
            val intent=it.data
           val  img=intent?.data
            Glide.with(this).load(img).into(binding.imageUser)
            user.imagen= img.toString()

        }
    }

    fun registerUser() {
        if (validarEntradaDatos() != null) {
            firebaseAuth.createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val id = firebaseAuth.currentUser?.uid
                        id?.let {
                            user.id = it
                        }
                        val userRef = databaseReference.child("users").child(user.id)
                        userRef.setValue(user).addOnSuccessListener {
                            Toast.makeText(this, "usuario guardado con exito", Toast.LENGTH_SHORT)
                                .show()
                            startActivity(Intent(this,CalcularIMC::class.java))
                            finish()

                        }


                    } else {
                        Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    private fun validarEntradaDatos(): User? {
        val nombre = binding.nombreUsuario.text.toString().trim()
        val apellido = binding.apellidoUsuario.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        return if (!nombre.isNullOrEmpty() && !apellido.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            user.nombre=nombre
            user.apellido=apellido
            user.email=email
            user.password=password
            user
        } else {
            null
        }

    }

}