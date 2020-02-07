package com.blackspider.recetapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.MainActivity
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mMedicoPaciente
import com.blackspider.recetapp.model.mPaciente
import com.blackspider.recetapp.viewHolder.viewholderPaciente
import java.util.*
import kotlin.collections.ArrayList

class adapterPaciente(var lpacientes : ArrayList<mMedicoPaciente>): RecyclerView.Adapter<RecyclerView.ViewHolder>(), View.OnClickListener, Filterable{

    private var lpacientes1 = lpacientes
    private lateinit var listener: View.OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder : RecyclerView.ViewHolder

        val inflater = LayoutInflater.from(parent.context)

        viewHolder = getViewHolder(parent, inflater)
        viewHolder.itemView.setOnClickListener(this)

        return viewHolder
    }

    private fun getViewHolder(parent: ViewGroup, inflater: LayoutInflater): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val v1 = inflater.inflate(R.layout.layout_paciente, parent, false)
        viewHolder = viewholderPaciente(v1)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return if (lpacientes == null) 0 else lpacientes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as viewholderPaciente

        val mpaciente = lpacientes.get(position).mpkmedicopaciente.mpaciente

        holder.tvfullname.text = "${mpaciente!!.mpersona!!.nombre} ${mpaciente.mpersona!!.apellido}"
        holder.tvcelular.text = mpaciente!!.mpersona!!.celular
        holder.tvemail.text = mpaciente!!.mpersona!!.email
    }

    fun setOnClickListener(listener : View.OnClickListener){

        this.listener = listener

    }

    override fun onClick(v: View?) {
        if (listener != null)   listener!!.onClick(v)
    }



    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charString : String = constraint.toString()

                if (charString.isEmpty()){

                    lpacientes = lpacientes1

                }else{

                    var filterList : ArrayList<mMedicoPaciente> = ArrayList()

                    for(mmp in lpacientes1){

                        val nc = mmp.mpkmedicopaciente.mpaciente!!.mpersona!!.nombre!!+" "+mmp.mpkmedicopaciente.mpaciente!!.mpersona!!.apellido!!

                        if (nc.toLowerCase().contains(charString)){

                            filterList.add(mmp)

                        }

                    }

                    lpacientes = filterList

                }

                val filterResult = FilterResults()
                filterResult.values = lpacientes

                return filterResult

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                lpacientes = results!!.values as ArrayList<mMedicoPaciente>
                notifyDataSetChanged()
            }

        }
    }


}