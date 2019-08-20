package com.example.myapplication.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.Entity.Pharmacie
import com.example.myapplication.R
import com.example.myapplication.Server.RetrofitService


class PharmaciesAdapter(var pharmaList: List<Pharmacie>): RecyclerView.Adapter<PharmacieViewHoder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PharmacieViewHoder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.pharmacie_card, parent, false)
        return PharmacieViewHoder(cellForRow) // To be checked
    }

    // Number of items
    override fun getItemCount(): Int {
        return pharmaList.size
    }

    override fun onBindViewHolder(holder: PharmacieViewHoder, position: Int) {
        val pharmacie = pharmaList[position]
        holder.bind(pharmacie)
    }
}

interface OnItemClickListener {
    fun onItemClicked(position: Int, view: View)
}

fun RecyclerView.addOnItemClickListener(onClickListener: OnItemClickListener) {
    this.addOnChildAttachStateChangeListener(object: RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }

        override fun onChildViewAttachedToWindow(view: View) {
            view?.setOnClickListener({
                val holder = getChildViewHolder(view)
                onClickListener.onItemClicked(holder.adapterPosition, view)
            })
        }
        })
}

class PharmacieViewHoder(v: View): RecyclerView.ViewHolder(v){

    private var image: ImageView? = null
    private var nom: TextView? = null
    private var adresse: TextView? = null

    init{
        image = itemView.findViewById(R.id.ImagePharmacieCard)
        nom = itemView.findViewById(R.id.NamePharmacie)
        adresse = itemView.findViewById(R.id.AddressPharmacie)
    }

    fun bind(pharmacie: Pharmacie){

        Glide
            .with(image!!.context)
            .load(RetrofitService.imagesPharmacieAdresse+pharmacie.photo)
            .centerCrop()
            .into(image!!)

        // image with glide
        nom?.text = pharmacie.nom
        adresse?.text = pharmacie.adresse
    }


}