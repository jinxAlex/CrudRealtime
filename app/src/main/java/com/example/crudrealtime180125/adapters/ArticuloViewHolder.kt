package com.example.crudrealtime180125.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealtime180125.databinding.ArticuloLayoutBinding
import com.example.crudrealtime180125.models.Articulo

class ArticuloViewHolder(v:View):RecyclerView.ViewHolder(v) {
    private val binding = ArticuloLayoutBinding.bind(v)
    fun render(articulo: Articulo, borrar: (Articulo) -> Unit, editar: (Articulo) -> Unit) {
        binding.tvNombre.text = articulo.nombre
        binding.tvDescripcion.text = articulo.descripcion
        binding.tvPrecio.text = String.format(articulo.precio.toString())
        binding.btnBorrar.setOnClickListener {
            borrar(articulo)
        }
        binding.btnEditar.setOnClickListener {
            editar(articulo)
        }
    }
}