package com.blackspider.recetapp.fragments


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager

import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterReceta
import com.blackspider.recetapp.model.mPaciente
import com.blackspider.recetapp.model.mReceta
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.recursos.sessionManager
import com.blackspider.recetapp.request.requestPaciente
import com.blackspider.recetapp.request.requestReceta
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_paciente.*
import kotlinx.android.synthetic.main.toolbar.view.*

/**
 * A simple [Fragment] subclass.
 */
class PacienteFragment : Fragment() {

    private lateinit var mCompositeDisposable : CompositeDisposable
    //private val pacienteid: Long = 1
    private val args : PacienteFragmentArgs by navArgs()
    private lateinit var adapter : adapterReceta
    private lateinit var session : sessionManager

    private var resume = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_paciente, container, false)

        (activity as AppCompatActivity).setSupportActionBar(view.app_bar)

        setHasOptionsMenu(true)

        return view
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        session = sessionManager(activity!!.applicationContext)

        fabAddReceta.setOnClickListener {

            val action = PacienteFragmentDirections.actionPacienteFragmentToCargarRecetaFragment(args.pacienteid, args.medicoid)
            findNavController().navigate(action)

        }

       /* iv_paciente.setOnClickListener{

            val action = PacienteFragmentDirections.actionPacienteFragmentToPerfilFragment(args.pacienteid)
            findNavController().navigate(action)

        }*/



        if (!args.medico){

            bottomappbar.visibility = View.GONE
            fabAddReceta.visibility = View.GONE
        }

        mCompositeDisposable = CompositeDisposable()

        if (args.medico){

            loadJsonRecetaPorMedico()

        }else{

            loadJsonReceta()

        }




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

    fun loadJsonRecetaPorMedico(){

        val retrofit = connector().create(requestReceta::class.java)
        mCompositeDisposable.add(
            retrofit.getRecetaPaciente(args.pacienteid, args.medicoid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseReceta, this::handleError)
        )

    }

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

        adapter = adapterReceta(lreceta)

        adapter.setOnClickListener(View.OnClickListener{

            val recetaid = adapter.lrecetas[rvRecetas.getChildAdapterPosition(it)].recetaid
            val action = PacienteFragmentDirections.actionPacienteFragmentToRecetaFragment(recetaid!!)
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

    // menu toolbar
    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)
        menuInflater.inflate(R.menu.toolbar_menu, menu)

        val actionPerfil = menu.findItem(R.id.action_perfil)

        if (args.medico){

            actionPerfil.isVisible = false

        }

        val menuItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(menuItem) as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                if (adapter != null ){

                    adapter.filter.filter(newText)

                }
                return true
            }


        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> {
                TransitionManager.beginDelayedTransition(
                    activity!!.findViewById<View>(
                        R.id.app_bar
                    ) as ViewGroup
                )
                MenuItemCompat.getActionProvider(item)
                return true


            }

            R.id.action_perfil -> {

                val action = PacienteFragmentDirections.actionPacienteFragmentToPerfilFragment(args.pacienteid, false, 0)
                findNavController().navigate(action)


            }

            R.id.action_logout -> {

                session.logoutUser()
                val navBuilder = NavOptions.Builder()
                val navOptions: NavOptions = navBuilder.setPopUpTo(R.id.nav_graph,true).build()
                findNavController().navigate(R.id.loginFragment,null ,navOptions)

            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()

        if(resume){

            //Toast.makeText(this.context,"Resumio",Toast.LENGTH_LONG).show()
            if (args.medico){

                loadJsonRecetaPorMedico()

            }else{

                loadJsonReceta()

            }

        }

    }

    override fun onPause() {
        super.onPause()

        resume = true
    }
}

