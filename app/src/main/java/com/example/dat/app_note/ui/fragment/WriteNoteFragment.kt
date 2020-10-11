package com.example.dat.app_note.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.dat.app_note.R
import com.example.dat.app_note.model.Note
import com.example.dat.app_note.utils.setPreventDoubleClick
import com.example.dat.app_note.viewmodel.FolderViewmodel
import com.example.dat.app_note.viewmodel.NoteViewmodel
import kotlinx.android.synthetic.main.fragment_write_note.*
import kotlinx.android.synthetic.main.fragment_write_note.ivBack
import kotlinx.android.synthetic.main.fragment_write_note.tvFolder
import java.text.SimpleDateFormat
import java.util.*


class WriteNoteFragment : BaseFragment(R.layout.fragment_write_note) {
    override fun onFragmentBackPressed() {
        findNavController().popBackStack()
    }
    var idFolder = 0
     var  note = Note()
    private val noteViewmodel: NoteViewmodel by lazy {
        ViewModelProvider(this, NoteViewmodel.NoteViewmodelFactory(requireActivity().application))[NoteViewmodel::class.java]
    }

    private val folderViewmodel: FolderViewmodel by lazy {
        ViewModelProvider(this, FolderViewmodel.NoteViewmodelFactory(requireActivity().application))[FolderViewmodel::class.java]
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(requireActivity()).load(R.drawable.background_write).into(ivBackground)
        try {
            idFolder = requireArguments().getInt("id")
            note = requireArguments().getParcelable<Note>("note")!!
            edContent.setText( note.content.toString())

        }catch (e:Exception){

        }


        tvDone.setPreventDoubleClick(300){
            if(edContent.text.toString().isNotEmpty()){
                val currentDate: String =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                val note = Note(edContent.text.toString(),currentDate,idFolder)
                noteViewmodel.insertNote(note)
                noteViewmodel.getAllNote(idFolder).observe(requireActivity(), androidx.lifecycle.Observer {
                    folderViewmodel.updateFolderById(idFolder,it.size)
                })
                edContent.clearFocus()
                findNavController().popBackStack()

            }
        }
        ivBack.setPreventDoubleClick(300){
            onFragmentBackPressed()
        }

        tvFolder.setPreventDoubleClick(300){
            onFragmentBackPressed()
        }
    }
}