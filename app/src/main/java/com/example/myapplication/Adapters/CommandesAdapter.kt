package com.example.myapplication.Adapters

import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Entity.Commande
import com.example.myapplication.R
import kotlinx.android.synthetic.main.commandes_fragment.*

class CommandesAdapter(val commandesList: List<Commande>): RecyclerView.Adapter<CommandeViewHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommandeViewHoder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.commande_card, parent, false)
        return CommandeViewHoder(cellForRow) // To be checked
    }

    // Number of items
    override fun getItemCount(): Int {
        return commandesList.size
    }

    override fun onBindViewHolder(holder: CommandeViewHoder, position: Int) {
        val commande = commandesList[position]
        holder.bind(commande)
    }
}

class CommandeViewHoder(v: View): RecyclerView.ViewHolder(v){

    private var nom: TextView? = null
    private var place: TextView? = null
    private var date: TextView? = null
    private var etat: TextView? = null

    init{
        nom = itemView.findViewById(R.id.NameCommande)
        place = itemView.findViewById(R.id.PlaceCommande)
        date = itemView.findViewById(R.id.DateCommande)
        etat = itemView.findViewById(R.id.StateCommande)
    }

    fun bind(commande: Commande){
        nom?.text = commande.nom
        place?.text = commande.nomPharmacie+", "+commande.villePharmacie
        date?.text = commande.dateEmission
        etat?.text = commande.etat

        when(commande.etat){
            "Rejetée" ->{
                etat?.setTextColor(Color.parseColor("#BD473A"))
            }

            "En attente" ->{
                etat?.setTextColor(Color.parseColor("#E67E2E"))
            }

            "Prête" ->{
                etat?.setTextColor(Color.parseColor("#2E91AA"))
            }
        }
    }
}