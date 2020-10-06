package com.example.dat.app_note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController


class MainFragment : BaseFragment(R.layout.fragment_main) {

    override fun onFragmentBackPressed() {
        activity?.finish()
    }

}