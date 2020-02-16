package com.blackspider.recetapp.adapter

import android.text.Editable
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mRecetaDetalle
import com.blackspider.recetapp.viewHolder.viewholderMedicamentoDato


class adapterMedicamentoRecetaDato (val lrecetadetalle : ArrayList<mRecetaDetalle>): RecyclerView.Adapter<RecyclerView.ViewHolder>(){


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
        return if (lrecetadetalle == null) 0 else lrecetadetalle.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as viewholderMedicamentoDato


            holder.tvmedicamento.text = lrecetadetalle[position].mpkrecetadetalle.mmedicamento!!.medicamento
          /*  holder.tietindicaciones.setText(lrecetadetalle[position].indicaciones,TextView.BufferType.EDITABLE) //Editable.Factory.getInstance().newEditable(lrecetadetalle[position].indicaciones)
            holder.tietdosis.setText(lrecetadetalle[position].dosis,TextView.BufferType.EDITABLE)

            holder.tietindicaciones.doOnTextChanged { text, start, count, after ->

                lrecetadetalle[position].indicaciones = holder.tietindicaciones.text.toString()

            }

            holder.tietdosis.doOnTextChanged { text, start, count, after ->

                lrecetadetalle[position].dosis = holder.tietdosis.text.toString()

            }*/

    }

    fun add(mrecetadetalle: mRecetaDetalle){

        lrecetadetalle.add(mrecetadetalle)
        notifyItemInserted(lrecetadetalle.size-1)
       // notifyDataSetChanged()

    }


}