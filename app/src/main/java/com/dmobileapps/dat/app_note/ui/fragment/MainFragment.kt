package com.dmobileapps.dat.app_note.ui.fragment

//import com.adconfigonline.AdHolderOnline
//import com.adconfigonline.untils.AdDef
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.Folder
import com.dmobileapps.dat.app_note.ui.adapter.FolderAdapter
import com.dmobileapps.dat.app_note.utils.Common
import com.dmobileapps.dat.app_note.utils.hideKeyboard
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.dmobileapps.dat.app_note.viewmodel.FolderViewmodel
import kotlinx.android.synthetic.main.dialog_add_folder.view.*
import kotlinx.android.synthetic.main.dialog_add_folder.view.tvCancel
import kotlinx.android.synthetic.main.dialog_add_folder.view.tvSave
import kotlinx.android.synthetic.main.dialog_add_folder.view.view1
import kotlinx.android.synthetic.main.dialog_rename.view.*
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.android.synthetic.main.layout_dialog_bottom.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainFragment : BaseFragment(R.layout.fragment_main) {

    override fun onFragmentBackPressed() {
        if (layoutDialogBottom.visibility == View.VISIBLE) {
            setAnimationDialog()
        } else {
            activity?.finish()
        }

    }

    private lateinit var sharedPreference: SharedPreferences
    lateinit var folder: Folder
    lateinit var noteAdapter: FolderAdapter
    lateinit var navController: NavController
    private val folderViewmodel: FolderViewmodel by lazy {
        ViewModelProvider(
            this,
            FolderViewmodel.NoteViewmodelFactory(requireActivity().application)
        )[FolderViewmodel::class.java]
    }
    var idFolder = 0
    var list: MutableList<Folder> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        sharedPreference = activity?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!
        var editor = sharedPreference.edit()
        initControl()

        ivNewFolder.setPreventDoubleClick(300) {
            setDialog()
        }

        layoutDialogBottom.setPreventDoubleClick(300) {
            setAnimationDialog()
        }

        ivCancel.setPreventDoubleClick(300) {
            setAnimationDialog()
        }

        layoutDialogBottom2.setOnClickListener {

        }

        layoutAddFolder.setPreventDoubleClick(300) {
            setAnimationDialogUnit(setDialog())


        }

        layoutRename.setPreventDoubleClick(300) {
            setAnimationDialogUnit(setDialogRename())


        }

        layoutDelete.setPreventDoubleClick(300) {

            setAnimationDialogUnit(setDialogDelete())

        }

        layoutMove.setPreventDoubleClick(300) {
            Toast.makeText(activity, getString(R.string.feature), Toast.LENGTH_SHORT).show()
        }

        layoutShare.setPreventDoubleClick(300) {
            Toast.makeText(activity, getString(R.string.feature), Toast.LENGTH_SHORT).show()
        }

        edSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                try {
                    noteAdapter.searchFolder(p0.toString())
                } catch (e: Exception) {

                }

            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        ivSetting.setPreventDoubleClick(300) {
            if (navController.currentDestination?.id == R.id.mainFragment) {
                navController.navigate(R.id.action_mainFragment_to_settingFragment)
            }
        }

        btnAddNote.setPreventDoubleClick(300) {
            if (navController.currentDestination?.id == R.id.mainFragment) {
                val job: Job = GlobalScope.launch {
                    if (list.size == 0) {
                        var folder = Folder("My folder", 0)
                        folderViewmodel.insertFolder(folder)
                    }
                }
                val job2: Job = GlobalScope.launch {
                    job.join()
                    delay(50)
                    Common.checkMain = true
                    Common.checkScreen = false
                    val bundle = Bundle()
                    bundle.putInt("id", idFolder)
                    navController.navigate(R.id.action_mainFragment_to_writeNoteFragment, bundle)
                }

            }
            showMenu(lnMenu.visibility == View.GONE)
        }
        btnCheckList.setPreventDoubleClick(300) {

            if (navController.currentDestination?.id == R.id.mainFragment) {
//            val job: Job = GlobalScope.launch {
                if (list.size == 0) {
                    var folder = Folder("My folder", 0)
                    folderViewmodel.insertFolder(folder)
                }
//
//            }
//            val job2: Job = GlobalScope.launch {
//                job.join()
//                delay(50)
                Common.checkMain = true
                Common.checkScreen = false
                val bundle = Bundle()
                bundle.putInt("id", idFolder)
                navController.navigate(R.id.action_mainFragment_to_checkListFragment,bundle)
//            }
            }
            showMenu(lnMenu.visibility == View.GONE)

        }
        ivAddNote.setPreventDoubleClick(300) {
            showMenu(lnMenu.visibility == View.GONE)
        }
        if (!sharedPreference.getBoolean("data", false)) {
            var folder = Folder()
            folder.name = "My folder"
            folderViewmodel.insertFolder(folder)
            editor.putBoolean("data", true)
            editor.apply()

        }

        ivAddNote2.setPreventDoubleClick(300) {
            if (navController.currentDestination?.id == R.id.mainFragment) {
                Common.checkMain = true
                Common.checkScreen = false
                val bundle = Bundle()
                bundle.putInt("id", idFolder)
                navController.navigate(R.id.action_mainFragment_to_writeNoteFragment, bundle)
            }
        }

        Common.checkInterface = sharedPreference.getBoolean("interface", false)
        if (Common.checkInterface) {
            interfaceBlack()
        }
        edSearch.clearFocus()
        view.hideKeyboard()
        loadBanner()
    }


    private fun interfaceBlack() {
        layoutMain.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorBlack
            )
        )
        ivSetting.setImageResource(R.drawable.ic_settings_white)
        tvFolder.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        edSearch.setBackgroundResource(R.drawable.custom_background_edittext_black)
        edSearch.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.colorSearchText))
        edSearch.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        layoutBottom.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorToolbarBlack
            )
        )
        // layoutFolder.setBackgroundResource(R.drawable.custom_background_reyclewview)
        recyclerviewFolder.setBackgroundResource(R.drawable.custom_background_reyclewview)

        layoutDialogBottom2.setBackgroundResource(R.drawable.custom_background_edittext_black)
        tvNameFolder.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        layoutShare.setBackgroundResource(R.drawable.ripper_top_black)
        layoutAddFolder.setBackgroundResource(R.drawable.ripper_background_white)
        layoutMove.setBackgroundResource(R.drawable.ripper_background_white)
        layoutRename.setBackgroundResource(R.drawable.ripper_background_white)
        layoutDelete.setBackgroundResource(R.drawable.ripper_bottom_black)
        tvShare.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvAdd.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvMove.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvRename.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvDelete.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        ivShare.setImageResource(R.drawable.ic_share_white)
        ivAddFolderDialog.setImageResource(R.drawable.ic_add_folder_white)
        ivMove.setImageResource(R.drawable.ic_folder_white)
        ivRename.setImageResource(R.drawable.ic_rename_white)
        ivDeleteDialog.setImageResource(R.drawable.ic_delete_white)
        layout.setBackgroundResource(R.drawable.custom_background_dialog2_black)
        viewDialog6.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorView1))
        viewDialog2.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorView2))
        viewDialog3.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorView2))
        viewDialog4.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorView2))
        viewDialog5.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorView2))
        ivCancel.setImageResource(R.drawable.ic_cancel_black)
    }

    fun loadBanner() {
//        AdHolderOnline(activity).showAdsTotalOffline(
//            Common.NATIVE_MAIN_NOTE,
//            ll_ads,
//            "",
//            object : AdHolderOnline.AdHolderCallback {
//                override fun onAdShow(
//                    @AdDef.NETWORK network: String?,
//                    @AdDef.AD_TYPE adtype: String?
//                ) {
//                    llads2?.stopShimmer()
//                    llads2?.visibility = View.GONE
//                }
//
//                override fun onAdClose(adType: String) {
//
//                }
//
//                override fun onAdFailToLoad(messageError: String) {
//                    ads2?.visibility = View.GONE
//                }
//
//                override fun onAdOff() {
//                    //NOTHING TO DO HERE
//                }
//            })

    }

    private fun setDialogDelete() {
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

    private fun setAnimationDialog() {
        YoYo.with(Techniques.SlideOutDown)
            .duration(800)
            .onEnd {
                layoutDialogBottom.visibility = View.GONE
            }
            .playOn(layoutDialogBottom)
    }

    private fun setAnimationDialogUnit(unit: Unit) {
        YoYo.with(Techniques.SlideOutDown)
            .duration(800)
            .onEnd {
                layoutDialogBottom.visibility = View.GONE
                unit
            }
            .playOn(layoutDialogBottom)
    }

    private fun showMenu(boolean: Boolean) {
        if (boolean) {
            YoYo.with(Techniques.FadeInUp).duration(150).onEnd {
                lnMenu?.visibility = View.VISIBLE
            }.playOn(lnMenu)
        } else {
            YoYo.with(Techniques.FadeOutDown).duration(150).onEnd {
                lnMenu?.visibility = View.GONE
            }.playOn(lnMenu)
        }
    }

    private fun setDialog() {
        val dialog: Dialog
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_add_folder, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        if (!dialog.isShowing) {
            if (Common.checkInterface) {
                view.layourDialogAddFolder.setBackgroundResource(R.drawable.custom_background_dialog_add_folder_black)
                view.tvNewfolder.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                view.tvEntername.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                view.edNameFolder.setBackgroundResource(R.drawable.custom_background_edittext_black)
                view.edNameFolder.setHintTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorTextDialogBack
                    )
                )
                view.edNameFolder.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                view.tvSave.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorTextDialogBack
                    )
                )
                view.view1.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBorder
                    )
                )
                view.view2.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBorder
                    )
                )
            }

            dialog.show()
        }
        view.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        view.tvSave.setOnClickListener {
            if (view.edNameFolder.text.toString().isEmpty()) {
                Toast.makeText(activity, getString(R.string.entername), Toast.LENGTH_SHORT).show()
            } else {
                val folder = Folder(view.edNameFolder.text.toString(), 0)
                folderViewmodel.insertFolder(folder)
                dialog.dismiss()
            }
        }


    }


    private fun setDialogRename() {
        val dialog: Dialog
        val view: View = LayoutInflater.from(context).inflate(R.layout.dialog_rename, null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!dialog.isShowing) {
            if (Common.checkInterface) {
                view.layoutDialogReanme.setBackgroundResource(R.drawable.custom_background_dialog_add_folder_black)
                view.tvNewfolders.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                view.tvEnternames.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                view.edNameFolders.setBackgroundResource(R.drawable.custom_background_edittext_black)
                view.edNameFolders.setHintTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorTextDialogBack
                    )
                )
                view.edNameFolders.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorWhite
                    )
                )
                view.tvSave.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorTextDialogBack
                    )
                )
                view.view1.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBorder
                    )
                )
                view.view3.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorBorder
                    )
                )
            }

            dialog.show()
            view.edNameFolders.setText(folder.name)
        }

        view.tvCancel.setOnClickListener {
            dialog.dismiss()
        }

        view.tvSave.setOnClickListener {
            if (view.edNameFolders.text.toString().isEmpty()) {
                Toast.makeText(activity, getString(R.string.enternamefolder), Toast.LENGTH_SHORT)
                    .show()
            } else {
                rename(view.edNameFolders.text.toString())
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

                if (it.size > 0) {
                    idFolder = it[0].id
                }

                noteAdapter.setFolder(it)
            })
        }

    }

    private val onItemClick: (Folder) -> Unit = {
        if (navController != null && navController.currentDestination?.id == R.id.mainFragment) {
            val bundle = Bundle()
            bundle.putInt("id", it.id)
            navController.navigate(R.id.action_mainFragment_to_listNoteFragment, bundle)
            edSearch.clearFocus()
        }
    }

    private fun rename(name: String) {
        this.folder.name = name
        folderViewmodel.updateFolder(folder)
    }

    private val onItemLongClick: (Folder) -> Unit = {
        tvNameFolder.text = it.name
        this.folder = it
        layoutDialogBottom.visibility = View.VISIBLE
        YoYo.with(Techniques.BounceInUp)
            .duration(800)
            .playOn(layoutDialogBottom)

    }
}