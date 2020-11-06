package e.riper.projekt2

import android.util.Log
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.random.Random

interface WinEventListener {
    fun onWin(winner: Cell)
}

interface InteractionManager {
    fun makeMove(x: Int, y: Int)
}

class SingleInteractionManager(private val board: Board): InteractionManager {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor()
    private var clickable = true
    private var ended = false
    var reactor: WinEventListener? = null

    fun clearFlags() {
        ended = false
        clickable = true
    }

    override fun makeMove(x: Int, y: Int) {
        // Warunek końca gry
        if (!ended && clickable && board.isFree(x, y)) {
            // Ruch gracza
            clickable = false
            val winW = board.placeAt(x, y, Cell.WHITE)
            if(winW) {
                ended = true
                reactor?.onWin(Cell.WHITE)
            }
            board.decreaseTurnsCount()
            if (board.noMovesLeft()) {
                ended = true
                reactor?.onWin(Cell.EMPTY)
            }

            // Ruch komputera
            if (!ended) {
                executor.execute {
                    var aiX = 0
                    var aiY = 0
                    do {
                        aiX = Random.nextInt(0, board.board.width)
                        aiY = Random.nextInt(0, board.board.height)
                    } while (!board.isFree(aiX, aiY))
                    // Simulate AI
                    //Thread.sleep(2000)
                    val winB = board.placeAt(aiX, aiY, Cell.BLACK)
                    if(winB) {
                        Log.i("WINNER", "BLACK - $aiX  $aiY")
                        ended = true
                        reactor?.onWin(Cell.BLACK)
                    }
                    board.decreaseTurnsCount()
                    if (board.noMovesLeft()) {
                        ended = true
                        reactor?.onWin(Cell.EMPTY)
                    }
                    clickable = true
                }
            }
        }
    }
}

class MultiInteractionManager(private val board: Board): InteractionManager {

    private var player = Cell.WHITE
    private var clickable = true
    private var ended = false
    var reactor: WinEventListener? = null

    fun clearFlags() {
        ended = false
        clickable = true
    }

    override fun makeMove(x: Int, y: Int) {
        // Warunek końca gry
        if (!ended && clickable && board.isFree(x, y)) {
            // Ruch gracza
            clickable = false
            val win = board.placeAt(x, y, player)
            if(win) {
                ended = true
                reactor?.onWin(player)
            }
            // Czy można grać dalej
            board.decreaseTurnsCount()
            if (board.noMovesLeft()) {
                ended = true
                reactor?.onWin(Cell.EMPTY)
            }
            // Zamień gracza
            player = if (player == Cell.WHITE) Cell.BLACK else Cell.WHITE
            clickable = true
        }
    }
}
