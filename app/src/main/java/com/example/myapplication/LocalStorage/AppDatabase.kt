package com.example.myapplication.LocalStorage

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie

@Database(entities = arrayOf(Pharmacie::class,Commande::class),version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getPharmacieDao(): PharmacieDao
    abstract fun getCommandeDao(): CommandeDao
}