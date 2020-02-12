package com.blackspider.recetapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView


import androidx.fragment.app.DialogFragment
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
class MedicamentoDialogFragment : DialogFragment(){

    private lateinit var mCompositeDisposable : CompositeDisposable
    private lateinit var adapter : adapterMedicamento

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


        adapter = adapterMedicamento(lmedicamentos)

        rvMedicamentos.layoutManager = LinearLayoutManager(this.context)
        rvMedicamentos.adapter = adapter


    }

    private fun handleError(error: Throwable) {

        Toast.makeText(this.context, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
        println(error.localizedMessage)

    }

}


