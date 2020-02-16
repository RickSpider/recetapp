package com.blackspider.recetapp.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterMedicamento
import com.blackspider.recetapp.model.mMedicamento
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestMedicamento
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_medicamento_dialog.*


/**
 * A simple [Fragment] subclass.
 */
class MedicamentoDialogFragment(val medicamentos: ArrayList<mMedicamento>) : DialogFragment(){

    private lateinit var mCompositeDisposable : CompositeDisposable
    private lateinit var adapter : adapterMedicamento
    private val RQUEST_CODE = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medicamento_dialog, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        setStyle(
            DialogFragment.STYLE_NORMAL, WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog!!.window!!.attributes.windowAnimations = R.style.DialogAnimation
        //dialog!!.window!!.setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        btnAgregar.setOnClickListener{

            sendResult()

        }


        searchMedicamento.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (adapter != null ){

                    adapter.filter.filter(newText)

                }
                return true
            }
        } )

        mCompositeDisposable = CompositeDisposable()
        loadJsonMedicamentos()
    }


    private fun loadJsonMedicamentos(){

        val retrofit = connector().create(requestMedicamento::class.java)
        mCompositeDisposable.add(
            retrofit.getmedicamento()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseMedicamento, this::handleError)
        )

    }

    private fun handleResponseMedicamento (lmedicamentos : ArrayList<mMedicamento>){

        var cont = 0
        if (medicamentos.size > 0){

            for (i in lmedicamentos){


                for (j in medicamentos){

                    if (j.medicamentoid == i.medicamentoid){

                        i.isSelected = true
                        cont++
                        break

                    }

                }


                if (cont == medicamentos.size){

                    break
                }

            }

        }

        adapter = adapterMedicamento(lmedicamentos, this.context!!)

        rvMedicamentos.layoutManager = LinearLayoutManager(this.context)
        rvMedicamentos.adapter = adapter



    }

    private fun sendResult(){

       /* val action = MedicamentoDialogFragmentDirections.actionMedicamentoDialogFragmentToCargarRecetaFragment3(null)
        findNavController().navigate(action)*/

        var datos = ArrayList<mMedicamento>()

        for (mmedicamento in adapter.lmedicamentos){

            if(mmedicamento.isSelected){

                datos.add(mmedicamento)
               // println("este es el numero ${mmedicamento.medicamentoid}")

            }

        }

        val intent = Intent()
        intent.putExtra("datos", datos )
        targetFragment!!.onActivityResult(targetRequestCode, RQUEST_CODE, intent)
        dismiss()



    }



    private fun handleError(error: Throwable) {

        Toast.makeText(this.context, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
        println(error.localizedMessage)

    }



}


