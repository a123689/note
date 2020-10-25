package com.example.dat.app_note.ui.fragment

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.dat.app_note.R
import com.example.dat.app_note.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_splash.*


class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override fun onFragmentBackPressed() {

    }
    private lateinit var sharedPreference : SharedPreferences
    lateinit var  editor:SharedPreferences.Editor
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }
    private lateinit var  navController:NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        sharedPreference = activity?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!
        editor = sharedPreference.edit()

        if(sharedPreference.getBoolean("splash",false)){
            if (navController != null && navController.currentDestination?.id == R.id.splashFragment) {
                navController.navigate(R.id.action_splashFragment_to_mainFragment)
                return
            }
        }

        Glide.with(requireActivity()).load(R.drawable.icon_splash).into(imIcon)


        val splashTread: Thread = object : Thread() {
            override fun run() {
               sleep(3000)
                if (navController != null && navController.currentDestination?.id == R.id.splashFragment) {
                    editor.putBoolean("splash",true)
                    editor.apply()
                    navController.navigate(R.id.action_splashFragment_to_mainFragment)
                }
            }
        }
        splashTread.start()

    }
}