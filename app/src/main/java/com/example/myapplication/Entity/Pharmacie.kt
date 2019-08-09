package com.example.myapplication.Entity

import androidx.room.PrimaryKey

data class Pharmacie(
    @PrimaryKey
    var id: Int,
    var nom: String,
    var adresse:String,
    var ville: String,
    var tel: String,
    var cnas: Int, /** Too much for a Boolean */
    var casnos: Int,
    var cnr: Int,
    var facebook: String,
    var map: String,
    var photo: String
)