package com.blackspider.recetapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.blackspider.recetapp.R
import com.blackspider.recetapp.model.mLogin
import com.blackspider.recetapp.recursos.connector
import com.blackspider.recetapp.recursos.sessionManager
import com.blackspider.recetapp.request.requestLogin
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment(R.layout.fragment_login) {

   /* override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }*/

    private lateinit var  mCompositeDisposable : CompositeDisposable
    private lateinit var mlogin : mLogin
    private lateinit var session : sessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        session = sessionManager(activity!!.applicationContext)

        if (session.checkLogin()) {


            if (!session.getUserDetails().medico) {
              //  val action = LoginFragmentDirections.actionLoginFragmentToPacienteFragment(session.getUserDetails().id.toLong())
                //findNavController().navigate(action)

                findNavController().navigate(R.id.medicoFragment)


            }else{

                val action = LoginFragmentDirections.actionLoginFragmentToMedicoFragment(session.getUserDetails().id)
                findNavController().navigate(action)

            }
        }


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCompositeDisposable = CompositeDisposable()

        btn_iniciar.setOnClickListener{

           var medico = false



            if(rbPaciente.isChecked && tietUsername.text!!.isNotEmpty() || rbMedico.isChecked && tietUsername.text!!.isNotEmpty()) {

               /* if (rbPaciente.isChecked) {
                    val action = LoginFragmentDirections.actionLoginFragmentToPacienteFragment(1)
                    findNavController().navigate(action)
                }*/

                if (rbMedico.isChecked){

                    medico = true

                    /*val action = LoginFragmentDirections.actionLoginFragmentToMedicoFragment()
                    findNavController().navigate(action)*/

                }

                mlogin = mLogin(null, tietUsername.text.toString() ,medico)

                postJsonLogin()

            }else{

                Toast.makeText(context, "Debes de seleccionar Medico o Paciente y completar los campos de usuario", Toast.LENGTH_LONG).show()

            }




        }

        btn_regitrar.setOnClickListener{

            val action = LoginFragmentDirections.actionLoginFragmentToRegistroFragment()
            findNavController().navigate(action)

        }

    }

    private fun postJsonLogin(){

        val retrofit = connector().create(requestLogin::class.java)
        mCompositeDisposable.add(
            retrofit.postLogin(mlogin)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleLogin, this::handleError)
        )

    }

    private fun handleLogin(login: mLogin){

        if(login != null){

            session.createLoginSession(login.id!!,login.medico)

            if(login.medico){

                val action = LoginFragmentDirections.actionLoginFragmentToMedicoFragment(login.id)
                findNavController().navigate(action)

            }else{

                val action = LoginFragmentDirections.actionLoginFragmentToPacienteFragment(login.id)
                findNavController().navigate(action)

            }

        }else{

            Toast.makeText(this.context,"Usuario o contraseña no validos",Toast.LENGTH_LONG).show()

        }


    }

    private fun handleError(error: Throwable) {

        Toast.makeText(this.context, "Error ${error.localizedMessage}", Toast.LENGTH_LONG).show()
        println(error.localizedMessage)

    }




}
