package com.blackspider.recetapp.viewHolder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R

class viewholderPaciente (itemView : View): RecyclerView.ViewHolder(itemView){

    val tvfullname : TextView
    val tvcelular : TextView
    val tvemail : TextView
    val tvcedula: TextView
    init {

        tvfullname = itemView.findViewById(R.id.tvMedicoFullName)
        tvcelular = itemView.findViewById(R.id.tvCelular)
        tvemail = itemView.findViewById(R.id.tvEmail)
        tvcedula=itemView.findViewById(R.id.tvCedula)
    }

}