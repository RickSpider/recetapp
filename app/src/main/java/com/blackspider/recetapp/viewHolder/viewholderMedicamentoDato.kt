package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.google.android.material.textfield.TextInputEditText

class viewholderMedicamentoDato(itemView : View): RecyclerView.ViewHolder(itemView){

    val tvmedicamento : TextView
    val tietindicaciones : TextInputEditText
    val tietdosis : TextInputEditText

    init {

        tvmedicamento = itemView.findViewById(R.id.tvMedicamento)
        tietindicaciones = itemView.findViewById(R.id.tietIndicaciones)
        tietdosis = itemView.findViewById(R.id.tietDosis)

    }

}