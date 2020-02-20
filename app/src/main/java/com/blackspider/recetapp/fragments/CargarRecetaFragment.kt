package com.blackspider.recetapp.fragments


import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterMedicamentoReceta
import com.blackspider.recetapp.model.*
import com.blackspider.recetapp.model.pk.mPkRecetaDetalle
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestPaciente
import com.blackspider.recetapp.request.requestReceta
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_cargar_receta.*


/**
 * A simple [Fragment] subclass.
 */

class CargarRecetaFragment : Fragment(R.layout.fragment_cargar_receta) {

    private val REQUEST_CODE_DATO = 2
    private val REQUEST_CODE = 1
    private var adapter = adapterMedicamentoReceta(ArrayList<mRecetaDetalle>())
    private lateinit var mCompositeDisposable : CompositeDisposable
    private val args : CargarRecetaFragmentArgs by navArgs()
    private var isOpen = false
    //private var  mrecetadetalles = mRecetaDetalles(null,null,null)
    private lateinit var mreceta : mReceta
    private var postear = false

    private val itemTouchHelper = object : SimpleCallback(0, RIGHT or LEFT ){
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
           return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

            adapter.lmedicamentos.removeAt(viewHolder.adapterPosition)
            adapter.notifyItemRemoved(viewHolder.adapterPosition)



           // println("esta es la cantidad que hay"+adapter.itemCount)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder,dX,dY,actionState,isCurrentlyActive)
                .addBackgroundColor(ContextCompat.getColor(context!!, R.color.colorPrimaryDark ))
                .addActionIcon(R.drawable.ic_delete)
                .setActionIconTint(Color.WHITE)
                .create()
                .decorate()

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter.setOnClickListener(View.OnClickListener {

            val dm = parentFragmentManager.beginTransaction()
            val mdf = MedicamentoDatoDialogFragment(adapter.lmedicamentos[rvRecetaDetalle.getChildAdapterPosition(it)])
            mdf.setTargetFragment(this,REQUEST_CODE_DATO)
            mdf.show(dm,"Medicamentos")

        })

        mCompositeDisposable = CompositeDisposable()

        fabMenu.setOnClickListener{

            fabMenu.isExpanded = !fabMenu.isExpanded
            animacion()

        }



        fabGuardarReceta.setOnClickListener{

            if (!postear){

                if (adapter.lmedicamentos.size > 0 && tietDiagnostico.text.toString().length > 1 ){

                    val set = HashSet<mRecetaDetalles>()

                    for (x in adapter.lmedicamentos){

                        val mrecetadetalles = mRecetaDetalles(x.mpkrecetadetalle.mmedicamento,x.indicaciones,x.dosis)

                        /*

                        mrecetadetalles.mmedicamento = x.mpkrecetadetalle.mmedicamento
                        mrecetadetalles.indicaciones = x.indicaciones
                        mrecetadetalles.dosis = x.dosis
                        */

                        set.add(mrecetadetalles)

                    }

                    val mmedico = mMedico(args.medicoid,null,null,null,null)
                    val mpaciente = mPaciente (args.pacientid, null,null,null)

                    mreceta = mReceta(null,mmedico,mpaciente,null,tietDiagnostico.text.toString(),"normal", set)

                    println("la cantidad del set es ${mreceta.mlrecetadetalle!!.size}")
                    println("la cantidad es ${mreceta.mlrecetadetalle!!.size}")

                    fabMenu.isExpanded = !fabMenu.isExpanded
                    animacion()

                    postear = true

                    postJsonReceta()







                }else{

                    Toast.makeText(context,"Debes de Cargar medicamentos y poner el diagnostico",Toast.LENGTH_LONG).show()

                }


            }






            //findNavController().popBackStack()


        }

        fabaddMedicamento2.setOnClickListener{

            var lmedicamento = ArrayList<mMedicamento>()

            for (x in adapter.lmedicamentos){

                lmedicamento.add(x.mpkrecetadetalle.mmedicamento!!)

            }


            val dm = parentFragmentManager.beginTransaction()
            val mdf = MedicamentoDialogFragment(lmedicamento)
            mdf.setTargetFragment(this,REQUEST_CODE)
            mdf.show(dm,"Medicamentos")

            fabMenu.isExpanded = !fabMenu.isExpanded
            animacion()


        }




        rvRecetaDetalle.layoutManager = LinearLayoutManager(this.context)
        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvRecetaDetalle)
        rvRecetaDetalle.adapter = adapter

        loadJsonPaciente()

    }

    private fun postJsonReceta(){

        val retrofit = connector().create(requestReceta::class.java)
        mCompositeDisposable.add(
            retrofit.postReceta(mreceta)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlePostReceta,this::handleError)
        )


    }

    private fun handlePostReceta(){

        findNavController().popBackStack()

    }

    private fun animacion(){


        val fabOpen = AnimationUtils.loadAnimation(context,R.anim.fab_open)
        val fabClose= AnimationUtils.loadAnimation(context,R.anim.fab_close)
        val fabClock = AnimationUtils.loadAnimation(context,R.anim.fab_rotate_clock)
        val fabAntiClock = AnimationUtils.loadAnimation(context,R.anim.fab_rotate_anticlock)

        when (isOpen){

            true -> {

                /*fabaddMedicamento2.visibility = View.GONE
                fabGuardarReceta.visibility = View.GONE*/

                fabaddMedicamento2.startAnimation(fabClose)
                fabGuardarReceta.startAnimation(fabClose)
                mtvGuardar.startAnimation(fabClose)
                mtvMedicamento.startAnimation(fabClose)



                isOpen = false


            }

            false -> {

                /* fabaddMedicamento2.visibility = View.VISIBLE
                 fabGuardarReceta.visibility = View.VISIBLE*/
                mtvGuardar.startAnimation(fabOpen)
                mtvMedicamento.startAnimation(fabOpen)
                fabaddMedicamento2.startAnimation(fabOpen)
                fabGuardarReceta.startAnimation(fabOpen)


                isOpen = true

            }

        }

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

                            if (adapter.lmedicamentos[i].mpkrecetadetalle.mmedicamento!!.medicamentoid == array[j].medicamentoid) {

                                array.removeAt(j)
                                break

                            }

                        }

                    }





                }



            }

            for (i in array) {

                val mRecetaDetalle = mRecetaDetalle(mPkRecetaDetalle(null, i),"","")

                adapter.add(mRecetaDetalle)

            }

        }else {

            if (requestCode == REQUEST_CODE_DATO) {

                for(x in adapter.lmedicamentos){

                    if (data != null && x.mpkrecetadetalle.mmedicamento!!.medicamentoid == data.extras!!["id"]){

                        x.indicaciones = data.extras!!["indicaciones"].toString()
                        x.dosis = data.extras!!["dosis"].toString()
                        adapter.notifyDataSetChanged()
                        break

                    }

                }

            }
        }







    }


    private fun handleError(error: Throwable) {

        Toast.makeText(this.context, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
        postear = false
       // println(error.localizedMessage)

    }

    override fun onDestroy() {
        super.onDestroy()

        mCompositeDisposable.clear()
        mCompositeDisposable.dispose()
    }



}
