package com.blackspider.recetapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mMedicamento
import com.blackspider.recetapp.viewHolder.viewholderMedicamento


class adapterMedicamento (var lmedicamentos : ArrayList<mMedicamento>, val context : Context): RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    Filterable {

    private lateinit var listener: View.OnClickListener
    private var lmedicamentos1  = lmedicamentos

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder

        val inflater = LayoutInflater.from(parent.context)

        viewHolder = getViewHolder(parent, inflater)


        return viewHolder
    }

    private fun getViewHolder(
        parent: ViewGroup,
        inflater: LayoutInflater
    ): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.layout_medicamento, parent, false)
        viewHolder = viewholderMedicamento(v1)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (lmedicamentos == null) 0 else lmedicamentos.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as viewholderMedicamento

        holder.tvmedicamento.text = lmedicamentos[position].medicamento
        holder.tvobservacion.text = lmedicamentos[position].observacion

        holder.mchbmedicamento.isChecked = lmedicamentos[position].isSelected

        holder.mchbmedicamento.setOnCheckedChangeListener { buttonView, isChecked ->

            lmedicamentos[position].isSelected = isChecked

            //Toast.makeText(context, "$isChecked", Toast.LENGTH_LONG).show()


        }



    }

    /* override fun onClick(v: View?) {
        if (listener != null) listener!!.onClick(v)
    }*/

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charString : String = constraint.toString()

                if (charString.isEmpty()){

                    lmedicamentos = lmedicamentos1

                }else{

                    var filterList : ArrayList<mMedicamento> = ArrayList()

                    for(mm in lmedicamentos1){

                        val nc = mm.medicamento

                        if (nc.toLowerCase().contains(charString)){

                            filterList.add(mm)

                        }

                    }

                    lmedicamentos = filterList

                }

                val filterResult = FilterResults()
                filterResult.values = lmedicamentos

                return filterResult

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                lmedicamentos = results!!.values as ArrayList<mMedicamento>
                notifyDataSetChanged()
            }

        }
    }





}
