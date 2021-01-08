package com.dmobileapps.dat.app_note.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.adconfigonline.AdHolderOnline
import com.adconfigonline.server.AdsChild
import com.adconfigonline.untils.AdDef
//import com.adconfigonline.AdHolderOnline
//import com.adconfigonline.server.AdsChild
//import com.adconfigonline.untils.AdDef
import com.bumptech.glide.Glide
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.utils.Common
import kotlinx.android.synthetic.main.fragment_splash.*
import java.util.*


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
        AdHolderOnline(activity).isDebugMode = false
       // getListAD()
        if(sharedPreference.getBoolean("splash",false)){
            if ( navController.currentDestination?.id == R.id.splashFragment) {
                navController.navigate(R.id.action_splashFragment_to_mainFragment)
                return
            }
        }

        Glide.with(requireActivity()).load(R.drawable.icon_splash).into(imIcon)


        val splashTread: Thread = object : Thread() {
            override fun run() {
               sleep(3000)
                if (navController.currentDestination?.id == R.id.splashFragment) {
                    editor.putBoolean("splash",true)
                    editor.apply()
                    navController.navigate(R.id.action_splashFragment_to_mainFragment)
                }
            }
        }
        splashTread.start()

    }
//    private val adsHashMap = HashMap<String, Stack<AdsChild>>()
//    private fun getListAD() {
//        val native_main_note = Stack<AdsChild>()
//        val a = AdsChild(
//            "ca-app-pub-4040515803655174/7727739213",
//            AdDef.NETWORK.GOOGLE,
//            AdDef.GOOGLE_AD_TYPE.NATIVE,
//            AdDef.GOOGLE_AD_NATIVE.NATIVE_LARGE,
//            AdDef.GOOGLE_AD_NATIVE_TEMPLATE.admob_native_home_pdf
//        )
//        a.backgroundColor = "none"
//        a.borderColor = "none"
//        native_main_note.push(a)
//        adsHashMap[Common.NATIVE_MAIN_NOTE] = native_main_note
//        AdHolderOnline(activity).setListAd(adsHashMap)
//    }
}