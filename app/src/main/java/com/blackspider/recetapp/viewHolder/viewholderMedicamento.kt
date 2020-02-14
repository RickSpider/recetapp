package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.google.android.material.checkbox.MaterialCheckBox

class viewholderMedicamento(itemView : View): RecyclerView.ViewHolder(itemView){

    val tvmedicamento : TextView
    val tvobservacion : TextView
    val mchbmedicamento : MaterialCheckBox

    init {

        tvmedicamento = itemView.findViewById(R.id.tvMedicamento)
        tvobservacion = itemView.findViewById(R.id.tvObservacion)
        mchbmedicamento = itemView.findViewById(R.id.mchbMedicamento)

    }

}