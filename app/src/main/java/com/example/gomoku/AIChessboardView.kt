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



class AIChessboardView :
    View {

    private var mViewWidth = 0


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
        mwhitePiece = decodeResource(
            resources,
            R.drawable.white
        )
        mblackPiece = decodeResource(
            resources,
            R.drawable.black
        )
       paint2.color = 0x78000000
    }

    //获取自定义的长宽
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

        // 绘制棋盘的网格
        drawBoard(canvas!!)

        // 绘制棋盘的黑白棋子
        drawPieces(canvas!!)

        // 检查游戏是否结束
        checkGameOver()
    }

    // 根据黑白棋子的数组绘制棋子
    fun drawPieces(canvas: Canvas) {
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

    // 绘制棋盘的网线
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

    // 谁赢？
    private fun checkGameOver() {
        val checkWinner = checkWinner()
        val whiteWin: Boolean = checkWinner.checkFiveInLineWinner(mwhiteArray)
        val blackWin: Boolean = checkWinner.checkFiveInLineWinner(mblackArray)
        if (whiteWin || blackWin) {
            mIsGameOver = true
            mIsWhiteWinner = whiteWin
            var text: String = ""
            if (mIsWhiteWinner) {
                text = "White WIN!"
                mblackArray.remove(mblackArray[mblackArray.size - 1])
            } else {
                text = "Black WIN!"
            }
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

    //改变棋子大小
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w
        maxLineHeight = mViewWidth * 1.0f / MAX_LINE
        val pieceWidth = (maxLineHeight * ratioPieceOfLineHeight).toInt()
        mwhitePiece = Bitmap.createScaledBitmap(mwhitePiece!!, pieceWidth, pieceWidth, false)
        mblackPiece = Bitmap.createScaledBitmap(mblackPiece!!, pieceWidth, pieceWidth, false)
    }

    // regret what just played 悔棋
    fun regretPlay() {
        if (!mIsGameOver) {
            var wsize = mwhiteArray.size
            var bsize = mblackArray.size
            if (wsize > 0 && bsize > 0) {
                mwhiteArray.remove(mwhiteArray[wsize - 1])
                mblackArray.remove(mblackArray[bsize - 1])
            }
            invalidate()
        }
    }

    //下棋
    override fun onTouchEvent(event: MotionEvent): Boolean {
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
                mIsWhite = !mIsWhite

                //after user pick point, AI pick a point
                val ai = AI(mwhiteArray, mblackArray)
                val apoint = ai.getNextPoint()
                mblackArray.add(apoint)
                //MainActivity.event("black", apoint.x, apoint.y)
                mIsWhite = !mIsWhite
            }

            invalidate()
        }
        checkGameOver()
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

    fun switchModel() {
        mwhiteArray.clear()
        mblackArray.clear()
        mIsGameOver = false
        mIsWhiteWinner = false
        mIsWhite = true
        invalidate()
        Toast.makeText(context, "in A.I. Mode", Toast.LENGTH_SHORT).show()

    }


}


