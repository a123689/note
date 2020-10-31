package com.dmobileapps.dat.app_note.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.Note
import com.dmobileapps.dat.app_note.utils.PassCodeView
import kotlinx.android.synthetic.main.fragment_pass_code.*
import java.lang.Exception


class PassCodeFragment : BaseFragment(R.layout.fragment_pass_code) {
    override fun onFragmentBackPressed() {
        findNavController().popBackStack()
    }
    private lateinit var sharedPreference :SharedPreferences

    var passwordBefore = ""
    var passwordAfter = ""
    lateinit var note:Note
    var check = false
    var idFolder = 0
    lateinit var navController: NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        sharedPreference = activity?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!
        val editor = sharedPreference.edit()
        try {
            note = arguments?.getParcelable<Note>("note")!!
            check = arguments?.getBoolean("check",false)!!
            idFolder = requireArguments().getInt("id")
        }catch (e:Exception){

        }

        setClick()

        passCodeView.setOnDoneListener(object : PassCodeView.PassCodeViewListener {
            override fun onPassCodeDone(passCode: String) {
                if(!check){
                    if(!sharedPreference.getString(note.id.toString(),"")?.isEmpty()!!){
                        if(sharedPreference.getString(note.id.toString(),"") == passCode){
                            editor.putString(note.id.toString(),"")
                            editor.apply()
                            onFragmentBackPressed()
                        }else{
                            tvWrongPass.text = getString(R.string.wrongpasscode)
                            passcodeWrong()
                        }
                    }else{
                        if(passwordBefore.isEmpty()){
                            passwordBefore = passCode
                            passCodeView.clearPassCode()
                            tvPasscode.text = getString(R.string.enterpasswordagain)
                        }else{
                            passwordAfter = passCode
                            if(passwordAfter == passwordBefore){
                                editor.putString(note.id.toString(),passCode)
                                editor.apply()
                                onFragmentBackPressed()
                            }else{
                                tvWrongPass.text = getString(R.string.theincorrectpassword)
                                passcodeWrong()
                            }
                        }
                    }

                }else{

                    if(sharedPreference.getString(note.id.toString(),"") == passCode){
                        if(navController.currentDestination?.id == R.id.passCodeFragment){
                            val  bundle = Bundle()
                            bundle.putParcelable("note",note)
                            bundle.putInt("id",idFolder)
                            navController.navigate(R.id.action_passCodeFragment_to_writeNoteFragment,bundle)
                        }
                    }else{
                        tvWrongPass.text = getString(R.string.wrongpasscode)
                        passcodeWrong()
                    }
                }


            }

        })
    }

    private fun passcodeWrong(){
        tvWrongPass.visibility = View.VISIBLE
        passCodeView.clearPassCode()
        passCodeView.playWrongPassAnimation()
    }
}