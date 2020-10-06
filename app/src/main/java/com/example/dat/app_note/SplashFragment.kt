package com.example.dat.app_note

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_splash.*


class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    override fun onFragmentBackPressed() {

    }

    lateinit var  navController:NavController
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        Glide.with(requireActivity()).load(R.drawable.icon_splash).into(imIcon)
        Handler().postDelayed({
            if(navController != null && navController.currentDestination?.id == R.id.splashFragment){
                navController.navigate(R.id.action_splashFragment_to_mainFragment)
            }
        },3000)
    }
}