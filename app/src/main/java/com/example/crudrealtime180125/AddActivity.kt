package com.example.crudrealtime180125

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudrealtime180125.databinding.ActivityAddBinding
import com.example.crudrealtime180125.models.Articulo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddActivity : AppCompatActivity() {

    private var nombre = ""

    private var descripcion = ""

    private var precio = 0F

    private lateinit var binding: ActivityAddBinding

    private var editar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setListeners()
    }

    private fun setListeners() {
        binding.btnAgregar.setOnClickListener {
            agregarArticulo()
        }
    }

    private fun agregarArticulo() {
        if (datosCorrectos()){
            val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("tienda")
            val item = Articulo(nombre,descripcion,precio)
            val nodo = nombre.replace(" ","_")
            database.child(nodo).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()&& !editar){
                        Toast.makeText(this@AddActivity,"El nombre ya esta registrado",Toast.LENGTH_SHORT).show()
                    }else{
                        database.child(nodo).setValue(item).addOnSuccessListener {
                            finish()
                        }
                            .addOnFailureListener{
                                Toast.makeText(this@AddActivity,"Error al guardar el artículo",Toast.LENGTH_SHORT).show()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }

    private fun datosCorrectos(): Boolean {
        var esCorrecto = true
        nombre = binding.etNombre.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()
        precio = binding.etPrecio.text.toString().toFloat()

        if(nombre.length <3){
            esCorrecto = false
            binding.etNombre.error = "El nombre introducido es invalido, tiene menos de 3 carácteres"
        }
        if(descripcion.length <15){
            esCorrecto = false
            binding.etNombre.error = "La descripción introducida es invalida, tiene menos de 15 carácteres"
        }
        if(precio <0){
            esCorrecto = false
            binding.etNombre.error = "El precio introducido es invalida, tiene menos de 15 carácteres"
        }



        return esCorrecto
    }
}