package com.dmobileapps.dat.app_note.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.Note
import com.dmobileapps.dat.app_note.utils.Common
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.dmobileapps.dat.app_note.viewmodel.FolderViewmodel
import com.dmobileapps.dat.app_note.viewmodel.NoteViewmodel
import kotlinx.android.synthetic.main.fragment_write_note.*
import kotlinx.android.synthetic.main.fragment_write_note.ivBack
import kotlinx.android.synthetic.main.fragment_write_note.tvFolder
import java.text.SimpleDateFormat
import java.util.*


class WriteNoteFragment : BaseFragment(R.layout.fragment_write_note) {
    override fun onFragmentBackPressed() {
        edContent.clearFocus()

        if(Common.checkMain){
            Common.checkMain = false
            findNavController().popBackStack()
        }else{
            findNavController().popBackStack(R.id.listNoteFragment,false)
        }

    }
    var idFolder = 0
     lateinit var  note:Note
    private val noteViewmodel: NoteViewmodel by lazy {
        ViewModelProvider(this, NoteViewmodel.NoteViewmodelFactory(requireActivity().application))[NoteViewmodel::class.java]
    }

    private val folderViewmodel: FolderViewmodel by lazy {
        ViewModelProvider(this, FolderViewmodel.NoteViewmodelFactory(requireActivity().application))[FolderViewmodel::class.java]
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(Common.checkInterface){
            layoutWrite.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorBlack))
            edContent.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorWhite))
        }else{
            edContent.setTextColor(ContextCompat.getColor(requireActivity(),R.color.colorBlack))
            Glide.with(requireActivity()).load(R.drawable.background_write).into(ivBackground)
        }


        try {
            idFolder = requireArguments().getInt("id")
            note = requireArguments().getParcelable<Note>("note")!!
            edContent.setText( note.content.toString())

        }catch (e:Exception){

        }


        tvDone.setPreventDoubleClick(300){
            if(edContent.text.toString().isNotEmpty()){
                if(Common.checkScreen){
                    Log.d("dat123","zxc")
                    val currentDate: String =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                    note.date = currentDate
                    note.content = edContent.text.toString()
                    noteViewmodel.updateNote(note)

                }else{
                    val currentDate: String =
                        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

                    val note = Note(edContent.text.toString(),currentDate,idFolder)
                    noteViewmodel.insertNote(note)
                    noteViewmodel.getAllNote(idFolder).observe(requireActivity(), androidx.lifecycle.Observer {
                        folderViewmodel.updateFolderById(idFolder,it.size)
                    })
                }

                onFragmentBackPressed()


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