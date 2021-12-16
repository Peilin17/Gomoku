package com.example.gomoku


import android.graphics.Point


class checkWinner {
    private var point1: Point? = null
    private var point2: Point? = null


    fun checkFiveInLineWinner(points: List<Point>): Boolean {
        for (point in points) {
            val x = point.x
            val y = point.y
            when {
                checkHorizontal(x, y, points) -> {
                    return true
                }
                checkVertical(x, y, points) -> {
                    return true
                }
                checkLeftDiagonal(x, y, points) -> {
                    return true
                }
                checkRighttDiagonal(x, y, points) -> {
                    return true
                }
            }
        }
        return false
    }

    // 横
    private fun checkHorizontal(
        x: Int,
        y: Int,
        points: List<Point>
    ): Boolean {
        return check(x, y, points, "HORIZONTAL")
    }

    // 竖
    private fun checkVertical(
        x: Int,
        y: Int,
        points: List<Point>
    ): Boolean {
        return check(x, y, points, "VERTICAL")
    }

    // 左斜
    private fun checkLeftDiagonal(
        x: Int,
        y: Int,
        points: List<Point>
    ): Boolean {
        return check(x, y, points, "LEFT_DIAGONAL")
    }

    // 右斜
    private fun checkRighttDiagonal(
        x: Int,
        y: Int,
        points: List<Point>
    ): Boolean {
        return check(x, y, points, "RIGHT_DIAGONAL")
    }


    private fun check(
        x: Int,
        y: Int,
        points: List<Point>,
        checkOri: String
    ): Boolean {
        var count = 1
        for (i in 1 until MAX_COUNT_IN_LINE) {
            when (checkOri) {
                "HORIZONTAL" -> point1 = Point(x - i, y)
                "VERTICAL" -> point1 = Point(x, y - i)
                "LEFT_DIAGONAL" -> point1 = Point(x - i, y + i)
                "RIGHT_DIAGONAL" -> point1 = Point(x + i, y + i)
            }
            if (points.contains(point1)) {
                count++
            } else {
                break
            }
        }
        for (i in 1 until MAX_COUNT_IN_LINE) {
            when (checkOri) {
                "HORIZONTAL" -> point2 = Point(x + i, y)
                "VERTICAL" -> point2 = Point(x, y + i)
                "LEFT_DIAGONAL" -> point2 = Point(x + i, y - i)
                "RIGHT_DIAGONAL" -> point2 = Point(x - i, y - i)
            }
            if (points.contains(point2)) {
                count++
            } else {
                break
            }
        }
        return count == MAX_COUNT_IN_LINE
    }
}