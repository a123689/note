package com.example.dat.app_note.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.LayoutRes
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager

abstract class BaseFragment(
    @LayoutRes layout: Int
) : Fragment(layout) {

    lateinit var glide: RequestManager

    abstract fun onFragmentBackPressed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this, true) {
            onFragmentBackPressed()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        glide = Glide.with(view)
    }

    fun isSafe(): Boolean {
        return !(this.isRemoving || this.activity == null || this.isDetached || !this.isAdded || view == null)
    }
}