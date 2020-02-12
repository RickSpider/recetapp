package com.blackspider.recetapp.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mRecetaDetalle
import com.blackspider.recetapp.viewHolder.viewholderMedicamentoReceta

class adapterMedicamentoReceta (val lmedicamentos : ArrayList<mRecetaDetalle>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener {

    private  lateinit var  listener : View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder : RecyclerView.ViewHolder

        val inflater = LayoutInflater.from(parent.context)

        viewHolder = getViewHolder(parent, inflater)
        viewHolder.itemView.setOnClickListener(this)

        return viewHolder
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.layout_medicamento_receta, parent, false)
        viewHolder = viewholderMedicamentoReceta(v1)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (lmedicamentos == null) 0 else lmedicamentos.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as viewholderMedicamentoReceta



        holder.tvmedicamento.text = lmedicamentos[position].mpkrecetadetalle.mmedicamento!!.medicamento
        holder.tvindicaciones.text = lmedicamentos[position].indicaciones
        holder.tvdosis.text = lmedicamentos[position].dosis
    }

    fun setOnClickListener(listener : View.OnClickListener){

        this.listener = listener

    }

    override fun onClick(v: View?) {
        if (listener != null)   listener!!.onClick(v)
    }


}