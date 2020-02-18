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

import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mMedico
import com.blackspider.recetapp.model.mPaciente
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestMedico
import com.blackspider.recetapp.request.requestPaciente
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_perfil_edit.*
import kotlin.properties.Delegates






/**
 * A simple [Fragment] subclass.
 */
class PerfilFragmentEdit : Fragment() {

    private lateinit var mCompositeDisposable : CompositeDisposable
    // private var pacienteid : Long = 1
    private val args : PerfilFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_perfil_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ibReceta.setOnClickListener{

            val action = PerfilFragmentDirections.actionPerfilFragmentToPacienteFragment2(args.id, true, args.medicoid)
            findNavController().navigate(action)

        }

        mCompositeDisposable = CompositeDisposable()

        if (args.medico){

            loadJsonMedico()

        }else{

            loadJsonPaciente()

        }



    }

    fun loadJsonMedico () {

        val retrofit = connector().create(requestMedico::class.java)
        mCompositeDisposable.add(
            retrofit.getOneMedico(args.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleMedico, this::handleError)
        )

    }

    private fun handleMedico(mmedico : mMedico){

        tvFullNombre.text = "${mmedico.mtitulo.titulo} ${mmedico.mpersona.nombre} ${mmedico.mpersona.apellido}"
        tvCI.text = mmedico.mpersona.ci


        etvDireccion.setText(mmedico.mpersona.direccion)
        etvCelular.setText(mmedico.mpersona.celular)
        etvEmail.setText(mmedico.mpersona.email)


        tvTitulo.isVisible = true
        tvCertificado.isVisible = true
        tvNroregistro.isVisible = true

        if (mmedico.certificado!!){

            tvCertificado.text = "Certificado"

        }else{

            tvCertificado.text = "No Certificado"

        }

        tvNroregistro.text = mmedico.nroregistro
        tvTitulo.text = mmedico.mtitulo.titulo


        if (mmedico.mpersona.genero.equals("m")){

            tvSexo.text = "Masculino"

        }else{
            tvSexo.text = "Femenino"

        }


        tvPeso.isVisible = false
        tvGrupoS.isVisible = false
        ibReceta.isVisible = false
        ibWhtasapp.isVisible = false



    }

    private fun loadJsonPaciente() {
        val retrofit = connector().create(requestPaciente::class.java)
        mCompositeDisposable.add(
            retrofit.getPaciente(args.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePaciente, this::handleError)
        )
    }

    private fun handlePaciente(mpaciente : mPaciente){

        tvFullNombre.text = "${mpaciente.mpersona!!.nombre} ${mpaciente.mpersona!!.apellido}"
        tvCI.text = mpaciente.mpersona!!.ci
        //tvEmail.text = mpaciente.mpersona!!.email
        //tvTelefono.text = mpaciente.mpersona!!.telefono
        //tvCelular.text = mpaciente.mpersona!!.celular
        tvGrupoS.text = mpaciente.gruposanguineo
        tvPeso.text = mpaciente.pesocorporal
       // tvDireccion.text = mpaciente.mpersona!!.direccion


        etvDireccion.setText(mpaciente.mpersona!!.direccion)
        etvCelular.setText(mpaciente.mpersona!!.celular)
        etvEmail.setText(mpaciente.mpersona!!.email)

        //val editText3 = findViewById(R.id.inNumber)
        //val editText3 = EditText(this)
       // editText3.apply {
        //    setText("Androidly EditText")
       // }


        if (mpaciente.mpersona!!.genero.equals("m")){

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
