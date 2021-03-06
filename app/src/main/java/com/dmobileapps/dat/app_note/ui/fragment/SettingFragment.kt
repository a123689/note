package com.dmobileapps.dat.app_note.ui.fragment

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
//import com.adconfigonline.admob.ads.AdmobInterstitialTest
//import com.adconfigonline.admob.ads.AdmobInterstitialTest
import com.dmobileapps.dat.app_note.R
import com.dmobileapps.dat.app_note.utils.Common
import com.dmobileapps.dat.app_note.utils.setPreventDoubleClick
import com.test.dialognew.DialogLib
import com.test.dialognew.DialogNewInterface
import com.test.dialognew.RateCallback
import kotlinx.android.synthetic.main.fragment_setting.*


class SettingFragment : BaseFragment(R.layout.fragment_setting) {
    override fun onFragmentBackPressed() {
        findNavController().popBackStack()
    }

    private lateinit var sharedPreference: SharedPreferences
    lateinit var navController: NavController
    val editor by lazy {
        sharedPreference.edit()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = findNavController()
        sharedPreference = activity?.getSharedPreferences("NOTE", Context.MODE_PRIVATE)!!

        if (sharedPreference.getBoolean("interface", false)) {
            interfaceBlack()
        }

        ivBack.setPreventDoubleClick(300) {
            onFragmentBackPressed()
        }
        layoutRating.setPreventDoubleClick(300) {
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
                    editor.putBoolean("rate",true)
                    editor.apply()

                }

                override fun onFB(choice: Int) {

                    editor.putBoolean("rate",true)
                    editor.apply()
                }

                override fun onCancel() {

                }

                override fun onCancelFb() {

                }

            }, object : RateCallback {
                override fun onFBShow() {
                    editor.putBoolean("rate",true)

                }

            })

        }
        layoutFeedback.setPreventDoubleClick(300) {
            sendEmailMoree(
                requireContext(),
                arrayOf("khoanglang270102@gmail.com"),
                "Feedback to Note",
                "",
            )
        }

        tvSwich.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked) {
                    editor.putBoolean("interface", true)
                    showAdsBack()
                    tvSwich.isChecked = true

                } else {
                    showAdsBack()
                    editor.putBoolean("interface", false)
                    tvSwich.isChecked = false

                }
                editor.apply()


            }

        })

        layoutPolicy.setPreventDoubleClick(300) {
            if (navController.currentDestination?.id == R.id.settingFragment) {
                navController.navigate(R.id.action_settingFragment_to_policyFragment)
            }
        }

    }



    private fun showAdsBack() {
        if (tvSwich != null) {
            if (tvSwich.isChecked) {
                interfaceBlack()
            } else {
                interfaceWhite()
            }
        }

    }

    private fun interfaceBlack() {
        layout_setting.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorBlack
            )
        )
        ivRate.setImageResource(R.drawable.ic_rate_white)
        ivFeedback.setImageResource(R.drawable.ic_feedback_white)
        ivVersion.setImageResource(R.drawable.ic_version_white)
        ivNight.setImageResource(R.drawable.ic_night_white)
        ivPolicy.setImageResource(R.drawable.ic_policy_white)
        tvRating.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvFeedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvInterface.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvVersion.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        tvPolicy.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
        ivMore1.setImageResource(R.drawable.ic_more_white)
        ivMore2.setImageResource(R.drawable.ic_more_white)
        ivMore3.setImageResource(R.drawable.ic_more_white)
        view1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
        tvSwich.isChecked = true
        toolBar.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorToolbarBlack
            )
        )
    }

    private fun interfaceWhite() {
        layout_setting.setBackgroundColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorWhite
            )
        )
        ivRate.setImageResource(R.drawable.ic_baseline_rate_review_24)
        ivFeedback.setImageResource(R.drawable.ic_baseline_feedback_24)
        ivVersion.setImageResource(R.drawable.ic_version)
        ivNight.setImageResource(R.drawable.ic_night)
        ivPolicy.setImageResource(R.drawable.ic_baseline_policy_24)
        tvRating.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
        tvFeedback.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
        tvInterface.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
        tvVersion.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
        tvPolicy.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack))
        ivMore1.setImageResource(R.drawable.ic_more_white)
        ivMore2.setImageResource(R.drawable.ic_more_white)
        ivMore3.setImageResource(R.drawable.ic_more_white)
        view1.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorView))
        toolBar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorWhite))
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