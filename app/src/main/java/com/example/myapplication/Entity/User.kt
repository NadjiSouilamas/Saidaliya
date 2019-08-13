package com.example.myapplication.Entity

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey
    var tel : String,
    var nss : Double,
    var nom : String,
    var prenom : String,
    var toReinit : Int,
    var adresse : String
)