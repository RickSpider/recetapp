package com.blackspider.recetapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterReceta
import com.blackspider.recetapp.model.mPaciente
import com.blackspider.recetapp.model.mReceta
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestPaciente
import com.blackspider.recetapp.request.requestReceta
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_paciente.*

/**
 * A simple [Fragment] subclass.
 */
class PacienteFragment : Fragment() {

    private lateinit var mCompositeDisposable : CompositeDisposable
    //private val pacienteid: Long = 1
    private val args : PacienteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_paciente, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       /* iv_paciente.setOnClickListener{

            val action = PacienteFragmentDirections.actionPacienteFragmentToPerfilFragment(args.pacienteid)
            findNavController().navigate(action)

        }*/

        if (!args.medico){

            fabAddReceta.isVisible = false

        }

        mCompositeDisposable = CompositeDisposable()
        loadJsonReceta()


        //loadJsonPaciente()
    }

  /*  private fun loadJsonPaciente() {

        val retrofit = connector().create(requestPaciente::class.java)
        mCompositeDisposable.add(
            retrofit.getPaciente(args.pacienteid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePaciente, this::handleError)
        )


    }*/

 /*  private fun handlePaciente(mpaciente : mPaciente){

        tvPaciente.text = "${mpaciente.mpersona.nombre} ${mpaciente.mpersona.apellido}"
        tvEdad.text = mpaciente.mpersona.celular
        tvCI.text = mpaciente.mpersona.ci


    }*/

    private fun loadJsonReceta() {
        val retrofit = connector().create(requestReceta::class.java)
        mCompositeDisposable.add(
            retrofit.getRecetaPaciente(args.pacienteid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseReceta, this::handleError)
        )
    }

    private fun handleResponseReceta (lreceta : ArrayList<mReceta>){

        var adapter = adapterReceta(lreceta)

        adapter.setOnClickListener(View.OnClickListener{

            val recetaid: Long = lreceta[rvRecetas.getChildAdapterPosition(it)].recetaid
            val action = PacienteFragmentDirections.actionPacienteFragmentToRecetaFragment(recetaid)
            findNavController().navigate(action)

        })

        rvRecetas.layoutManager = LinearLayoutManager(this.context)
        rvRecetas.adapter = adapter


    }

    private fun handleError(error: Throwable) {

        Toast.makeText(this.context, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
        println(error.localizedMessage)

    }


    override fun onDestroy() {
        super.onDestroy()

        mCompositeDisposable.clear()
        mCompositeDisposable.dispose()
    }
}

