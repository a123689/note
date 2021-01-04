package com.dmobileapps.dat.app_note.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.Note
import com.dmobileapps.dat.app_note.ui.adapter.NoteAdapter
import com.dmobileapps.dat.app_note.utils.Common
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.dmobileapps.dat.app_note.viewmodel.FolderViewmodel
import com.dmobileapps.dat.app_note.viewmodel.NoteViewmodel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_list_note.*
import kotlinx.android.synthetic.main.fragment_list_note.tvFolder
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ListNoteFragment : BaseFragment(R.layout.fragment_list_note) {
    override fun onFragmentBackPressed() {
        edSearchNote.clearFocus()
        findNavController().popBackStack()
    }

    lateinit var noteAdapter: NoteAdapter
    lateinit var navController: NavController
    lateinit var listBackup: MutableList<Note>
    private lateinit var sharedPreference: SharedPreferences
    var idFolder = 0
    private val noteViewmodel: NoteViewmodel by lazy {
        ViewModelProvider(
            this,
            NoteViewmodel.NoteViewmodelFactory(requireActivity().application)
        )[NoteViewmodel::class.java]
    }
    private val folderViewmodel: FolderViewmodel by lazy {
        ViewModelProvider(
            this,
            FolderViewmodel.NoteViewmodelFactory(requireActivity().application)
        )[FolderViewmodel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreference = activity?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!
        navController = findNavController()
        initControl()
        ivBack.setPreventDoubleClick(300) {
            onFragmentBackPressed()
        }
        tvFolder.setPreventDoubleClick(300) {
            onFragmentBackPressed()
        }

        addNote.setPreventDoubleClick(300) {
            showMenu(lnMenuNote.visibility == View.GONE)
        }


        rlAddNote.setPreventDoubleClick(300) {
            if (navController != null && navController.currentDestination?.id == R.id.listNoteFragment) {
                Common.checkScreen = false
                val bundle = Bundle()
                bundle.putInt("id", idFolder)
                navController.navigate(
                    R.id.action_listNoteFragment_to_writeNoteFragment,
                    bundle
                )
            }
            showMenu(lnMenuNote.visibility == View.GONE)
        }
        rlAddCheckList.setPreventDoubleClick(300) {
            if (navController != null && navController.currentDestination?.id == R.id.listNoteFragment) {
                Common.checkScreen = false
                val bundle = Bundle()
                bundle.putInt("id", idFolder)
                navController.navigate(
                    R.id.action_listNoteFragment_to_checkListFragment,
                    bundle
                )
            }
        }

        edSearchNote.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                noteAdapter.searchFolder(p0.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        tvDoneNote.setPreventDoubleClick(300) {
            edSearchNote.visibility = View.VISIBLE
            tvFolders.visibility = View.VISIBLE
            layoutBottomNote.visibility = View.VISIBLE
            layoutDeleteNote.visibility = View.GONE
            noteAdapter.check = false
            noteAdapter.resetCheck()
            noteAdapter.notifyDataSetChanged()

        }

        tvDeleteNote.setPreventDoubleClick(300) {
            var check = false
            for (item in listBackup) {
                if (item.isCheck) {
                    check = true
                }
            }
            if (!check) {
                Toast.makeText(activity, getString(R.string.youmustchoosenote), Toast.LENGTH_SHORT)
                    .show()
                return@setPreventDoubleClick
            }
            setDialogMutilDelete()
        }

        if (Common.checkInterface) {
            interfaceBlack()
        }

    }

    private fun showMenu(boolean: Boolean) {
        if (boolean) {
            YoYo.with(Techniques.FadeInUp).duration(150).onEnd {
                lnMenuNote?.visibility = View.VISIBLE
            }.playOn(lnMenuNote)
        } else {
            YoYo.with(Techniques.FadeOutDown).duration(150).onEnd {
                lnMenuNote?.visibility = View.GONE
            }.playOn(lnMenuNote)
        }
    }

    private fun interfaceBlack() {
        layoutListnote.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorBlack
            )
        )
        tvFolders.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        edSearchNote.setBackgroundResource(R.drawable.custom_background_edittext_black)
        edSearchNote.setHintTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorSearchText
            )
        )
        edSearchNote.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        layoutBottomNote.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorToolbarBlack
            )
        )
        layoutFolders.setBackgroundResource(R.drawable.custom_background_reyclewview)
        tvSize.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        layoutDeleteNote.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorToolbarBlack
            )
        )
    }

    private fun deleteNote(note: Note) {
        noteViewmodel.deleteNote(note)
    }

    private fun deleteMultiNote() {
        for (item in listBackup) {
            if (item.isCheck) {
                noteViewmodel.deleteNote(item)
            }
        }
    }

    private fun setDialogDelete(note: Note) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.deletenote))
            .setMessage(getString(R.string.areyousure))
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog, which ->
                deleteNote(note)
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun setDialogMutilDelete() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.deletenote))
            .setMessage(getString(R.string.areyousuremulti))
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog, which ->
                deleteMultiNote()
            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun initControl() {
        recyclerviewNote.apply {
            idFolder = arguments?.getInt("id")!!
            noteAdapter = NoteAdapter(
                requireContext(),
                onItemClick,
                onItemLongClick,
                onMultiDelete,
                onDelete,
                onLock
            )

            recyclerviewNote.layoutManager = LinearLayoutManager(requireActivity())
            adapter = noteAdapter
            if (idFolder != null) {

                noteViewmodel.getAllNote(idFolder).observe(requireActivity(), Observer {
                    try {
                        tvSize.text = it.size.toString() + " " + getString(R.string.note)
                    } catch (e: Exception) {

                    }

                    noteAdapter.setNote(it)
                })

            }
        }

    }

    private val onItemClick: (Note) -> Unit = {
        if (sharedPreference.getString(it.id.toString(), "")?.isEmpty()!!) {
            if (it.checkList.isNullOrEmpty()) {
                if (navController != null && navController.currentDestination?.id == R.id.listNoteFragment) {
                    Common.checkScreen = true
                    val bundle = Bundle()
                    bundle.putInt("id", idFolder)
                    bundle.putString("note", Gson().toJson(it))
                    navController.navigate(
                        R.id.action_listNoteFragment_to_writeNoteFragment,
                        bundle
                    )
                }
            } else {
                if (navController.currentDestination?.id == R.id.listNoteFragment) {
                    Common.checkScreen = true
                    val bundle = Bundle()
                    bundle.putInt("id", idFolder)
                    bundle.putString("note", Gson().toJson(it))
                    navController.navigate(
                        R.id.action_listNoteFragment_to_checkListFragment,
                        bundle
                    )
                }
            }


        } else {
//            if (it.checkList.isNullOrEmpty()) {
                if (navController.currentDestination?.id == R.id.listNoteFragment) {
                    val bundle = Bundle()
                    bundle.putInt("id", idFolder)
                    bundle.putString("note", Gson().toJson(it))
                    bundle.putBoolean("check", true)
                    navController.navigate(R.id.action_listNoteFragment_to_passCodeFragment, bundle)
                }
//            } else {
//                if (navController.currentDestination?.id == R.id.listNoteFragment) {
//                    Common.checkScreen = true
//                    val bundle = Bundle()
//                    bundle.putInt("id", idFolder)
//                    bundle.putBoolean("check", true)
//                    bundle.putString("note", Gson().toJson(it))
//                    navController.navigate(
//                        R.id.action_listNoteFragment_to_checkListFragment,
//                        bundle
//                    )
//                }
//            }
        }


    }


    private val onMultiDelete: (MutableList<Note>) -> Unit = {
        listBackup = it

    }

    private val onDelete: (Note) -> Unit = {
        setDialogDelete(it)
    }

    private val onLock: (Note) -> Unit = {
        if (navController.currentDestination?.id == R.id.listNoteFragment) {
            val bundle = Bundle()
            bundle.putString("note", Gson().toJson(it))
            bundle.putBoolean("check", false)
            navController.navigate(R.id.action_listNoteFragment_to_passCodeFragment, bundle)
        }
    }

    private val onItemLongClick: (Note) -> Unit = {
        edSearchNote.visibility = View.GONE
        tvFolders.visibility = View.GONE
        layoutBottomNote.visibility = View.GONE
        layoutDeleteNote.visibility = View.VISIBLE
    }
}