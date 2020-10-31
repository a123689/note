package com.dmobileapps.dat.app_note.ui.fragment

import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_pass_code.*

fun PassCodeFragment.setClick(){
    btn1.setOnClickListener {
        passCodeView.addPassCode(1)
    }
    btn2.setOnClickListener {
        passCodeView.addPassCode(2)
    }
    btn3.setOnClickListener {
        passCodeView.addPassCode(3)
    }
    btn4.setOnClickListener {
        passCodeView.addPassCode(4)
    }
    btn5.setOnClickListener {
        passCodeView.addPassCode(5)
    }
    btn6.setOnClickListener {
        passCodeView.addPassCode(6)
    }
    btn7.setOnClickListener {
        passCodeView.addPassCode(7)
    }
    btn8.setOnClickListener {
        passCodeView.addPassCode(8)
    }
    btn9.setOnClickListener {
        passCodeView.addPassCode(9)
    }
    btn0.setOnClickListener {
        passCodeView.addPassCode(0)
    }

    tvCancel.setOnClickListener {

        findNavController().popBackStack()
    }
}