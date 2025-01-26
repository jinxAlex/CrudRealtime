package com.example.crudrealtime180125.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.crudrealtime180125.R
import com.example.crudrealtime180125.databinding.ArticuloLayoutBinding
import com.example.crudrealtime180125.models.Articulo

class ArticuloViewHolder(v:View):RecyclerView.ViewHolder(v) {
    private val binding = ArticuloLayoutBinding.bind(v)
    fun render(articulo: Articulo, borrar: (Articulo) -> Unit, editar: (Articulo) -> Unit) {
        binding.tvNombre.text = articulo.nombre
        binding.tvDescripcion.text = String.format(binding.tvDescripcion.context.getString(R.string.descripcion_articulo),articulo.descripcion)
        binding.tvPrecio.text = String.format(binding.tvDescripcion.context.getString(R.string.precio_articulo),articulo.precio.toString())
        binding.btnBorrar.setOnClickListener {
            borrar(articulo)
        }
        binding.btnEditar.setOnClickListener {
            editar(articulo)
        }
    }
}