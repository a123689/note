package com.dmobileapps.dat.app_note.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.Nullable
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.model.Note
import com.dmobileapps.dat.app_note.utils.Common
import com.dmobileapps.dat.app_note.utils.hideKeyboard
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.dmobileapps.dat.app_note.viewmodel.FolderViewmodel
import com.dmobileapps.dat.app_note.viewmodel.NoteViewmodel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.gson.Gson
import com.test.dialognew.DialogLib
import com.test.dialognew.DialogNewInterface
import com.test.dialognew.RateCallback
import kotlinx.android.synthetic.main.fragment_write_note.*
import java.text.SimpleDateFormat
import java.util.*


class WriteNoteFragment : BaseFragment(R.layout.fragment_write_note) {
    override fun onFragmentBackPressed() {
        edContent.clearFocus()
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
                        view?.hideKeyboard()
                        onFragmentBackPressed()
                    }
                    mInterstitialAd.adListener = object: AdListener() {
                        override fun onAdLoaded() {

                        }

                        override fun onAdFailedToLoad(errorCode: Int) {
                            view?.hideKeyboard()
                            onFragmentBackPressed()
                        }

                        override fun onAdOpened() {
                            view?.hideKeyboard()
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


    }

    var idFolder = 0
    val editor by lazy {
        sharedPreference.edit()
    }
    private lateinit var sharedPreference: SharedPreferences
    lateinit var note: Note
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
    private lateinit var mInterstitialAd: InterstitialAd
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd.adUnitId = "ca-app-pub-4040515803655174/2797309680"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        sharedPreference = activity?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!
        if (Common.checkInterface) {
            layoutWrite.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.colorBlack
                )
            )
            edContent.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        } else {
            edContent.setTextColor(ContextCompat.getColor(requireActivity(), R.color.colorBlack))
            Glide.with(requireActivity()).load(R.drawable.background_write).into(ivBackground)
        }


        try {
            idFolder = requireArguments().getInt("id")
            note = Gson().fromJson(requireArguments().getString("note"), Note::class.java)
            edContent?.setText(note.content.toString())

        } catch (e: Exception) {

        }


        tvDone.setPreventDoubleClick(300) {

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {

                doneNote()
            }
            mInterstitialAd.adListener = object: AdListener() {
                override fun onAdLoaded() {
                   // dialog.dismiss()
                }

                override fun onAdFailedToLoad(errorCode: Int) {

                    doneNote()
                }

                override fun onAdOpened() {

                    doneNote()
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

        ivBack.setPreventDoubleClick(300) {

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                view.hideKeyboard()
                onFragmentBackPressed()
            }
            mInterstitialAd.adListener = object: AdListener() {
                override fun onAdLoaded() {

                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    view.hideKeyboard()
                    onFragmentBackPressed()
                }

                override fun onAdOpened() {
                    view.hideKeyboard()
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

        tvFolder.setPreventDoubleClick(300) {

            if (mInterstitialAd.isLoaded) {
                mInterstitialAd.show()
            } else {
                view.hideKeyboard()
                onFragmentBackPressed()
            }
            mInterstitialAd.adListener = object: AdListener() {
                override fun onAdLoaded() {

                }

                override fun onAdFailedToLoad(errorCode: Int) {
                    view.hideKeyboard()
                    onFragmentBackPressed()
                }

                override fun onAdOpened() {
                    view.hideKeyboard()
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

    private fun doneNote(){
        if (edContent.text.toString().isNotEmpty()) {
            edContent.clearFocus()
            view?.hideKeyboard()
            if (Common.checkScreen) {
                Log.d("dat123", "zxc")
                val currentDate: String =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                note.date = currentDate
                note.content = edContent.text.toString()
                noteViewmodel.updateNote(note)

            } else {
                val currentDate: String =
                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
                val note = Note(
                    content = edContent.text.toString(),
                    date = currentDate,
                    folderId = idFolder
                )
                noteViewmodel.insertNote(note)
                noteViewmodel.getAllNote(idFolder)
                    .observe(requireActivity(), androidx.lifecycle.Observer {
                        folderViewmodel.updateFolderById(idFolder, it.size)
                    })
            }
            onFragmentBackPressed()
            //showDialogRate()

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
}