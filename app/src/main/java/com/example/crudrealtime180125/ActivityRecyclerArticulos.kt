package com.example.crudrealtime180125

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudrealtime180125.adapters.ArticuloAdapter
import com.example.crudrealtime180125.databinding.ActivityRecyclerArticulosBinding
import com.example.crudrealtime180125.models.Articulo
import com.example.crudrealtime180125.providers.ArticuloProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class ActivityRecyclerArticulos : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerArticulosBinding

    var adapter = ArticuloAdapter(mutableListOf(), { item->borrar(item)}, { item->editar(item)})


    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRecyclerArticulosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth= Firebase.auth
        database = FirebaseDatabase.getInstance().getReference("tienda")
        setListeners()
        setRecycler()
    }

    private fun setRecycler() {
        val layoutManager = LinearLayoutManager(this)
        binding.recycler.layoutManager=layoutManager

        binding.recycler.adapter=adapter
        traerDatos()
    }

    private fun traerDatos() {
        val agendaProvider = ArticuloProvider()
        agendaProvider.getDatos { todosLosRegistros->
            adapter.lista = todosLosRegistros
            adapter.notifyDataSetChanged()
        }
    }

    private fun setListeners() {
        binding.floatingActionButton.setOnClickListener {
            val i = Intent(this,AddActivity::class.java)
            startActivity(i)
        }
        binding.navegacion.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.item_salir -> {
                    finishAffinity()
                    true
                }

                R.id.item_borrar -> {
                    database.removeValue().addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this,"Articulos eliminados",Toast.LENGTH_SHORT).show()
                            traerDatos()
                        }else{
                            Toast.makeText(this,"Hubo un error en el sistema", Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                R.id.item_cerrarSesion -> {
                    auth.signOut()
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    private fun editar(articulo: Articulo) {
        val bundle = Bundle().apply {
            putSerializable("ARTICULO",articulo)
        }
        val i = Intent(this, AddActivity::class.java)
        i.putExtras(bundle)
        startActivity(i)
    }

    private fun borrar(articulo: Articulo) {
        database.get().addOnSuccessListener {
            for(nodo in it.children){
                val nombre=nodo.child("nombre").getValue(String::class.java)
                if(nombre==articulo.nombre){
                    nodo.ref.removeValue()
                        .addOnSuccessListener {
                            val position = adapter.lista.indexOf(articulo)
                            if(position!= -1){
                                adapter.lista.removeAt(position)
                                adapter.notifyItemRemoved(position)
                                Toast.makeText(this,"Se ha eliminado",Toast.LENGTH_SHORT).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(this,"No se ha conseguido eliminar",Toast.LENGTH_SHORT).show()
                        }
                    break
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        traerDatos()
    }
}