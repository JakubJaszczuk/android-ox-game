package e.riper.projekt2

import java.lang.StringBuilder
import kotlin.collections.ArrayList
import kotlin.random.Random

class Array2d<T>(val width: Int, val height: Int) {

    private val data: ArrayList<T> = ArrayList<T>(width * height)

    fun getBuffer() = data

    fun set(x: Int, y: Int, value: T) {
        data[convertToIndex(x, y)] = value
    }

    fun setSafe(x: Int, y: Int, value: T) {
        if (!(x < 0 || x >= width || y < 0 || y >= height)) {
            data[convertToIndex(x, y)] = value
        }
    }

    fun get(x: Int, y: Int): T {
        return data[convertToIndex(x, y)]
    }

    fun getSafe(x: Int, y: Int): T? {
        return if(x < 0 || x >= width || y < 0 || y >= height) null
        else data[convertToIndex(x, y)]
    }

    fun getSafeOrDefault(x: Int, y: Int, default: T): T {
        return if(x < 0 || x >= width || y < 0 || y >= height) default
        else data[convertToIndex(x, y)]
    }

    fun convertIndexTo2d(index: Int): Pair<Int, Int> {
        return Pair(index % width, index / width)
    }

    fun convertToIndex(x: Int, y: Int): Int {
        return x + width * y
    }

    override fun toString(): String {
        val res = StringBuilder()
        for ((i, e) in data.withIndex()) {
            res.append(e)
            res.append(" ")
            if((i+1) % width == 0) res.append("\n")
        }
        return res.toString()
    }
}

enum class Cell {
    EMPTY, WHITE, BLACK
}

class Board(w: Int = 10, h: Int = 10, private val hitCount: Int = 5) {

    val board  = Array2d<Cell>(w, h).apply { getBuffer().addAll( (1..w * h).map {Cell.EMPTY} ) }
    private var turnsLeft = w * h

    fun isFree(x: Int, y: Int): Boolean {
        return board.getSafe(x, y) == Cell.EMPTY
    }

    fun noMovesLeft(): Boolean {
        return turnsLeft <= 0
    }

    fun decreaseTurnsCount() {
        --turnsLeft
    }

    fun checkWin(x: Int, y: Int, currentColor: Cell): Boolean {
        if (currentColor == Cell.EMPTY) throw IllegalArgumentException()
        // Check row -> count on both sides
        var countRow = 1
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x + i, y, Cell.EMPTY)) {
                ++countRow
            }
            else break
        }
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x - i, y, Cell.EMPTY)) {
                ++countRow
            }
            else break
        }
        if (countRow >= hitCount) return true
        // Check column -> count on both sides
        var countColumn = 1
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x, y + i, Cell.EMPTY)) {
                ++countColumn
            }
            else break
        }
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x, y - i, Cell.EMPTY)) {
                ++countColumn
            }
            else break
        }
        if (countColumn >= hitCount) return true
        // Check \ -> count on both sides
        var countDecreasing = 1
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x + i, y - i, Cell.EMPTY)) {
                ++countDecreasing
            }
            else break
        }
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x - i, y + i, Cell.EMPTY)) {
                ++countDecreasing
            }
            else break
        }
        if (countDecreasing >= hitCount) return true
        // Check / -> count on both sides
        var countIncreasing = 1
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x + i, y + i, Cell.EMPTY)) {
                ++countIncreasing
            }
            else break
        }
        for (i in 1 until hitCount) {
            if (currentColor == board.getSafeOrDefault(x - i, y - i, Cell.EMPTY)) {
                ++countIncreasing
            }
            else break
        }
        if (countIncreasing >= hitCount) return true
        // Nothing detected :(
        return false
    }

    fun placeAt(x: Int, y: Int, color: Cell): Boolean {
        board.setSafe(x, y, color)
        return checkWin(x, y, color)
    }

    fun placeRandom(color: Cell = Cell.BLACK) {
        val buffer = board.getBuffer()
        buffer[Random.nextInt(0, buffer.size)] = color
    }

    fun clear() {
        board.getBuffer().fill(Cell.EMPTY)
        turnsLeft = board.width * board.height
    }

    override fun toString(): String {
        val res = StringBuilder()
        for ((i, e) in board.getBuffer().withIndex()) {
            if(e == Cell.EMPTY) res.append("_")
            else if(e == Cell.WHITE) res.append("O")
            else if(e == Cell.BLACK) res.append("X")

            res.append(" ")
            if((i+1) % board.width == 0) res.append("\n")
        }
        return res.toString()
    }
}
