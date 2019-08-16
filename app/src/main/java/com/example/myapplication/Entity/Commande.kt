package com.example.myapplication.Entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Pharmacie::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("idPharmacie"),
    onDelete = ForeignKey.CASCADE)
))
data class Commande(
    @PrimaryKey
    var id: Int,
    var nom: String,
    var telUser: String,
    var idPharmacie: Int,
    var nomPharmacie: String,
    var villePharmacie: String,
    var ordonnance: String,
    var dateEmission: String,
    var etat: String

)