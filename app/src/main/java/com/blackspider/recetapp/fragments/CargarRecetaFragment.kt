package com.blackspider.recetapp.fragments


import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blackspider.recetapp.R
import com.blackspider.recetapp.adapter.adapterMedicamentoRecetaDato
import com.blackspider.recetapp.model.mMedicamento
import com.blackspider.recetapp.model.mPaciente
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.request.requestPaciente
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.fragment_cargar_receta.*


/**
 * A simple [Fragment] subclass.
 */
class CargarRecetaFragment : Fragment(R.layout.fragment_cargar_receta) {

    private val REQUEST_CODE = 1
    private  var adapter = adapterMedicamentoRecetaDato(ArrayList<mMedicamento>())
    private lateinit var mCompositeDisposable : CompositeDisposable
    private val args : CargarRecetaFragmentArgs by navArgs()
    private var isOpen = false

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
            adapter.notifyDataSetChanged()

            println("esta es la cantidad que hay"+adapter.lmedicamentos.size)

            adapter
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

        mCompositeDisposable = CompositeDisposable()

        fabMenu.setOnClickListener{

            fabMenu.isExpanded = !fabMenu.isExpanded
            animacion()

        }

        fabGuardarReceta.setOnClickListener{

            Toast.makeText(context,"Guardar",Toast.LENGTH_LONG).show()

            fabMenu.isExpanded = !fabMenu.isExpanded
            animacion()

        }

        fabaddMedicamento2.setOnClickListener{


            val dm = parentFragmentManager.beginTransaction()
            val mdf = MedicamentoDialogFragment(adapter.lmedicamentos)
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

    fun animacion(){


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
