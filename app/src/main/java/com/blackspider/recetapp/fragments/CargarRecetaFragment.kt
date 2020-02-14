package com.blackspider.recetapp.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager

import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterMedicamentoRecetaDato
import com.blackspider.recetapp.model.mMedicamento
import com.blackspider.recetapp.model.mPaciente
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestPaciente
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_cargar_receta.*

/**
 * A simple [Fragment] subclass.
 */
class CargarRecetaFragment : Fragment(R.layout.fragment_cargar_receta) {

    private val REQUEST_CODE = 1
    private  var adapter = adapterMedicamentoRecetaDato(ArrayList<mMedicamento>())
    private lateinit var mCompositeDisposable : CompositeDisposable
    private val args : CargarRecetaFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCompositeDisposable = CompositeDisposable()

        fabaddMedicamento.setOnClickListener{

            val dm = parentFragmentManager.beginTransaction()
            val mdf = MedicamentoDialogFragment(adapter.lmedicamentos)
            mdf.setTargetFragment(this,REQUEST_CODE)
            mdf.show(dm,"Medicamentos")



           /* val action = CargarRecetaFragmentDirections.actionCargarRecetaFragmentToMedicamentoDialogFragment()
            findNavController().navigate(action)*/

        }

        rvRecetaDetalle.layoutManager = LinearLayoutManager(this.context)
        rvRecetaDetalle.adapter = adapter

        loadJsonPaciente()

    }

    private fun loadJsonPaciente() {
        val retrofit = connector().create(requestPaciente::class.java)
        mCompositeDisposable.add(
            retrofit.getPaciente(args.pacientid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePaciente, this::handleError)
        )
    }

    fun handlePaciente (mpaciente : mPaciente){

        tvPaciente.text = "${mpaciente.mpersona!!.nombre} ${mpaciente.mpersona!!.apellido}"

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE){

            val array = data!!.getSerializableExtra("datos") as ArrayList<mMedicamento>

            if (adapter.lmedicamentos.size > 0) {

                for (i in 0 until adapter.lmedicamentos.size) {

                    if (array.size == 0){

                        break

                    }else{

                        for (j in 0 until array.size) {

                            if (adapter.lmedicamentos[i].medicamentoid == array[j].medicamentoid) {

                                array.removeAt(j)
                                break

                            }

                        }

                    }





                }

            }

            for (i in array) {

                adapter.add(i)

            }
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
