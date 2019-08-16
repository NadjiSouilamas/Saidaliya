package com.example.myapplication.LocalStorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.Entity.Pharmacie

@Dao
interface PharmacieDao {

    @Insert
    fun addPharmacie(pharmacie: Pharmacie)

    @Query("select * from Pharmacie")
    fun getAllPharmacies(): List<Pharmacie>

    @Query("select * from Pharmacie where ville = :ville")
    fun getPharmaciesParVille(ville: String): List<Pharmacie>

    // For tests
    @Query("select * from Pharmacie where id = :id")
    fun getPharmacie(id: Int): List<Pharmacie>

    @Query("select id from pharmacie where nom = :nom and ville = :ville")
    fun getIDPharmacie(nom: String, ville: String): List<Int>

    //TODO getPharmacies les plus proches de ma location

}