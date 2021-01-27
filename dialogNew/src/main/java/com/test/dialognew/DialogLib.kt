package com.test.dialognew

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.*
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_feedback_useful.view.*
import kotlinx.android.synthetic.main.dialog_rate.view.*
import java.util.*

class DialogLib {
    private var dialogNewInterface: DialogNewInterface? = null

    companion object {
        private const val EMAIL_FEEDBACK = "feedback.govo@gmail.com"
        private var instance: DialogLib? = null

        fun getInstance(): DialogLib {
            if (instance == null)
                instance = DialogLib()
            return instance!!
        }
    }

    fun openMarket2(
        context: Context,
        packageName: String
    ) {
        val i =
            Intent(Intent.ACTION_VIEW)
        try {
            i.data = Uri.parse("market://details?id=$packageName")
            context.startActivity(i)
        } catch (ex: ActivityNotFoundException) {
//                openBrowser(
//                    context,
//                    com.volio.textonphoto.PhotorTool.BASE_GOOGLE_PLAY + packageName
//                )
        }
    }


    private fun openMarket(context: Context, packageName: String) {
        val uri = Uri.parse("market://details?id=" + packageName)
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            context.startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)
                )
            )
        }
    }


    private fun animScale(view: View, context: Context) {
        val animationScale1 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale1)
        val animationScale2 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale2)
        val animationScale3 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale3)
        val animationScale4 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale4)
        val animationScale5 =
            AnimationUtils.loadAnimation(context, R.anim.anim_scale5)

        view.ivStar1.startAnimation(animationScale1)
        view.ivStar2.startAnimation(animationScale2)
        view.ivStar3.startAnimation(animationScale3)
        view.ivStar4.startAnimation(animationScale4)
        view.ivStar5.startAnimation(animationScale5)

        view.ivStar1.setImageResource(R.drawable.ic_star_up2)
        view.ivStar2.setImageResource(R.drawable.ic_star_up2)
        view.ivStar3.setImageResource(R.drawable.ic_star_up2)
        view.ivStar4.setImageResource(R.drawable.ic_star_up2)
        view.ivStar5.setImageResource(R.drawable.ic_star_up2)
    }

    private fun animAlpha(view: View, context: Context) {
        val animationAlpha1 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha1)
        val animationAlpha2 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha2)
        val animationAlpha3 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha3)
        val animationAlpha4 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha4)
        val animationAlpha5 =
            AnimationUtils.loadAnimation(context, R.anim.anim_alpha5)

        view.ivWave1.visibility = View.VISIBLE
        view.ivWave2.visibility = View.VISIBLE
        view.ivWave3.visibility = View.VISIBLE
        view.ivWave4.visibility = View.VISIBLE
        view.ivWave5.visibility = View.VISIBLE

        view.ivWave1.startAnimation(animationAlpha1)
        view.ivWave2.startAnimation(animationAlpha2)
        view.ivWave3.startAnimation(animationAlpha3)
        view.ivWave4.startAnimation(animationAlpha4)
        view.ivWave5.startAnimation(animationAlpha5)
    }

    private fun resetStar(view: View) {
        view.ivStar1.setImageResource(R.drawable.ic_un_star_up)
        view.ivStar2.setImageResource(R.drawable.ic_un_star_up)
        view.ivStar3.setImageResource(R.drawable.ic_un_star_up)
        view.ivStar4.setImageResource(R.drawable.ic_un_star_up)
        view.ivStar5.setImageResource(R.drawable.ic_un_star_up)
    }

    private fun isEnable(view: View, isEnable: Boolean) {
        if (isEnable) {
            view.ivStar1.isEnabled = true
            view.ivStar2.isEnabled = true
            view.ivStar3.isEnabled = true
            view.ivStar4.isEnabled = true
            view.ivStar5.isEnabled = true
        } else {
            view.ivStar1.isEnabled = false
            view.ivStar2.isEnabled = false
            view.ivStar3.isEnabled = false
            view.ivStar4.isEnabled = false
            view.ivStar5.isEnabled = false
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


    private var rateCallback: RateCallback? = null

    fun showDialogRate(
        context: Context?,
        dialogNewInterface1: DialogNewInterface,
        rateCallback1: RateCallback
    ) {
        if (context == null) return

        rateCallback = rateCallback1
        this.dialogNewInterface = dialogNewInterface1
        val animation = AnimationUtils.loadAnimation(context, R.anim.anim_rate)

        var numRate = 0
        val dialog: Dialog
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.dialog_rate,
            null
        )
        val builder = android.app.AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!dialog.isShowing) {
            dialog.show()
            isEnable(view, false)
            animScale(view, context)
            animAlpha(view, context)

            Handler(Looper.getMainLooper()).postDelayed({
                resetStar(view)
            }, 1700)

            Handler(Looper.getMainLooper()).postDelayed({
                resetStar(view)
                isEnable(view, true)
            }, 4200)
        }
        Glide.with(view).load(R.drawable.ic_frame0).into(view.ivImage)

        view.ivStar1.setPreventDoubleClickScaleView(300) {
            view.btnRate.showIfInv()
            view.btnRate.text = context.getString(R.string.rate)
            numRate = 1
            RateUtil.star1(view)
            view.imThbest.visibility = View.VISIBLE
            view.img.visibility = View.VISIBLE
            view.tvContents.text = context.getString(R.string.please_leave_us_some_feed_back)
            view.ivImage.startAnimation(animation)
            Glide.with(view).load(R.drawable.ic_frame1).into(view.ivImage)
        }
        view.ivStar2.setPreventDoubleClickScaleView(300) {
            view.btnRate.showIfInv()
            numRate = 2
            view.btnRate.text = context.getString(R.string.rate)
            RateUtil.star2(view)
            view.imThbest.visibility = View.VISIBLE
            view.img.visibility = View.VISIBLE
            view.tvContents.text = context.getString(R.string.please_leave_us_some_feed_back)
            view.ivImage.startAnimation(animation)
            Glide.with(view).load(R.drawable.ic_frame2).into(view.ivImage)
        }
        view.ivStar3.setPreventDoubleClickScaleView(300) {
            view.btnRate.showIfInv()
            numRate = 3
            view.btnRate.text = context.getString(R.string.rate)
            RateUtil.star3(view)
            view.imThbest.visibility = View.VISIBLE
            view.img.visibility = View.VISIBLE
            view.tvContents.text = context.getString(R.string.please_leave_us_some_feed_back)
            view.ivImage.startAnimation(animation)
            Glide.with(view).load(R.drawable.ic_frame3).into(view.ivImage)

        }
        view.ivStar4.setPreventDoubleClickScaleView(300) {
            view.btnRate.showIfInv()
            numRate = 4
            view.btnRate.text = context.getString(R.string.rate)
            RateUtil.star4(view)
            view.imThbest.visibility = View.VISIBLE
            view.img.visibility = View.VISIBLE
            view.tvContents.text = context.getString(R.string.we_like_you_too_thanks_for_your_feed_back)
            view.ivImage.startAnimation(animation)
            Glide.with(view).load(R.drawable.ic_frame4).into(view.ivImage)
        }
        view.ivStar5.setPreventDoubleClickScaleView(300) {
            view.btnRate.showIfInv()
            numRate = 5
            view.btnRate.text = context.getString(R.string.rate_gp)
            RateUtil.star5(view)
//            view.imThbest.visibility = View.INVISIBLE
//            view.img.visibility = View.INVISIBLE
            view.tvContents.text = context.getString(R.string.we_like_you_too_thanks_for_your_feed_back)
            view.ivImage.startAnimation(animation)
            Glide.with(view).load(R.drawable.fram5).into(view.ivImage)
        }

        view.ivCancel.setPreventDoubleClickScaleView(300) {
            dialog.dismiss()
            dialogNewInterface?.onCancel()
        }

        view.btnRate.setPreventDoubleClickScaleView(300) {
            if (numRate == 0) {
                Toast.makeText(context, "You must choose star", Toast.LENGTH_SHORT).show()
                return@setPreventDoubleClickScaleView
            }
            dialog.dismiss()
            dialogNewInterface?.onRate(numRate)
//            if (numRate > 4) {
//                dialog.dismiss()
//                openMarket2(context, context.packageName)
//
//            } else {
//                dialog.dismiss()
//                rateCallback?.onFBShow()
//                //showDialogFeedBack(context, null)
//            }

        }
    }

    private fun showDialogFeedBack(
        context: Context?,
        callback: FeedbackCallback?
    ) {
        if (context == null) return

        var choice = 0

        val dialog: Dialog
        var text = ""
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.dialog_feedback_useful,
            null
        )
        val builder = android.app.AlertDialog.Builder(context)
        builder.setView(view)
        dialog = builder.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        dialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if (!dialog.isShowing) {
            dialog.show()
        }

        view.btnSubmit.setPreventDoubleClickScaleView(300) {
            if (view.rbOther.isChecked) {
                text = view.edFeedback.text.toString().trim()
            }

            if (text.isEmpty()) {
                Toast.makeText(context, "Please enter feedback", Toast.LENGTH_SHORT).show()
                return@setPreventDoubleClickScaleView
            }
//                PrefUtil.setFeedback(context)
            dialog.dismiss()
            var version = ""
            try {
                val pInfo: PackageInfo =
                    Objects.requireNonNull<Context>(context).packageManager.getPackageInfo(
                        context.packageName,
                        0
                    )
                version = pInfo.versionName

            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            val title = "Feedback to ${context.getString(R.string.app_name)} - $version"
           // sendEmailMoree(context, arrayOf(EMAIL_FEEDBACK), title, text)
          //  dialogNewInterface?.onFB(choice)
            callback?.onFeedback()
        }
        view.ivCancel2.setPreventDoubleClickScaleView(300) {
            dialog.dismiss()
            dialogNewInterface?.onCancelFb()
        }
        view.radioGroup.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                when (checkedId) {
                    R.id.rbFeedback1 -> {
                        view.edFeedback.visibility = View.GONE
                        text = view.rbFeedback1.text.toString()
                        choice = 1
                    }
                    R.id.rbFeedback2 -> {
                        view.edFeedback.visibility = View.GONE
                        text = view.rbFeedback2.text.toString()
                        choice = 2
                    }
                    R.id.rbFeedback3 -> {
                        view.edFeedback.visibility = View.GONE
                        text = view.rbFeedback3.text.toString()
                        choice = 3
                    }
                    R.id.rbFeedback4 -> {
                        view.edFeedback.visibility = View.GONE
                        text = view.rbFeedback4.text.toString()
                        choice = 4
                    }
                    R.id.rbFeedback5 -> {
                        view.edFeedback.visibility = View.GONE
                        text = view.rbFeedback5.text.toString()
                        choice = 5
                    }

                    else -> {
                        view.edFeedback.visibility = View.VISIBLE
                        choice = 6
                    }
                }
            }

        })

    }

    fun sendEmailMoree(
        context: Context,
        addresses: Array<String>,
        subject: String,
        body: String
    ) {

        disableExposure()
        val emailSelectorIntent = Intent(Intent.ACTION_SENDTO)
        emailSelectorIntent.data = Uri.parse("mailto:")

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE)
        // intent.type = "message/rfc822"
        // intent.data = Uri.parse("mailto:") // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses)
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
//            if (uris.size > 0)
//                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)


        intent.putExtra(
            Intent.EXTRA_TEXT, body + "\n\n\n" +
                    "DEVICE INFORMATION (Device information is useful for application improvement and development)"
                    + "\n\n" + getDeviceInfo()
        )
        intent.selector = emailSelectorIntent

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
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getDeviceInfo(): String {
        val densityText = when (Resources.getSystem().displayMetrics.densityDpi) {
            DisplayMetrics.DENSITY_LOW -> "LDPI"
            DisplayMetrics.DENSITY_MEDIUM -> "MDPI"
            DisplayMetrics.DENSITY_HIGH -> "HDPI"
            DisplayMetrics.DENSITY_XHIGH -> "XHDPI"
            DisplayMetrics.DENSITY_XXHIGH -> "XXHDPI"
            DisplayMetrics.DENSITY_XXXHIGH -> "XXXHDPI"
            else -> "HDPI"
        }

        var megAvailable = 0L

//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        val stat = StatFs(Environment.getExternalStorageDirectory().path)
        val bytesAvailable: Long
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bytesAvailable = stat.blockSizeLong * stat.availableBlocksLong
            megAvailable = bytesAvailable / (1024 * 1024)
        }
//        }

        return "Manufacturer ${Build.MANUFACTURER}, Model ${Build.MODEL}," +
                " ${Locale.getDefault()}, " +
                "osVer ${Build.VERSION.RELEASE}, Screen ${Resources.getSystem().displayMetrics.widthPixels}x${Resources.getSystem().displayMetrics.heightPixels}, " +
                "$densityText, Free space ${megAvailable}MB, TimeZone ${
                    TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
                }"
    }
}