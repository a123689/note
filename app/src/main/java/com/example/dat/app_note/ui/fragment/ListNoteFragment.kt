package com.example.dat.app_note.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.dat.app_note.R
import com.example.dat.app_note.model.Folder
import com.example.dat.app_note.model.Note
import com.example.dat.app_note.ui.adapter.FolderAdapter
import com.example.dat.app_note.ui.adapter.NoteAdapter
import com.example.dat.app_note.utils.setPreventDoubleClick
import com.example.dat.app_note.viewmodel.FolderViewmodel
import com.example.dat.app_note.viewmodel.NoteViewmodel
import kotlinx.android.synthetic.main.fragment_list_note.*
import kotlinx.android.synthetic.main.fragment_list_note.tvFolder
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_dialog_bottom.*
import kotlin.properties.Delegates


class ListNoteFragment : BaseFragment(R.layout.fragment_list_note) {
    override fun onFragmentBackPressed() {
        findNavController().popBackStack()
    }

    lateinit var  noteAdapter:NoteAdapter
    lateinit var navController: NavController
     var idFolder = 0
    private val noteViewmodel: NoteViewmodel by lazy {
        ViewModelProvider(this, NoteViewmodel.NoteViewmodelFactory(requireActivity().application))[NoteViewmodel::class.java]
    }
    private val folderViewmodel: FolderViewmodel by lazy {
        ViewModelProvider(this, FolderViewmodel.NoteViewmodelFactory(requireActivity().application))[FolderViewmodel::class.java]
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        initControl()
        ivBack.setPreventDoubleClick(300){
            onFragmentBackPressed()
        }
        tvFolder.setPreventDoubleClick(300){
            onFragmentBackPressed()
        }

        addNote.setPreventDoubleClick(300){
            if(navController != null && navController.currentDestination?.id == R.id.listNoteFragment){
                val bundle = Bundle()
                bundle.putInt("id",idFolder)
                navController.navigate(R.id.action_listNoteFragment_to_writeNoteFragment,bundle)
            }
        }
    }


    private fun initControl() {
        recyclerviewNote.apply {
            idFolder = arguments?.getInt("id")!!
            noteAdapter = NoteAdapter(
                requireContext(),
                onItemClick,
                onItemLongClick
            )

            recyclerviewNote.layoutManager = LinearLayoutManager(requireActivity())
            adapter = noteAdapter
            if (idFolder != null) {

                noteViewmodel.getAllNote(idFolder).observe(requireActivity(), Observer {
                    try {
                        tvSize.text = it.size.toString()+" "+getString(R.string.note)
                    }catch (e:Exception){

                    }

                    noteAdapter.setNote(it)
                })

            }
        }

    }

    private val onItemClick: (Note)->Unit = {
        if(navController != null && navController.currentDestination?.id == R.id.listNoteFragment){
            val bundle = Bundle()
            bundle.putInt("id",idFolder)
            bundle.putParcelable("note",it)
            navController.navigate(R.id.action_listNoteFragment_to_writeNoteFragment,bundle)
        }

    }


    private val onItemLongClick: (Note)->Unit ={

    }
}