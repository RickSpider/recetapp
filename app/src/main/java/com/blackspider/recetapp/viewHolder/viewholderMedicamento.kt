package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R

class viewholderMedicamento(itemView : View): RecyclerView.ViewHolder(itemView){

    val tvmedicamento : TextView
    val tvobservacion : TextView

    init {

        tvmedicamento = itemView.findViewById(R.id.tvMedicamento)
        tvobservacion = itemView.findViewById(R.id.tvObservacion)

    }

}