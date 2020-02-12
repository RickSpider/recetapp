package com.blackspider.recetapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterMedicamentoReceta
import com.blackspider.recetapp.model.mReceta
import com.blackspider.recetapp.model.mRecetaDetalle
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestReceta
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_receta.*
import kotlinx.android.synthetic.main.toolbar.*

/**
 * A simple [Fragment] subclass.
 */
class RecetaFragment : Fragment() {

    private lateinit var mCompositeDisposable : CompositeDisposable
    private val args : RecetaFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_receta, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        app_bar.setTitle("Receta")
        mCompositeDisposable = CompositeDisposable()
        loadJsonReceta()
        loadJsonRecetaDetalle()
    }


    private fun loadJsonReceta(){

        val retrofit = connector().create(requestReceta::class.java)
        mCompositeDisposable.add(
            retrofit.getReceta(args.recetaid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleReceta, this::handleError)
        )

    }

    private fun handleReceta(mreceta : mReceta){

        tvMedico.text = mreceta.mmedico!!.mtitulo.titulo+" "+mreceta.mmedico!!.mpersona.nombre+" "+mreceta.mmedico!!.mpersona.apellido
        tvPaciente.text = mreceta.mpaciente!!.mpersona!!.nombre+" "+mreceta.mpaciente!!.mpersona!!.apellido
        tvFecha.text = mreceta.fchreceta
        tvDiagnostico.text = mreceta.diagnostico



    }


    private fun loadJsonRecetaDetalle(){

        val retrofit = connector().create(requestReceta::class.java)
        mCompositeDisposable.add(
            retrofit.getRecetaDetalles(args.recetaid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleRecetaDetalle, this::handleError)
        )

    }

    private fun handleRecetaDetalle(lrecetadetalle : ArrayList<mRecetaDetalle>){

        var adapter = adapterMedicamentoReceta(lrecetadetalle)
        adapter.setOnClickListener(View.OnClickListener{

            val observacion : String = lrecetadetalle[rvRecetaDetalle.getChildAdapterPosition(it)].mpkrecetadetalle.mmedicamento!!.observacion

            Snackbar.make(this.view!!, observacion, Snackbar.LENGTH_SHORT).show()

        })
        rvRecetaDetalle.layoutManager = LinearLayoutManager(this.context)
        rvRecetaDetalle.adapter = adapter



    }

    private fun handleError(error: Throwable) {

        Toast.makeText(this.context, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
        println(error.localizedMessage)

    }

    override fun onDestroy() {
        super.onDestroy()

        mCompositeDisposable!!.clear()
        mCompositeDisposable!!.dispose()
    }

}
