package com.blackspider.recetapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mMedicoPaciente
import com.blackspider.recetapp.model.mReceta
import com.blackspider.recetapp.viewHolder.viewholderReceta

class adapterReceta (var lrecetas : ArrayList<mReceta>):RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener, Filterable {

    private var auxarray = lrecetas
    private var listener: View.OnClickListener? =  null

    fun setOnClickListener(listener : View.OnClickListener){

        this.listener = listener

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder : RecyclerView.ViewHolder

        val inflater = LayoutInflater.from(parent.context)

        viewHolder = getViewHolder(parent, inflater)
        viewHolder.itemView.setOnClickListener(this)

       return viewHolder
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.layout_receta, parent, false)
        viewHolder = viewholderReceta(v1)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (lrecetas == null) 0 else lrecetas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as viewholderReceta

        val mreceta = lrecetas.get(position)

        holder.tvmedico.text = "${mreceta.mmedico!!.mtitulo!!.titulo} ${mreceta.mmedico!!.mpersona!!.nombre} ${mreceta.mmedico!!.mpersona!!.apellido}"
        holder.tvdiagnostico.text = mreceta.diagnostico
        holder.tvestado.text = mreceta.estado
        holder.tvfecha.text = mreceta.fchreceta

    }

    override fun onClick(p0: View?) {
        if (listener != null)   listener!!.onClick(p0)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charString : String = constraint.toString()

                if (charString.isEmpty()){

                    lrecetas = auxarray

                }else{

                    var filterList : ArrayList<mReceta> = ArrayList()

                    for(mr in auxarray){

                        val nc = mr.mmedico!!.mpersona!!.nombre!!+" "+mr.mmedico!!.mpersona!!.apellido!!

                        if (nc.toLowerCase().contains(charString)){

                            filterList.add(mr)

                        }

                    }

                    lrecetas = filterList

                }

                val filterResult = FilterResults()
                filterResult.values = lrecetas

                return filterResult

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                lrecetas = results!!.values as ArrayList<mReceta>
                notifyDataSetChanged()
            }

        }
    }


}