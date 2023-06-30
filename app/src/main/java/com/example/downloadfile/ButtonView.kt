package com.example.downloadfile

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class ButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){

    private var widthSize = 0
    private var heightSize = 0
    private var fillWidth = 0
    private var sweepAngle = 0
    private var buttonText = "Hello"
    private var fillColor = R.color.colorPrimaryDark

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
        color = context.getColor(R.color.colorPrimary)
    }

    private var buttonState: ButtonState by Delegates.observable(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> {
                Log.i("ButtonView", "ButtonState.Clicked")
                configureDownloadAnimation()
            }
            ButtonState.Loading -> {
                Log.i("ButtonView", "ButtonState.Loading")
                isClickable = false
                valueAnimator.start()
                buttonText = getContext().getString(R.string.buttonLoading)
            }
        }
    }

    init {
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            buttonText = getString(R.styleable.LoadingButton_text).toString()
            fillColor = getResourceId(R.styleable.LoadingButton_fillColor, R.color.colorPrimaryDark)
        }
        buttonState = ButtonState.Completed
    }

    private val valueAnimator = ValueAnimator()

    private fun configureDownloadAnimation() {
        Log.i("ButtonView", "setting up valueAnimator")
        valueAnimator.setIntValues(0, widthSize)
        valueAnimator.duration = 2000L
        valueAnimator.repeatCount = ValueAnimator.INFINITE
        valueAnimator.addUpdateListener { animator ->
            Log.i("ButtonView", "valueAnimator update")
            fillWidth = animator.animatedValue as Int;
            sweepAngle =  ((fillWidth.toFloat() / widthSize.toFloat()) * 360).toInt()

            if (fillWidth >= widthSize && buttonState ==ButtonState.Completed) {
                Log.i("ButtonView", "fillWidth at maximum, resetting animation")
                stopDownloadAnimation()
            }

            invalidate()
        }
        buttonState = ButtonState.Loading
    }

    fun downloadFinished(){
        buttonState = ButtonState.Completed
    }

    fun downloadStarted(){
        buttonState = ButtonState.Clicked
    }

    private fun stopDownloadAnimation() {
        Log.i("ButtonView", "stopping valueAnimator")
        isClickable = true
        buttonText = context.getString(R.string.buttonName)
        valueAnimator.cancel()
        valueAnimator.removeAllUpdateListeners()
        fillWidth = 0
        sweepAngle = 0
        invalidate()
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = context.getColor(R.color.colorPrimary)
        canvas.drawRect(0f, 0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        paint.color = resources.getColor(fillColor)
        canvas.drawRect(0f, 0f, fillWidth.toFloat(), heightSize.toFloat(), paint)

        paint.color = resources.getColor(R.color.colorAccent)
        val smallerSide = (if(widthSize > heightSize) heightSize else widthSize).toFloat()
        val padding = smallerSide * 0.1f
        val left = widthSize - smallerSide + padding
        val top = 0f + padding
        val right = widthSize.toFloat() - padding
        val bottom = smallerSide - padding
        canvas.drawArc(left, top, right, bottom, 0f, sweepAngle.toFloat(), true, paint)

        paint.color = resources.getColor(R.color.white)
        canvas.drawText(buttonText, width / 2f, height / 2f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}

open class ButtonState {
    object Clicked : ButtonState()
    object Loading : ButtonState()
    object Completed : ButtonState()
}