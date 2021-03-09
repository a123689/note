package com.dmobileapps.dat.app_note.ui.fragment.checkList

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
import com.dmobileapps.dat.app_note.ui.fragment.checkList.adapter.AdapterRecord
import com.dmobileapps.dat.app_note.ui.fragment.chooseImage.ChooseImageAct
import com.dmobileapps.dat.app_note.ui.fragment.viewImage.ViewImageAct
import com.dmobileapps.dat.app_note.utils.*
import com.dmobileapps.dat.app_note.viewmodel.FolderViewmodel
import com.dmobileapps.dat.app_note.viewmodel.NoteViewmodel
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.MediaSourceFactory
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.test.dialognew.DialogLib
import com.test.dialognew.DialogNewInterface
import com.test.dialognew.RateCallback
import kotlinx.android.synthetic.main.fragment_check_list.*
import nv.module.audiorecoder.ui.AudioActivity
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "CheckListFragment"

class CheckListFragment : BaseFragment(R.layout.fragment_check_list),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private val arrCheckList: ArrayList<CheckList> = ArrayList()
    private lateinit var adapterCheckList: CheckListAdapter
    private var IS_CHOOSE = 0
    private var POSITION_FOCUS = -1
    private var isOnClick = false
    private var isPause = false

    var arrDelete: ArrayList<String> = ArrayList()

    private lateinit var player: SimpleExoPlayer
    private var positionCheckListPlay = 0
    private var positionRecordPlay = 0
    private var avatarNote = ""

    var idFolder = 0
    lateinit var note: Note
    private val noteViewmodel: NoteViewmodel by lazy {
        ViewModelProvider(
            this,
            NoteViewmodel.NoteViewmodelFactory(requireActivity().application)
        )[NoteViewmodel::class.java]
    }

    private val folderViewModel: FolderViewmodel by lazy {
        ViewModelProvider(
            this,
            FolderViewmodel.NoteViewmodelFactory(requireActivity().application)
        )[FolderViewmodel::class.java]
    }
    val editor by lazy {
        sharedPreference.edit()
    }
    private lateinit var sharedPreference: SharedPreferences

    override fun onFragmentBackPressed() {
        if (Common.checkMain) {
            Common.checkMain = false
            findNavController().popBackStack()
        } else {
            findNavController().popBackStack(R.id.listNoteFragment, false)
        }
    }
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This callback will only be called when MyFragment is at least Started.
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    if (mInterstitialAd.isLoaded) {
                        mInterstitialAd.show()
                    } else {
                        onFragmentBackPressed()
                    }

                    mInterstitialAd.adListener = object: AdListener() {
                        override fun onAdLoaded() {
                            // dialog.dismiss()
                        }

                        override fun onAdFailedToLoad(errorCode: Int) {
                            onFragmentBackPressed()
                        }

                        override fun onAdOpened() {
                            onFragmentBackPressed()
                        }

                        override fun onAdClicked() {
                            // Code to be executed when the user clicks on an ad.
                        }

                        override fun onAdLeftApplication() {
                            // Code to be executed when the user has left the app.
                        }

                        override fun onAdClosed() {
                            // Code to be executed when the interstitial ad is closed.
                        }
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    private lateinit var mInterstitialAd: InterstitialAd
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd.adUnitId = "ca-app-pub-4040515803655174/6031715796"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        sharedPreference = activity?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!

        if (Common.checkInterface) {
            constrainCheckList.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlack
                )
            )
            toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
            bottom_nav.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.color_black
                )
            )
            val iconsColorStates = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ), intArrayOf(
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#FFFFFF")
                )
            )

            val textColorStates = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ), intArrayOf(
                    Color.parseColor("#FFFFFF"),
                    Color.parseColor("#FFFFFF")
                )
            )
            bottom_nav.itemTextColor = textColorStates
            bottom_nav.itemIconTintList = iconsColorStates
        } else {
            constrainCheckList.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )
            toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
            bottom_nav.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorWhite
                )
            )

            val iconsColorStates = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ), intArrayOf(
                    Color.parseColor("#020202"),
                    Color.parseColor("#020202")
                )
            )

            val textColorStates = ColorStateList(
                arrayOf(
                    intArrayOf(-android.R.attr.state_checked),
                    intArrayOf(android.R.attr.state_checked)
                ), intArrayOf(
                    Color.parseColor("#020202"),
                    Color.parseColor("#020202")
                )
            )
            bottom_nav.itemTextColor = textColorStates
            bottom_nav.itemIconTintList = iconsColorStates
        }

        ivBack.setPreventDoubleClick(300) {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                onFragmentBackPressed()
            }

            mInterstitialAd.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    // dialog.dismiss()
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    onFragmentBackPressed()
                }

                override fun onAdOpened() {
                    onFragmentBackPressed()
                }

                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    // Code to be executed when the interstitial ad is closed.
                }
            }
        }
        btnSave.setPreventDoubleClick(1000) {
            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                saveNote()
               // showDialogRate()
            }
            mInterstitialAd.adListener = object: AdListener() {
                override fun onAdLoaded() {
                    // dialog.dismiss()
                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    saveNote()
                   // showDialogRate()
                }

                override fun onAdOpened() {
                    saveNote()
                   // showDialogRate()
                }

                override fun onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                override fun onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                override fun onAdClosed() {
                    // Code to be executed when the interstitial ad is closed.
                }
            }

        }
        val currentDate: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
        tvTime.text = currentDate
        initRcv()
        initializePlayer(requireContext())
        bottom_nav.setOnNavigationItemSelectedListener(this)
        getData()

    }
    private fun openMarket(context: Context, packageName: String) {
        val i = Intent(Intent.ACTION_VIEW)
        try {
            i.data = Uri.parse("market://details?id=$packageName")
            context.startActivity(i)
        } catch (ex: ActivityNotFoundException) {
            openBrowser(
                context,
                "https://play.google.com/store/apps/details?id=\"" + packageName
            )
        }
    }

    private fun showDialogRate() {
        val isShowRateInSession = Common.IS_SHOW_RATE_IN_SESSION
        val isRate = sharedPreference.getBoolean("rate", false)

        if (isRate) {
            //do nothing
        } else if (!isRate && !isShowRateInSession) {
            //show dialog rate

            Common.IS_SHOW_RATE_IN_SESSION = true
            DialogLib.getInstance().showDialogRate(context, object : DialogNewInterface {
                override fun onRate(rate: Int) {
                    if(rate < 4){
                        sendEmailMoree(
                            requireContext(),
                            arrayOf("khoanglang270102@gmail.com"),
                            "Feedback to Note",
                            "",
                        )
                    }else{
                        openMarket(requireContext(), requireActivity().packageName)
                    }
                    editor.putBoolean("rate", true)
                    editor.apply()
                }

                override fun onFB(choice: Int) {

                    editor.putBoolean("rate", true)
                    editor.apply()
                }

                override fun onCancel() {

                }

                override fun onCancelFb() {

                }

            }, object : RateCallback {
                override fun onFBShow() {
                    editor.putBoolean("rate", true)
                    editor.apply()

                }

            })
        }

    }

    private fun openBrowser(context: Context, url: String) {
        var url = url
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://$url"
        }
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            context.startActivity(browserIntent)
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }

    private fun sendEmailMoree(
        context: Context,
        mail: Array<String>,
        subject: String,
        body: String
    ) {
        disableExposure()
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, mail)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        } else {
            Toast.makeText(context, "you need install gmail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun disableExposure() {
        if (Build.VERSION.SDK_INT >= 24) {
            try {
                val m = StrictMode::class.java.getMethod("disableDeathOnFileUriExposure")
                m.invoke(null)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun saveNote() {
        if (arrCheckList.isNotEmpty()) {
            if (Common.checkScreen) {
                moveImageToInternal()
                val currentDate: String =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                if (edtTbTitle.text != null) {
                    note.content = edtTbTitle.text.toString()
                }
                note.date = currentDate
                note.checkList = arrCheckList
                note.avatar = avatarNote
                noteViewmodel.updateNote(note)

            } else {
                val currentDate: String =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val newNote = Note()
                moveImageToInternal()
                if (edtTbTitle.text != null) {
                    newNote.content = edtTbTitle.text.toString()
                }
                newNote.date = currentDate
                newNote.folderId = idFolder
                newNote.checkList = arrCheckList
                newNote.avatar = avatarNote
                noteViewmodel.insertNote(newNote)

                noteViewmodel.getAllNote(idFolder)
                    .observe(requireActivity(), androidx.lifecycle.Observer {
                        folderViewModel.updateFolderById(idFolder, it.size)
                    })


            }
            if (!arrDelete.isNullOrEmpty()) {
                for (path in arrDelete) {
                    Log.e(TAG, "$path delete: ${AppUtil.deleteFileFromInternalStorage(path)}")
                }
            }

            AppUtil.hideKeyboard(requireActivity())
            onFragmentBackPressed()
        } else {
            AppUtil.showToast(context, R.string.please_input_content)
        }
    }

    private fun moveImageToInternal() {
        if (!arrCheckList.isNullOrEmpty()) {
            for (i in 0 until arrCheckList.size) {
                if (!arrCheckList[i].images.isNullOrEmpty()) {
                    for (j in 0 until arrCheckList[i].images.size) {
                        if (arrCheckList[i].images[j].bitmap != null) {
                            arrCheckList[i].images[j].path = ImageUtil.saveToInternalStorage(
                                requireContext(),
                                arrCheckList[i].images[j]
                            )
                            arrCheckList[i].images[j].bitmap = null
                        } else if (arrCheckList[i].images[j].path != null && !arrCheckList[i].images[j].path!!.contains(
                                "imageDir"
                            )
                        ) {
                            arrCheckList[i].images[j].path = ImageUtil.copyFileToInternal(
                                requireContext(),
                                arrCheckList[i].images[j].path!!,
                                arrCheckList[i].images[j].id
                            )
                        }
                        avatarNote = arrCheckList[i].images[j].path!!
                    }
                }
            }
        }
    }

    private fun getData() {
        try {
            idFolder = requireArguments().getInt("id")
            val noteString = requireArguments().getString("note")
            if (!noteString.isNullOrBlank()) {
                note = Gson().fromJson(noteString, Note::class.java)
            }
            if (note != null && !note.checkList.isNullOrEmpty()) {
                arrCheckList.addAll(note.checkList)
                if (!note.content.isNullOrBlank())
                    edtTbTitle.setText(note.content!!)
            }

        } catch (e: Exception) {
            Log.e("TAG", "getData: $e")
        }
    }

    private fun initRcv() {
        adapterCheckList = CheckListAdapter(arrCheckList,
            { position, text ->
                //onFocusText
                POSITION_FOCUS = position
                Log.e("TAG", " text change:$POSITION_FOCUS : $text ")
            },
            {
                // text change
                    position, text ->
                arrCheckList[position].title = text
            },
            { position, positionImage ->
                // Click Image
                val intent = Intent(requireContext(), ViewImageAct::class.java)
                intent.putExtra("arrImage", Gson().toJson(arrCheckList[position].images))
                intent.putExtra("position", positionImage)
                startActivity(intent)

            },
            { positionItem, oldPositionPlay, positionPlay ->
                // Click record
                positionCheckListPlay = positionItem
                positionRecordPlay = positionPlay
                if (arrCheckList[positionItem].audios[positionPlay].isPlay) {
                    stopSound()
                } else {
//                    adapterCheckList.adapterRecord.currentPlay = 0
                    adapterCheckList.adapterRecord.currentPlay = 0
                    arrCheckList[positionItem].audios[oldPositionPlay].isPlay = false
                    arrCheckList[positionItem].audios[positionPlay].isPlay = true
                    playRecord(positionItem, positionPlay)
                }
                adapterCheckList.adapterRecord.notifyItemChanged(oldPositionPlay,
                    AdapterRecord.Information()
                )
                adapterCheckList.adapterRecord.notifyItemChanged(positionPlay,
                    AdapterRecord.Information()
                )
            },
            {
                // delete item
var delete = 0
                if (it<arrCheckList.size){
                    delete =it
                }
                if (it==arrCheckList.size){
                    delete =0
                }


                val checklist = arrCheckList[delete]
                if (!checklist.audios.isNullOrEmpty()) {
                    for (audios in checklist.audios) {
                        arrDelete.add(audios.path)
                    }
                }
                if (!checklist.images.isNullOrEmpty()) {
                    for (image in checklist.images) {
                        if (image.path != null && image.path!!.contains("imageDir")) {
                            arrDelete.add(image.path!!)
                        }
                    }
                }
                arrCheckList.removeAt(delete)
                adapterCheckList.notifyItemRemoved(delete)
//                adapterCheckList.notifyItemRangeChanged(it, arrCheckList.size)
            },
            { positionCheckList, positionImage ->
                // onDeleteImage
                val img = arrCheckList[positionCheckList].images[positionImage]
                if (img.path != null && img.path!!.contains("imageDir")) {
                    arrDelete.add(img.path!!)
                }
                arrCheckList[positionCheckList].images.removeAt(positionImage)
                adapterCheckList.notifyItemChanged(positionCheckList)
            },
            { positionCheckList, positionRecord ->
                // onDeleteRecord
                val record = arrCheckList[positionCheckList].audios[positionRecord]
                arrDelete.add(record.path)
                arrCheckList[positionCheckList].audios.removeAt(positionRecord)
                adapterCheckList.notifyItemChanged(positionCheckList)

            })
        rcvCheckList.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rcvCheckList.adapter = adapterCheckList

    }

    private fun playRecord(
        positionItem: Int,
        positionPlay: Int
    ) {
        val url = arrCheckList[positionItem].audios[positionPlay].path
        if (!File(url).exists()) {
            Toast.makeText(context, R.string.file_not_exit, Toast.LENGTH_SHORT).show()
            return
        }
        player.setMediaItem(MediaItem.fromUri(url))
        player.prepare()
    }


    private val handler = Handler()
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            adapterCheckList.adapterRecord.currentPlay =  player.currentPosition.toFloat().div(player.duration.toFloat()).times(100f).toInt()
            adapterCheckList.adapterRecord.notifyItemChanged(positionRecordPlay,
                AdapterRecord.Information()
            )
            handler.postDelayed(this, 200)
        }
    }


    override fun onResume() {
        super.onResume()
        if (isOnClick) {
            if (POSITION_FOCUS == -1) {
                POSITION_FOCUS = 0
            }
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
                AudioActivity.checkInterface = Common.checkInterface
                val intent = Intent(context, AudioActivity::class.java)
                startActivityForResult(intent, AudioActivity.REQUEST_AUDIO)
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
        Log.e(TAG, "addItemCheckList: $POSITION_FOCUS")
        if (POSITION_FOCUS != -1) {
            if (arrCheckList.size == 0) {
                arrCheckList.add(0, CheckList(id = idCheckList))
                adapterCheckList.positionFocus = 0
            } else {
                arrCheckList.add(POSITION_FOCUS + 1, CheckList(id = idCheckList))
                adapterCheckList.positionFocus = POSITION_FOCUS + 1
            }
            adapterCheckList.notifyItemInserted(POSITION_FOCUS + 1)
        } else {
            arrCheckList.add(arrCheckList.size, CheckList(id = idCheckList))
            adapterCheckList.positionFocus = arrCheckList.size - 1
            adapterCheckList.notifyItemInserted(arrCheckList.size)
            rcvCheckList.scrollToPosition(arrCheckList.size - 1)
        }
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
//        adapterCheckList.adapterRecord.currentPlay = 0
        arrCheckList[positionCheckListPlay].audios[positionRecordPlay].isPlay = false
        adapterCheckList.adapterRecord.notifyItemChanged(positionRecordPlay,
            AdapterRecord.Information()
        )
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
                adapterCheckList.adapterRecord.notifyItemChanged(positionRecordPlay,
                    AdapterRecord.Information()
                )
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
                if (POSITION_FOCUS == -1) {
                    POSITION_FOCUS = 0
                }
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