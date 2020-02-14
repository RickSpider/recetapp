package com.blackspider.recetapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mMedicamento
import com.blackspider.recetapp.viewHolder.viewholderMedicamentoDato


class adapterMedicamentoRecetaDato (val lmedicamentos : ArrayList<mMedicamento>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private  lateinit var  listener : View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder : RecyclerView.ViewHolder

        val inflater = LayoutInflater.from(parent.context)

        viewHolder = getViewHolder(parent, inflater)

        return viewHolder
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.layout_medicamento_dato, parent, false)
        viewHolder = viewholderMedicamentoDato(v1)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (lmedicamentos == null) 0 else lmedicamentos.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as viewholderMedicamentoDato



        holder.tvmedicamento.text = lmedicamentos[position].medicamento
      /*  holder.tvindicaciones.text = lmedicamentos[position].indicaciones
        holder.tvdosis.text = lmedicamentos[position].dosis*/
    }

    fun add(mmedicamento: mMedicamento){

        lmedicamentos.add(mmedicamento)
        notifyItemInserted(lmedicamentos.size-1)

    }


}