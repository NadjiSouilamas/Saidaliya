package com.example.myapplication.Entity

import androidx.room.PrimaryKey
import java.util.*

data class Commande(
    @PrimaryKey
    var id: Int,
    var telUser: String,
    var idPharmacie: Int,
    var ordonnance: String,
    var dateEmission: String, // java.util.Date
    var etat: String

)