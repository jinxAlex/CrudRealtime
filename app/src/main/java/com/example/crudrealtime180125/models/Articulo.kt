package com.example.crudrealtime180125.models

import java.io.Serializable

data class Articulo(
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Float = 0F
): Serializable
