package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R

class viewholderPaciente (itemView : View): RecyclerView.ViewHolder(itemView){

    val tvfullname : TextView
    val tvci: TextView
    val tvcelular : TextView

    init {

        tvfullname = itemView.findViewById(R.id.tvMedicoFullName)
        tvci = itemView.findViewById(R.id.tvCI)
        tvcelular = itemView.findViewById(R.id.tvCelular)

    }

}