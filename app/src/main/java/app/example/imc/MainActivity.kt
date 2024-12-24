package app.example.imc

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.example.imc.ui.CalcularIMC
import app.example.imc.ui.IniciarSesion
import com.google.firebase.auth.FirebaseAuth
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        irNuevaPantalla()

    }
    private fun irNuevaPantalla(){
        val handler= android.os.Handler().postDelayed({
            currenteUser()
        },2500)
    }
    private fun currenteUser(){
        if(firebaseAuth.currentUser != null){
            startActivity(Intent(this,CalcularIMC::class.java))
            finish()
        }else{
            startActivity(Intent(this,IniciarSesion::class.java))
            finish()
        }
    }
}