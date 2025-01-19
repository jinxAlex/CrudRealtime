package com.example.crudrealtime180125.providers

import com.example.crudrealtime180125.models.Articulo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ArticuloProvider {
    private val database = FirebaseDatabase.getInstance().getReference("tienda")
    fun getDatos(datos: (MutableList<Articulo>) -> Unit) {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lista = mutableListOf<Articulo>()
                for (item in snapshot.children) {
                    val valor = item.getValue(Articulo::class.java)
                    if (valor != null) {
                        lista.add(valor)
                    }
                }
                lista.sortBy { it.nombre }
                datos(lista)

            }
            override fun onCancelled(error: DatabaseError) {
                println("Error al leer realtime: ${error.message}")
            }
        })
    }
}