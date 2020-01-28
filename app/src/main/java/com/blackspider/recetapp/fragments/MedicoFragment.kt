package com.blackspider.recetapp.fragments


import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterPaciente
import com.blackspider.recetapp.model.mMedico
import com.blackspider.recetapp.model.mMedicoPaciente
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestMedico
import com.blackspider.recetapp.request.requestMedicoPaciente
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_medico.*
import kotlinx.android.synthetic.main.fragment_medico.view.*


/**
 * A simple [Fragment] subclass.
 */
class MedicoFragment : Fragment() {

    val medicoid : Long = 2
    private lateinit var mCompositeDisposable : CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_medico, container, false)

        (activity as AppCompatActivity).setSupportActionBar(view.app_bar)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCompositeDisposable = CompositeDisposable()
        loadJsonMedico()
        loadJsonPacientes()
    }

    private fun loadJsonMedico() {
        val retrofit = connector().create(requestMedico::class.java)
        mCompositeDisposable.add(
            retrofit.getOneMedico(medicoid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleMedico, this::handleError)
        )
    }

    private fun loadJsonPacientes() {
        val retrofit = connector().create(requestMedicoPaciente::class.java)
        mCompositeDisposable.add(
            retrofit.getPacientesxMedicos(medicoid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePacientes, this::handleError)
        )
    }

    fun handlePacientes(lmedicopacientes: ArrayList<mMedicoPaciente>){

        var adapter = adapterPaciente(lmedicopacientes)

        adapter.setOnClickListener(View.OnClickListener {

            val action = MedicoFragmentDirections.actionMedicoFragmentToPerfilFragment()
            findNavController().navigate(action)

        })

        rvPacientes.layoutManager = LinearLayoutManager(this.context)
        rvPacientes.adapter = adapter


    }

    fun handleMedico (mMedico: mMedico){

        tvMedicoFullName.text = mMedico.mpersona.nombre+" "+mMedico.mpersona.apellido

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

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, menuInflater)
    }


}
