package com.dmobileapps.dat.app_note.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Handler
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.dmobileapps.dat.app_note.R

class PassCodeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var interaction: PassCodeViewListener? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val size = convertDpToPx(resources, 12f) //size of circle
    private val startPos = size / 2
    private var space = convertDpToPx(resources, 16f) //space between circles

    private var mPassCode = StringBuilder()
    private var isWrongPass = false
    private var showPass = false
    private var rect = Rect()

    //default color
    private var activeColor: Int = Color.parseColor("#CCCCCC")
    private var inactiveColor: Int = Color.parseColor("#33FFFFFF")
    private var errorColor: Int = Color.parseColor("#C73F35")

    init {
        paint.style = Paint.Style.FILL_AND_STROKE

        with(textPaint) {
            try {
                typeface = ResourcesCompat.getFont(context, R.font.baloo_chettan)
            } catch (e: Exception) {
            }
//            textAlignment = TEXT_ALIGNMENT_CENTER
            textAlign = Paint.Align.CENTER
            color = Color.BLACK
            textSize = convertDpToPx(resources, 16f)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val height = size.toInt()
        val width = space * 5 + size * 6 + startPos
        setMeasuredDimension(width.toInt(), height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        for (i in 0..5) {
            if (i <= getPassCode().length - 1) {
                paint.color = activeColor
            } else {
                paint.color = inactiveColor
            }
            if (isWrongPass)
                paint.color = errorColor

            if (showPass && getPassCode().length > i) {
                textPaint.getTextBounds(
                    getPassCode()[i].toString(),
                    0,
                    getPassCode()[i].toString().length,
                    rect
                )
                canvas?.drawText(
                    getPassCode()[i].toString(),
                    i * (size  + space) + startPos,
                    size / 2f + rect.height() / 2,
                    textPaint
                )
            } else
                canvas?.drawCircle(i * (size + space) + startPos, size / 2f, size / 2f, paint)
                //canvas?.drawLine(0f,)

        }
    }

    fun removePassCode() {
        if (getPassCode().isEmpty()) return
        mPassCode.deleteCharAt(getPassCode().length - 1)
        invalidate()
    }

    fun addPassCode(passCode: Int) {
        if (getPassCode().length >= 6) return
        mPassCode.append(passCode)
        invalidate()
        if (getPassCode().length == 6)
            interaction?.onPassCodeDone(getPassCode())
    }

    fun playWrongPassAnimation() {
        isWrongPass = true
        invalidate()
        shake()
        Handler().postDelayed({
            clearPassCode()
            isWrongPass = false
            invalidate()
        }, 350)
    }

    fun clearPassCode() {
        mPassCode.clear()
        invalidate()
    }

    fun showPassCode(isShow: Boolean) {
        showPass = isShow
        invalidate()
    }

    fun setPassCode(passCode: String) {
        mPassCode.clear()
        mPassCode.append(passCode)
    }

    private fun getPassCode(): String {
        return mPassCode.toString()
    }

    fun setColor(active: Int? = null, inactive: Int? = null, error: Int? = null) {
        active?.let {
            this.activeColor = it
        }
        inactive?.let {
            this.inactiveColor = it
        }
        error?.let {
            this.errorColor = it
        }
        invalidate()
    }

    fun setSpaceSize(dp: Float) {
        space = convertDpToPx(resources, dp)
        invalidate()
    }

    fun setOnDoneListener(interaction: PassCodeViewListener) {
        this.interaction = interaction
    }

    interface PassCodeViewListener {
        fun onPassCodeDone(passCode: String)
    }
}