package com.blackspider.recetapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mPaciente
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestPaciente
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_perfil.*


/**
 * A simple [Fragment] subclass.
 */
class PerfilFragment : Fragment() {

    private lateinit var mCompositeDisposable : CompositeDisposable
    private val pacienteid: Long = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ibReceta.setOnClickListener{


            val action = PerfilFragmentDirections.actionPerfilFragmentToPacienteFragment2()
            findNavController().navigate(action)

        }

        mCompositeDisposable = CompositeDisposable()

        loadJsonPaciente()

    }

    private fun loadJsonPaciente() {
        val retrofit = connector().create(requestPaciente::class.java)
        mCompositeDisposable.add(
            retrofit.getPaciente(pacienteid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePaciente, this::handleError)
        )
    }

    private fun handlePaciente(mpaciente : mPaciente){

        tvFullNombre.text = "${mpaciente.mpersona.nombre} ${mpaciente.mpersona.apellido}"
        tvCI.text = mpaciente.mpersona.ci
        tvEmail.text = mpaciente.mpersona.email
        tvTelefono.text = mpaciente.mpersona.telefono
        tvCelular.text = mpaciente.mpersona.celular
        tvGrupoS.text = mpaciente.gruposanguineo
        tvPeso.text = mpaciente.pesocorporal
        tvDireccion.text = mpaciente.mpersona.direccion

        if (mpaciente.mpersona.genero.equals("m")){

            tvSexo.text = "Masculino"

        }else{
            tvSexo.text = "Femenino"

        }



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
