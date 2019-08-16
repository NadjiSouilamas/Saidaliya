package com.example.myapplication.LocalStorage

import androidx.room.*
import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie

@Dao
interface CommandeDao {

    @Insert
    fun addCommande(commande: Commande)

    @Update
    fun updateCommande(commande: Commande)

    @Delete
    fun deleteCommande(commande: Commande)

    @Query("select * from commande")
    fun getMyCommandes(): List<Commande>

    @Query("select * from commande where idPharmacie = :idPharmacie")
    fun getCommandesParPharmacie(idPharmacie: Int) : List<Commande>
}