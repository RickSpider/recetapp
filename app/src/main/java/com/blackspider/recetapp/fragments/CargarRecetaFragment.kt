package com.blackspider.recetapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.blackspider.recetapp.R
import kotlinx.android.synthetic.main.fragment_cargar_receta.*

/**
 * A simple [Fragment] subclass.
 */
class CargarRecetaFragment : Fragment(R.layout.fragment_cargar_receta) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabaddMedicamento.setOnClickListener{

            val dm = parentFragmentManager.beginTransaction()
            val mdf = MedicamentoDialogFragment()

            mdf.show(dm,"Medicamentos")

        }
    }

}
