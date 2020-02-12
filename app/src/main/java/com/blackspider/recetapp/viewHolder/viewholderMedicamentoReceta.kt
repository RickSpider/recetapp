package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R

class viewholderMedicamentoReceta(itemView : View): RecyclerView.ViewHolder(itemView){

    val tvmedicamento : TextView
    val tvindicaciones : TextView
    val tvdosis : TextView

    init {

        tvmedicamento = itemView.findViewById(R.id.tvMedicamento)
        tvindicaciones = itemView.findViewById(R.id.tvIndicaciones)
        tvdosis = itemView.findViewById(R.id.tvDosis)

    }

}