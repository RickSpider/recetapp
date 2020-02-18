package com.blackspider.recetapp.fragments


import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment

import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mRecetaDetalle
import kotlinx.android.synthetic.main.fragment_medicamento_dato_dialog.*

/**
 * A simple [Fragment] subclass.
 */
class MedicamentoDatoDialogFragment(var mrecetadetalle : mRecetaDetalle) : DialogFragment() {

    private val REQUEST_CODE_DATO = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =inflater.inflate(R.layout.fragment_medicamento_dato_dialog, container, false)

        if (dialog != null && dialog!!.window != null){

            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog!!.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        }

        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvMedicamento.text = mrecetadetalle.mpkrecetadetalle.mmedicamento!!.medicamento

        if (!mrecetadetalle.indicaciones.equals("")){

            tietIndicaciones.setText(mrecetadetalle.indicaciones)
            tietDosis.setText(mrecetadetalle.dosis)

        }

        btnAceptar.setOnClickListener {

            sendResult()

        }

    }


    private fun sendResult(){


        val intent = Intent()
        intent.putExtra("id", mrecetadetalle.mpkrecetadetalle.mmedicamento!!.medicamentoid)
        intent.putExtra("indicaciones", tietIndicaciones.text.toString())
        intent.putExtra("dosis", tietDosis.text.toString())
        targetFragment!!.onActivityResult(targetRequestCode, REQUEST_CODE_DATO, intent)
        dismiss()



    }


}
