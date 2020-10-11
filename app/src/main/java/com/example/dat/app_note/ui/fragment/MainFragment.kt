package com.example.dat.app_note.ui.fragment

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.example.dat.app_note.R
import com.example.dat.app_note.model.Folder
import com.example.dat.app_note.ui.adapter.FolderAdapter
import com.example.dat.app_note.utils.setPreventDoubleClick
import com.example.dat.app_note.viewmodel.FolderViewmodel
import com.example.dat.app_note.viewmodel.NoteViewmodel
import kotlinx.android.synthetic.main.dialog_add_folder.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_dialog_bottom.*
import kotlinx.android.synthetic.main.layout_dialog_bottom.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Exception



class MainFragment : BaseFragment(R.layout.fragment_main) {

    override fun onFragmentBackPressed() {
        activity?.finish()
    }

    lateinit var folder:Folder
    lateinit var  noteAdapter:FolderAdapter
    lateinit var navController: NavController
    private val folderViewmodel:FolderViewmodel by lazy {
        ViewModelProvider(this, FolderViewmodel.NoteViewmodelFactory(requireActivity().application))[FolderViewmodel::class.java]
    }
    var idFolder = 0
    var list:MutableList<Folder> = mutableListOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initControl()

        ivNewFolder.setPreventDoubleClick(300) {
            setDialog()
        }

        layoutDialogBottom.setPreventDoubleClick(300) {
            setAnimationDialog()
        }

        ivCancel.setPreventDoubleClick(300){
            setAnimationDialog()
        }

        layoutDialogBottom2.setOnClickListener {

        }

        layoutAddFolder.setPreventDoubleClick(300) {
            setAnimationDialogUnit(setDialog())


        }

        layoutRename.setPreventDoubleClick(300){
            setAnimationDialogUnit(setDialogRename())


        }

        layoutDelete.setPreventDoubleClick(300){

            setAnimationDialogUnit( setDialogDelete())

        }

        layoutMove.setPreventDoubleClick(300){
            Toast.makeText(activity, getString(R.string.feature), Toast.LENGTH_SHORT).show()
        }

        layoutShare.setPreventDoubleClick(300){
            Toast.makeText(activity, getString(R.string.feature), Toast.LENGTH_SHORT).show()
        }

        edSearch.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    noteAdapter.searchFolder(p0.toString())
                }catch (e:Exception){

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        ivSetting.setPreventDoubleClick(300){
            if(navController.currentDestination?.id == R.id.mainFragment){
                navController.navigate(R.id.action_mainFragment_to_settingFragment)
            }
        }
        ivAddNote.setPreventDoubleClick(300){
            if(navController.currentDestination?.id == R.id.mainFragment){
                val job: Job = GlobalScope.launch {
                    if(list.size == 0){
                        var folder = Folder("My folder",0)
                        folderViewmodel.insertFolder(folder)
                    }
                }
                val job2: Job = GlobalScope.launch {
                    job.join()
                    delay(50)
                    val bundle = Bundle()
                    bundle.putInt("id",idFolder)
                    navController.navigate(R.id.action_mainFragment_to_writeNoteFragment,bundle)
                }

            }
        }

        ivAddNote2.setPreventDoubleClick(300){
            if(navController.currentDestination?.id == R.id.mainFragment){

                val bundle = Bundle()
                bundle.putInt("id",idFolder)
                navController.navigate(R.id.action_mainFragment_to_writeNoteFragment,bundle)
            }
        }
    }




    private fun setDialogDelete(){
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.deletefodler))
            .setMessage(getString(R.string.areyousure))
            .setPositiveButton(
                getString(R.string.yes)
            ) { dialog, which ->
                folderViewmodel.deleteFolder(folder)


            }
            .setNegativeButton(getString(R.string.no), null)
            .show()
    }

    private fun setAnimationDialog(){
        YoYo.with(Techniques.SlideOutDown)
            .duration(800)
            .onEnd {
                layoutDialogBottom.visibility = View.GONE
            }
            .playOn(layoutDialogBottom)
    }

    private fun setAnimationDialogUnit(unit:Unit){
        YoYo.with(Techniques.SlideOutDown)
            .duration(800)
            .onEnd {
                layoutDialogBottom.visibility = View.GONE
                unit
            }
            .playOn(layoutDialogBottom)
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
                Toast.makeText(activity, getString(R.string.entername), Toast.LENGTH_SHORT).show()
            }else{
                val folder = Folder(view.edNameFolder.text.toString(), 0)
                folderViewmodel.insertFolder(folder)
                dialog.dismiss()
            }
        }


    }

    private fun setDialogRename(){
        val dialog: Dialog
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_rename, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!dialog.isShowing) {
            dialog.show()
            view.edNameFolder.setText(folder.name)
        }

        view.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        view.tvSave.setOnClickListener {
            if(view.edNameFolder.text.toString().isEmpty()){
                Toast.makeText(activity, getString(R.string.enternamefolder), Toast.LENGTH_SHORT).show()
            }else{
                rename(view.edNameFolder.text.toString())
                dialog.dismiss()
            }
        }



    }

    private fun initControl() {

        recyclerviewFolder.apply {
             noteAdapter = FolderAdapter(
                requireContext(),
                onItemClick,
                onItemLongClick
            )
            recyclerviewFolder.layoutManager = LinearLayoutManager(requireActivity())
            adapter = noteAdapter

            folderViewmodel.getAllFolder().observe(requireActivity(), Observer {
                list = it

                if(it.size > 0){
                    Log.d("dat123",it[0].id.toString()+"main")
                    idFolder = it[0].id
                }

                noteAdapter.setFolder(it)
            })
        }

    }

    private val onItemClick: (Folder)->Unit = {
        if(navController != null && navController.currentDestination?.id == R.id.mainFragment){
            val  bundle = Bundle()
            bundle.putInt("id",it.id)
            navController.navigate(R.id.action_mainFragment_to_listNoteFragment,bundle)
            edSearch.clearFocus()
        }
    }

    private fun rename(name: String){
        this.folder.name = name
        folderViewmodel.updateFolder(folder)
    }

    private val onItemLongClick: (Folder)->Unit ={
        tvNameFolder.text = it.name
        this.folder = it
        layoutDialogBottom.visibility = View.VISIBLE
        YoYo.with(Techniques.BounceInUp)
            .duration(800)
            .playOn(layoutDialogBottom)

    }
}