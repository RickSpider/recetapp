package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.google.android.material.textfield.TextInputEditText

class viewholderMedicamentoDato(itemView : View): RecyclerView.ViewHolder(itemView){

    val tvmedicamento : TextView
   // val tvindicaciones : TextInputEditText
    //val tvdosis : TextInputEditText

    init {

        tvmedicamento = itemView.findViewById(R.id.tvMedicamento)
        //tvindicaciones = itemView.findViewById(R.id.tietDiagnostico)
        //tvdosis = itemView.findViewById(R.id.tietDosis)

    }

}