package com.example.crudrealtime180125.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealtime180125.R
import com.example.crudrealtime180125.models.Articulo

class ArticuloAdapter(
    var lista:MutableList<Articulo>,
    private val borrar: (Articulo) -> Unit,
    private val editar: (Articulo) -> Unit
): RecyclerView.Adapter<ArticuloViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticuloViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.articulo_layout,parent,false)
        return ArticuloViewHolder(v)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ArticuloViewHolder, position: Int) {
        holder.render(lista[position], borrar, editar )
    }
}