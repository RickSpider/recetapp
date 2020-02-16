package com.blackspider.recetapp.fragments


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.*
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestCiudad
import com.blackspider.recetapp.request.requestMedico
import com.blackspider.recetapp.request.requestPaciente
import com.blackspider.recetapp.request.requestTitulo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_registro.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class RegistroFragment : Fragment(R.layout.fragment_registro) {

    private lateinit var mCompositeDisposable: CompositeDisposable
    private var cal = Calendar.getInstance()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        tvFchNacimiento.setOnClickListener{

            DatePickerDialog(this.context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)


                val myFormat = "dd MMMM yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.ROOT)
                tvFchNacimiento.text = sdf.format(cal.time)

            },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()

        }

        rbTipo.setOnCheckedChangeListener { group, checkedId ->

            when (checkedId){

                R.id.rbPaciente -> {

                    layoutPaciente.visibility = View.VISIBLE
                    layoutMedico.visibility = View.GONE

                }

                R.id.rbMedico -> {

                    layoutPaciente.visibility = View.GONE
                    layoutMedico.visibility = View.VISIBLE
                    loadJsonTitulo()

                }

            }


        }

        if (rbPaciente.isChecked || rbMedico.isChecked) {

            if (rbPaciente.isChecked){

                layoutPaciente.visibility

            }

        }



        btnRegitrar.setOnClickListener{

            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.ROOT)
            val fechaNacimiento : String = sdf.format(cal.time)

            var sexo =""

            if(radioFemenino.isChecked || radioMasculino.isChecked){



                if (radioFemenino.isChecked){


                    sexo = "f"

                }else{

                    if(radioMasculino.isChecked){

                        sexo = "m"

                    }

                }

                val mciudad = sCiudad.selectedItem as mCiudad

                var mpersona = mPersona(null,tvNombre.text.toString(), tvApellido.text.toString(),
                    tvCelular.text.toString(),tvRuc.text.toString(),tvCedula.text.toString(),tvCorreo.text.toString(),sexo
                    ,tietDireccion.text.toString(),fechaNacimiento,mciudad, tvPassword.text.toString())


            if(rbPaciente.isChecked || rbMedico.isChecked) {

                if(rbPaciente.isChecked){


                        var mpaciente = mPaciente(null,mpersona,tietGrupoS.text.toString(),tietPeso.text.toString())

                        postJsonPaciente(mpaciente)


                    }


                if(rbMedico.isChecked){

                    val mtitulo = sTitulo.selectedItem as mTitulo

                    var mmedico = mMedico(null, mpersona, mtitulo,tvNroregistro.text.toString(), false )

                    postJsonMedico(mmedico)

                }



                }



            }

        }

        mCompositeDisposable = CompositeDisposable()

        loadJsonCiudad()


    }

    private fun postJsonMedico (mmedico : mMedico){

        val retrofit = connector().create(requestMedico::class.java)
        mCompositeDisposable.add(
            retrofit.postMedico(mmedico)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePost,this::handleError)
        )


    }

    private fun postJsonPaciente(mpaciente : mPaciente){

        val retrofit = connector().create(requestPaciente::class.java)
        mCompositeDisposable.add(
            retrofit.postPaciente(mpaciente)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePost,this::handleError)
        )


    }

    private fun handlePost(){

        Toast.makeText(this.context, "Se Posteo", Toast.LENGTH_LONG).show()

    }

    private fun loadJsonCiudad(){

        val retrofit = connector().create(requestCiudad::class.java)
        mCompositeDisposable.add(
            retrofit.getCiudades()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleCiudades, this::handleError)
        )

    }

    private fun handleCiudades(lciudades : ArrayList<mCiudad>){

        val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_dropdown_item, lciudades)

        sCiudad.adapter = adapter
        sCiudad.setSelection(0)



    }

    private fun loadJsonTitulo(){

        val retrofit = connector().create(requestTitulo::class.java)
        mCompositeDisposable.add(
            retrofit.getTitulos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleTitulo, this::handleError)
        )

    }

    private fun handleTitulo(ltitulos : List<mTitulo>){

        val adapter = ArrayAdapter(this.context!!, android.R.layout.simple_spinner_dropdown_item, ltitulos)

        sTitulo.adapter = adapter
        sTitulo.setSelection(0)



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
