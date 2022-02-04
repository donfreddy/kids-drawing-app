package com.freddydev.kidsdrawing

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class DrawingView(context: Context, attrs: AttributeSet) : View(context, attrs) {

  private var mDrawPath: CustomPath? = null
  private var mDrawPaint: Paint? = null
  private var mCanvasBitmap: Bitmap? = null
  private var mCanvasPaint: Paint? = null
  private var mBrushSize: Float = 0.toFloat()
  private var defaultColor = Color.BLACK
  private var mCanvas: Canvas? = null

  init {
    setUpDrawing()
  }

  private fun setUpDrawing() {
    mDrawPath = CustomPath(defaultColor, mBrushSize)
    mDrawPaint = Paint()
    mDrawPaint!!.color = defaultColor
    mDrawPaint!!.style = Paint.Style.STROKE
    mDrawPaint!!.strokeJoin = Paint.Join.ROUND
    mDrawPaint!!.strokeCap = Paint.Cap.ROUND
    mCanvasPaint = Paint(Paint.DITHER_FLAG)
    mBrushSize = 20.toFloat()
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    mCanvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
    mCanvas = Canvas(mCanvasBitmap!!)
  }

  // Todo: Change Canvas to Canvas? if fails
  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)
    canvas.drawBitmap(mCanvasBitmap!!, 0f, 0f, mCanvasPaint)
    if (!mDrawPath!!.isEmpty) {
      mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
      mDrawPaint!!.color = mDrawPath!!.color
      canvas.drawPath(mDrawPath!!, mDrawPaint!!)
    }
  }

  @SuppressLint("ClickableViewAccessibility")
  override fun onTouchEvent(event: MotionEvent?): Boolean {
    val touchX = event?.x
    val touchY = event?.y

    when (event?.action) {
      MotionEvent.ACTION_DOWN -> {
        mDrawPath!!.color = defaultColor
        mDrawPath!!.brushThickness = mBrushSize

        mDrawPath!!.reset()
        if (touchX != null && touchY != null) {
          mDrawPath!!.moveTo(touchX, touchY)
        }
      }
      MotionEvent.ACTION_MOVE -> {
        if (touchX != null && touchY != null) {
          mDrawPath!!.lineTo(touchX, touchY)
        }
      }
      MotionEvent.ACTION_UP -> {
        mDrawPath = CustomPath(defaultColor, mBrushSize)
      }
      else -> return false
    }
    invalidate()

    return true
  }

  internal inner class CustomPath(var color: Int, var brushThickness: Float) :
    Path() {}
}