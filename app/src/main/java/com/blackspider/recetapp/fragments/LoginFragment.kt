package com.blackspider.recetapp.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs

import com.blackspider.recetapp.R
import kotlinx.android.synthetic.main.fragment_login.*

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_iniciar.setOnClickListener{

            if(rbPaciente.isChecked || rbMedico.isChecked) {

                if (rbPaciente.isChecked) {
                    val action = LoginFragmentDirections.actionLoginFragmentToPacienteFragment(1)
                    findNavController().navigate(action)
                }

                if (rbMedico.isChecked){

                    val action = LoginFragmentDirections.actionLoginFragmentToMedicoFragment()
                    findNavController().navigate(action)

                }

            }else{

                Toast.makeText(context, "Debes de seleccionar Medico o Paciente", Toast.LENGTH_LONG).show()

            }


        }

        btn_regitrar.setOnClickListener{



            val action = LoginFragmentDirections.actionLoginFragmentToRegistroFragment()
            findNavController().navigate(action)

        }

    }

}
