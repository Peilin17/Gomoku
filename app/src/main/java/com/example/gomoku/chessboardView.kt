package com.example.gomoku

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory.decodeResource
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast


class chessboardView :
    View {
    // 棋盘的宽度，也是长度
    private var mViewWidth = 0

    // 棋盘每格的长度
    private var maxLineHeight = 0f
    private val paint: Paint = Paint()
    private val paint2: Paint = Paint()


    private var mwhitePiece: Bitmap? = null


    private var mblackPiece: Bitmap? = null
    private val ratioPieceOfLineHeight = 3 * 1.0f / 4


    private var mIsWhite = true


    var mwhiteArray: ArrayList<Point> = ArrayList()
    var mblackArray: ArrayList<Point> = ArrayList()


    private var mIsGameOver = false


    private var mIsWhiteWinner = false


    constructor(
        context: Context?,
        attrs: AttributeSet?
    ) : super(context, attrs) {

        init()
    }


    private fun init() {
        paint.color = -0x78000000
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.STROKE
        mwhitePiece = decodeResource(resources, R.drawable.white)
        mblackPiece = decodeResource(resources, R.drawable.black)
        paint2.color = 0x78000000
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthModel = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightModel = MeasureSpec.getMode(heightMeasureSpec)
        var width = Math.min(widthSize, heightSize)
        if (widthModel == MeasureSpec.UNSPECIFIED) {
            width = heightSize
        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
            width = widthSize
        }
        setMeasuredDimension(width, width)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        // 网格
        drawBoard(canvas!!)

        // 棋子
        drawPieces(canvas!!)

        // 检查游戏是否结束
        checkGameOver()
    }

    // 绘制棋子
    private fun drawPieces(canvas: Canvas) {
        run {
            var i = 0
            val n = mwhiteArray.size
            while (i < n) {
                val whitePoint = mwhiteArray[i]
                val left =
                    (whitePoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
                val top =
                    (whitePoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
                canvas.drawBitmap(mwhitePiece!!, left, top, null)
                i++
            }
        }
        var i = 0
        val n = mblackArray.size
        while (i < n) {
            val blackPoint = mblackArray[i]
            val left =
                (blackPoint.x + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
            val top =
                (blackPoint.y + (1 - ratioPieceOfLineHeight) / 2) * maxLineHeight
            canvas.drawBitmap(mblackPiece!!, left, top, null)
            i++
        }
    }

    // 绘制网线
    private fun drawBoard(canvas: Canvas) {
        canvas.drawColor(resources.getColor(R.color.board))
        val w = mViewWidth
        val lineHeight = maxLineHeight
        for (i in 0 until MAX_LINE) {
            val startX = (lineHeight / 2).toInt()
            val endX = (w - lineHeight / 2).toInt()
            val y = ((0.5 + i) * lineHeight).toInt()
            canvas.drawLine(startX.toFloat(), y.toFloat(), endX.toFloat(), y.toFloat(), paint)
            canvas.drawLine(y.toFloat(), startX.toFloat(), y.toFloat(), endX.toFloat(), paint)
        }
    }

    // 检查是否结束
    private fun checkGameOver() {
        val checkWinner = checkWinner()
        val whiteWin: Boolean = checkWinner.checkFiveInLineWinner(mwhiteArray)
        val blackWin: Boolean = checkWinner.checkFiveInLineWinner(mblackArray)
        if (whiteWin || blackWin) {
            mIsGameOver = true
            mIsWhiteWinner = whiteWin
            val text = if (mIsWhiteWinner) "White WIN!" else "Black WIN!"
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()


        } else {
            if (mwhiteArray.size == 113 || mblackArray.size == 113 || (mwhiteArray.size + mblackArray.size) == 265) {
                mIsGameOver = true
                mIsWhiteWinner = whiteWin
                val text = "Even!!!"
                Toast.makeText(context, text, Toast.LENGTH_LONG).show()
            }
        }
    }

    //绘制棋子
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        maxLineHeight = mViewWidth * 1.0f / MAX_LINE
        val pieceWidth = (maxLineHeight * ratioPieceOfLineHeight).toInt()
        mwhitePiece = Bitmap.createScaledBitmap(mwhitePiece!!, pieceWidth, pieceWidth, false)
        mblackPiece = Bitmap.createScaledBitmap(mblackPiece!!, pieceWidth, pieceWidth, false)
    }

    // regret
    fun regretPlay() {
        if (!mIsGameOver) {
            var wsize = mwhiteArray.size
            var bsize = mblackArray.size
            if (mIsWhite) {
                if (bsize > 0) {
                    mblackArray.remove(mblackArray[bsize - 1])
                    mIsWhite = !mIsWhite
                }
            } else {
                if (wsize > 0) {
                    mwhiteArray.remove(mwhiteArray[wsize - 1])
                    mIsWhite = !mIsWhite
                }
            }
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //MainActivity.event()
        if (mIsGameOver) {
            return false
        }
        val action = event.action
        if (action == MotionEvent.ACTION_UP) {
            val x = event.x.toInt()
            val y = event.y.toInt()
            val point = getValidPoint(x, y)
            if (mwhiteArray.contains(point) || mblackArray.contains(point)) {
                return false
            }
            if (mIsWhite) {
                mwhiteArray.add(point)
                //MainActivity.event("white", point.x, point.y)
                //model.appendEvent("white", point.x, point.y)
            } else {
                mblackArray.add(point)
                //MainActivity.event("black", point.x, point.y)
                //model.appendEvent("black", point.x, point.y)
            }
            invalidate()
            mIsWhite = !mIsWhite
        }
        return true
    }

    //help method
    private fun getValidPoint(x: Int, y: Int): Point {
        val validX = (x / maxLineHeight).toInt()
        val validY = (y / maxLineHeight).toInt()
        return Point(validX, validY)
    }

    // restart
    fun playAgain() {
        mwhiteArray.clear()
        mblackArray.clear()
        mIsGameOver = false
        mIsWhiteWinner = false
        mIsWhite = true
        invalidate()
    }


}


