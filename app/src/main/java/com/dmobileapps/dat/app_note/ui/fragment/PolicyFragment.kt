package com.dmobileapps.dat.app_note.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.dmobileapps.dat.app_note.R
import kotlinx.android.synthetic.main.fragment_policy.*


class PolicyFragment : BaseFragment(R.layout.fragment_policy) {
    override fun onFragmentBackPressed() {
       findNavController().popBackStack()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvFolderBack.setOnClickListener {
            onFragmentBackPressed()
        }
        ivBackPoli.setOnClickListener {
            onFragmentBackPressed()
        }
    }
}