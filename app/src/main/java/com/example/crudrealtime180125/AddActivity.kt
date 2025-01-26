package com.example.crudrealtime180125

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.crudrealtime180125.databinding.ActivityAddBinding
import com.example.crudrealtime180125.models.Articulo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddActivity : AppCompatActivity() {

    private var nombre = ""

    private var descripcion = ""

    private var precio = 0F

    private lateinit var binding: ActivityAddBinding

    private lateinit var articulo: Articulo

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
        recogerDatos()
        ponerDatos()
    }

    private fun ponerDatos() {
        if (editar) {
            binding.tvTitulo.text = "EDITAR REGISTRO"
            binding.btnAgregar.text = "EDITAR"
            binding.etNombre.setText(articulo.nombre)
            binding.etDescripcion.setText(articulo.descripcion)
            binding.etPrecio.setText(articulo.precio.toString())
            binding.etNombre.isEnabled = false
        }
    }

    private fun recogerDatos() {
        val datos = intent.extras
        if (datos != null) {
            articulo = datos.getSerializable("ARTICULO") as Articulo
            editar = true
        }
    }

    private fun setListeners() {
        binding.btnAgregar.setOnClickListener {
            agregarArticulo()
        }
        binding.btnCancelar.setOnClickListener {
            finish()
        }

    }

    private fun agregarArticulo() {
        if (datosCorrectos()) {
            val tiendaRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("tienda")
            val articuloNuevo = Articulo(nombre, descripcion, precio)

            tiendaRef.get().addOnSuccessListener {
                var existe = false
                var nodoAEditar = ""

                for (nodo in it.children) {
                    val nombreExistente = nodo.child("nombre").getValue(String::class.java)
                    if (nombreExistente == articuloNuevo.nombre) {
                        existe = true
                        nodoAEditar = nodo.key.toString()
                        Log.d("NODO", nodoAEditar)
                    }
                }

                when {
                    (!existe && !editar) -> {
                        tiendaRef.push().setValue(articuloNuevo)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Se ha agregado el artículo", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "No se ha podido agregar el artículo", Toast.LENGTH_SHORT).show()
                            }
                    }

                    (existe && !editar) -> {
                        Toast.makeText(this, "El nombre del artículo ya existe", Toast.LENGTH_SHORT).show()
                    }

                    (existe && editar) -> {
                        tiendaRef.child(nodoAEditar).setValue(articuloNuevo).addOnSuccessListener {
                            finish()
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun datosCorrectos(): Boolean {
        var esCorrecto = true
        nombre = binding.etNombre.text.toString().trim()
        descripcion = binding.etDescripcion.text.toString().trim()
        precio = binding.etPrecio.text.toString().toFloat()

        if (nombre.length < 3) {
            esCorrecto = false
            binding.etNombre.error =
                "El nombre introducido es invalido, tiene menos de 3 carácteres"
        }
        if (descripcion.length < 15) {
            esCorrecto = false
            binding.etNombre.error =
                "La descripción introducida es invalida, tiene menos de 15 carácteres"
        }
        if (precio < 0) {
            esCorrecto = false
            binding.etNombre.error =
                "El precio introducido es invalida, tiene menos de 15 carácteres"
        }

        return esCorrecto
    }
}