package com.dmobileapps.dat.app_note.utils

import android.app.Activity
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Environment
import android.os.SystemClock
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import com.dmobileapps.dat.app_note.R
import java.io.File
import java.time.YearMonth
import java.util.*
import java.util.regex.Pattern

object AppUtil {
    private var mLastClickTime: Long = 0
    fun getWidthScreen(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }


    fun getHeightScreen(context: Activity): Int {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    val currentTime: String
        get() = Calendar.getInstance().time.toString() + ""

    fun clickOneSecond(): Boolean {
        if (SystemClock.elapsedRealtime() - mLastClickTime >= 1000) {
            mLastClickTime = SystemClock.elapsedRealtime()
            return true
        }
        return false
    }

    fun showToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun logE(TAG: String?, message: String?) {
        Log.e(TAG, message!!)
    }

    fun showToast(context: Context?, message: Int) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun numberCart(count: Int): String {
        return if (count < 99) {
            count.toString() + ""
        } else {
            99.toString() + "+"
        }
    }

    fun isStrValid(text: String?): Boolean {
        return text != null && !text.isEmpty()
    }

    fun isPhoneValid(text: String?): Boolean {
        return text != null && !text.isEmpty() && text.length >= 9 && text.length <= 11
    }

    fun isEmailValid(email: String?): Boolean {
        val expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern =
            Pattern.compile(expression, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun isPassWordValid(password: String): Boolean {
        return password.length > 5
    }

    fun confirmPassWordValid(password: String, rePassword: String): Boolean {
        return password == rePassword.trim { it <= ' ' }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val conMgr =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val i = conMgr.activeNetworkInfo
        if (i == null) {
            showToast(context, R.string.txt_no_internet_connected)
            return false
        }
        if (!i.isConnected) {
            showToast(context, R.string.txt_no_internet_connected)
            return false
        }
        return i.isAvailable
    }

    fun coppyText(context: Context, text: String?) {
        val cm =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cm.text = text
//        showToast(context, R.string.txt_copy_successful)
    }

    fun countDayInMonth(month: Int, year: Int): Int {
        var daysInMonth: Int = 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val yearMonthObject: YearMonth = YearMonth.of(year, month)
            daysInMonth = yearMonthObject.lengthOfMonth()
        } else {
            // Create a calendar object and set year and month
            val mycal: Calendar = GregorianCalendar(year, month, 1)
            // Get the number of days in that month
            daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH)
        }
        return daysInMonth
    }

    fun getPathDir(): String {
//old
        val root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
            .toString().trim ()
//        // String root = Environment.getExternalStorageDirectory().toString().trim();
        val file =    File("$root/VideoEditTest")
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.path
    }
}