package com.example.myapplication

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation

import com.example.myapplication.Entity.Commande
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.LocalStorage.AppDatabase
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

class ExampleInstrumentedTest {

    lateinit var db: AppDatabase

    @Before
    fun initDB(){
        db = Room.inMemoryDatabaseBuilder(
                getInstrumentation().context, AppDatabase::class.java).build()

    }

    @Test
    fun insertPharmacie(){

        val p1 = Pharmacie(1, "p1", "ddd", "Blida","456465464", 1,1,0,"facdsmc,s","dskldks","dsdskd")

        db?.getPharmacieDao().addPharmacie(p1)

        val p = db.getPharmacieDao().getPharmacie(1).get(0)

        Assert.assertEquals(p1, p)
    }

    @Test
    fun insertCommande(){

        // Inserting p1 to satisfy foreignkey constraint
        val p1 = Pharmacie(1, "p1", "ddd", "Blida","456465464", 1,1,0,"facdsmc,s","dskldks","dsdskd")
        db?.getPharmacieDao().addPharmacie(p1)

        val c1 = Commande(1,"2","2",1,"p1", "Blida", "nsfns", "15 Aug 2019", "En attente")

        db.getCommandeDao().addCommande(c1)

        val c = db.getCommandeDao().getMyCommandes().get(0)

        Assert.assertEquals(c1, c)
    }

    @After
    fun close(){
        db.close()
    }
}
