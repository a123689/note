package com.example.dat.app_note.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dat.app_note.R
import com.example.dat.app_note.model.Folder
import com.example.dat.app_note.ui.adapter.FolderAdapter
import com.example.dat.app_note.viewmodel.FolderViewmodel
import kotlinx.android.synthetic.main.dialog_add_folder.view.*
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : BaseFragment(R.layout.fragment_main) {

    override fun onFragmentBackPressed() {
        activity?.finish()
    }

    private val noteViewmodel:FolderViewmodel by lazy {
        ViewModelProvider(this,FolderViewmodel.NoteViewmodelFactory(requireActivity().application))[FolderViewmodel::class.java]
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initControl()

        ivNewFolder.setOnClickListener {
            setDialog()
        }
    }

    private fun setDialog(){
        val dialog: Dialog
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_add_folder, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!dialog.isShowing) {
            dialog.show()
        }
        view.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        view.tvSave.setOnClickListener {
            if(view.edNameFolder.text.toString().isEmpty()){
                Toast.makeText(activity,"You must enter folder name",Toast.LENGTH_SHORT).show()
            }else{
                val folder = Folder(view.edNameFolder.text.toString(),"0")
                noteViewmodel.insertFolder(folder)
                dialog.dismiss()
            }
        }
    }

    private fun initControl() {
        recyclerviewFolder.apply {
            val noteAdapter:FolderAdapter = FolderAdapter(requireContext(),onItemClick,onItemDelete)
            recyclerviewFolder.layoutManager = LinearLayoutManager(requireActivity())
            adapter = noteAdapter
            noteViewmodel.getAllFolder().observe(requireActivity(), Observer {
                noteAdapter.setFolder(it)
            })
        }

    }

    private val onItemClick:(Folder)->Unit = {

    }

    private val onItemDelete:(Folder)->Unit ={
       // noteViewmodel.deleteNote(it)
    }
}