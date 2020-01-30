package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R


class viewholderReceta (itemView : View): RecyclerView.ViewHolder(itemView){

    val tvmedico : TextView
    val tvestado : TextView
    val tvfecha : TextView
    val tvdiagnostico : TextView

    init {

        tvmedico = itemView.findViewById(R.id.tvMedico)
        tvestado = itemView.findViewById(R.id.tvEstado)
        tvfecha = itemView.findViewById(R.id.tvFecha)
        tvdiagnostico = itemView.findViewById(R.id.tvDiagnostico)

    }

}