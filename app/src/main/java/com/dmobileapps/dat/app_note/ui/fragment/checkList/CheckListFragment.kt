package com.dmobileapps.dat.app_note.ui.fragment.checkList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.CheckList
import com.dmobileapps.dat.app_note.model.Note
import com.dmobileapps.dat.app_note.model.RecordObj
import com.dmobileapps.dat.app_note.ui.adapter.CheckListAdapter
import com.dmobileapps.dat.app_note.ui.fragment.BaseFragment
import com.dmobileapps.dat.app_note.ui.fragment.chooseImage.ChooseImageAct
import com.dmobileapps.dat.app_note.utils.*
import com.dmobileapps.dat.app_note.viewmodel.FolderViewmodel
import com.dmobileapps.dat.app_note.viewmodel.NoteViewmodel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_check_list.*
import kotlinx.android.synthetic.main.fragment_check_list.ivBack
import kotlinx.android.synthetic.main.fragment_write_note.*
import nv.module.audiorecoder.ui.AudioActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckListFragment : BaseFragment(R.layout.fragment_check_list),
    BottomNavigationView.OnNavigationItemSelectedListener {


    private val arrCheckList: ArrayList<CheckList> = ArrayList()
    private lateinit var adapterCheckList: CheckListAdapter
    private var IS_CHOOSE = 0
    private var POSITION_FOCUS = 0
    private var isOnClick = false
    private var isPause = false

    private lateinit var player: SimpleExoPlayer
    private var positionCheckListPlay = 0
    private var positionRecordPlay = 0

    var idFolder = 0
    lateinit var  note: Note
    private val noteViewmodel: NoteViewmodel by lazy {
        ViewModelProvider(this, NoteViewmodel.NoteViewmodelFactory(requireActivity().application))[NoteViewmodel::class.java]
    }

    private val folderViewmodel: FolderViewmodel by lazy {
        ViewModelProvider(this, FolderViewmodel.NoteViewmodelFactory(requireActivity().application))[FolderViewmodel::class.java]
    }

    override fun onFragmentBackPressed() {
        if(Common.checkMain){
        Common.checkMain = false
        findNavController().popBackStack()
    }else{
        findNavController().popBackStack(R.id.listNoteFragment,false)
    }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ivBack.setPreventDoubleClick(300) {
            onFragmentBackPressed()
        }
        btnSave.setPreventDoubleClick(300){
            saveNote()
        }
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        tvTime.text = currentDate
        initRcv()
        initializePlayer(requireContext())
        bottom_nav.setOnNavigationItemSelectedListener(this)
        getData()

    }

    private fun saveNote() {
        if(arrCheckList.isNotEmpty()){
            if(Common.checkScreen){
                val currentDate: String =  SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                note.date = currentDate

                for (i in 0 until arrCheckList.size){
                    for (j in 0 until arrCheckList[i].images.size){
                        if ( arrCheckList[i].images[j].bitmap!=null){
                            arrCheckList[i].images[j].path = ImageUtil.saveToInternalStorage(requireContext(),arrCheckList[i].images[j])
                        }else if (arrCheckList[i].images[j].path !=null){
                            arrCheckList[i].images[j].path = ImageUtil.copyFile(requireContext(),arrCheckList[i].images[j].path!!,arrCheckList[i].images[j].id)
                        }
                   }
                }
                note.checkList = arrCheckList
                noteViewmodel.updateNote(note)

            }else{
                val currentDate: String =  SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                note.date = currentDate
                note.folderId = idFolder

                noteViewmodel.insertNote(note)
                noteViewmodel.getAllNote(idFolder).observe(requireActivity(), androidx.lifecycle.Observer {
                    folderViewmodel.updateFolderById(idFolder,it.size)
                })
            }

            onFragmentBackPressed()


        }else{
            AppUtil.showToast(context,R.string.please_input_content)
        }
    }

    private fun getData() {
        try {
            idFolder = requireArguments().getInt("id")
            note = Gson().fromJson(requireArguments().getString("note"),Note::class.java)
            arrCheckList.addAll(note.checkList)
        }catch (e:Exception){
            Log.e("TAG", "getData: $e" )
        }
    }

    private fun initRcv() {
        adapterCheckList = CheckListAdapter(arrCheckList, { position, text ->
            //onFocusText
            POSITION_FOCUS = position
        },
            {
                // Click Image
            },
            { positionItem, oldPositionPlay, positionPlay ->
                // Click record
                Log.e("TAG", "initRcv: ")
                positionCheckListPlay = positionItem
                positionRecordPlay = positionPlay
                if (arrCheckList[positionItem].audios[positionPlay].isPlay) {
                    stopSound()
                } else {
                    adapterCheckList.adapterRecord.currentPlay = 0
                    arrCheckList[positionItem].audios[oldPositionPlay].isPlay = false
                    arrCheckList[positionItem].audios[positionPlay].isPlay = true
                    playRecord(positionItem, oldPositionPlay, positionPlay)
                }
                adapterCheckList.adapterRecord.notifyItemChanged(oldPositionPlay)
                adapterCheckList.adapterRecord.notifyItemChanged(positionPlay)
            },
            {
                // delete item
                arrCheckList.removeAt(it)
                adapterCheckList.notifyDataSetChanged()
            },
            { positionCheckList, positionImage ->
                // onDeleteImage
                arrCheckList[positionCheckList].images.removeAt(positionImage)
                adapterCheckList.notifyItemChanged(positionCheckList)
            },
            { positionCheckList, positionRecord ->
                // onDeleteRecord
                arrCheckList[positionCheckList].audios.removeAt(positionRecord)
                adapterCheckList.notifyItemChanged(positionCheckList)

            })
        rcvCheckList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcvCheckList.adapter = adapterCheckList

    }

    private fun playRecord(
        positionItem: Int,
        oldPositionPlay: Int,
        positionPlay: Int
    ) {
        val url = arrCheckList[positionItem].audios[positionPlay].path
        if (!File(url).exists()) {
            Toast.makeText(context, "File doesn't exists", Toast.LENGTH_SHORT).show()
            return
        }
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
    }


    private val handler = Handler()
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            adapterCheckList.adapterRecord.currentPlay =
                player.currentPosition.toFloat().div(player.duration.toFloat()).times(100f).toInt()

            adapterCheckList.adapterRecord.notifyItemChanged(positionRecordPlay)
            handler.postDelayed(this, 200)
        }
    }


    override fun onResume() {
        super.onResume()
        if (isOnClick) {
            if (IS_CHOOSE == 1) {
                Log.e("TAG", "onResume: ${DeviceUtil.arrImage} ")
                if (DeviceUtil.arrImage.isNotEmpty()) {
                    if (arrCheckList.isEmpty()) {
                        addItemCheckList()
                    }
                    arrCheckList[POSITION_FOCUS].images.addAll(DeviceUtil.arrImage)
                    adapterCheckList.notifyItemChanged(POSITION_FOCUS)
                }
            } else if (IS_CHOOSE == 2) {
                if (arrCheckList.isEmpty()) {
                    addItemCheckList()
                }
            }
            isOnClick = false
        }
        if (isPause) {
            resumeSound()
        }
    }

    override fun onNavigationItemSelected(menu: MenuItem): Boolean {
        when (menu.itemId) {
            R.id.menu_add -> {
                addItemCheckList()
            }
            R.id.menu_image -> {
                IS_CHOOSE = 1
                startActivity(Intent(requireContext(), ChooseImageAct::class.java))
                isOnClick = true
            }
            R.id.menu_draw -> {
                startActivity(Intent(requireActivity(), DrawAct::class.java))
                IS_CHOOSE = 1
                isOnClick = true
            }

            R.id.menu_recording -> {

                val intent = Intent(context, AudioActivity::class.java)
                startActivityForResult(intent, AudioActivity.REQUEST_AUDIO)
// AudioActivity.startActivity(requireActivity())
                IS_CHOOSE = 3
                isOnClick = true
            }
        }
        return true
    }

    private fun addItemCheckList() {
        var idCheckList = 0
        if (arrCheckList.size > 0) {
            idCheckList = arrCheckList.size + 1
        }
        arrCheckList.add(CheckList(idCheckList))
        adapterCheckList.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        isPause = true
        pauseSound()
    }

    private fun pauseSound() {
        if (player.isPlaying) {
            player.pause()
        }
    }


    private fun resumeSound() {
        if (isPause) {
            player.play()
            isPause = false
        }
    }

    private fun stopSound() {
        player.stop()
        adapterCheckList.adapterRecord.currentPlay = 0
        arrCheckList[positionCheckListPlay].audios[positionRecordPlay].isPlay = false
        adapterCheckList.adapterRecord.notifyItemChanged(positionRecordPlay)
        handler.removeCallbacks(runnable)
    }

    private fun initializePlayer(context: Context) {
        val renderersFactory: RenderersFactory =
            DefaultRenderersFactory(context.applicationContext)
                .setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF)
        val mediaSourceFactory: MediaSourceFactory = DefaultMediaSourceFactory(context)
        player = SimpleExoPlayer.Builder(context, renderersFactory)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()
        player.addListener(object : Player.EventListener {

            override fun onPlayerError(error: ExoPlaybackException) {
                arrCheckList[positionCheckListPlay].audios[positionRecordPlay].isPlay = false
                adapterCheckList.adapterRecord.notifyItemChanged(positionRecordPlay)
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == ExoPlayer.STATE_ENDED) {
                    stopSound()
                } else if (playbackState == ExoPlayer.STATE_READY && playWhenReady) {
                    runnable.run()
                }
            }
        })
        player.setAudioAttributes(AudioAttributes.DEFAULT, true)
        player.playWhenReady = true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AudioActivity.REQUEST_AUDIO && resultCode == AppCompatActivity.RESULT_OK) {
            val requiredValue = data!!.getStringExtra(AudioActivity.KEY_PATH_AUDIO)
            if (arrCheckList.isEmpty()) {
                addItemCheckList()
            }
            if (!requiredValue.isNullOrBlank()) {
                arrCheckList[POSITION_FOCUS].audios.add(
                    RecordObj(
                        "record${System.currentTimeMillis() / 1000}",
                        requiredValue,
                        false
                    )
                )
                adapterCheckList.notifyItemChanged(POSITION_FOCUS)
            }
        }

    }
}