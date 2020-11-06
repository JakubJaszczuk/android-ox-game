package e.riper.projekt2

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.core.os.postDelayed
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_multi_game.*
import kotlinx.android.synthetic.main.content_multi_game.*
import kotlinx.coroutines.launch
import kotlin.math.max

class MultiGameModel(application: Application) : AndroidViewModel(application) {

    val board = prepareBoard()
    val interactionManager = MultiInteractionManager(board)
    var win1 = 0
    var win2 = 0
    var rowId: Long? = null

    private fun prepareBoard(): Board {
        val app = getApplication<Application>()
        val preferences = app.getSharedPreferences(SettingsActivity.getSharedPreferencesName(app), Context.MODE_PRIVATE)
        val w = preferences.getInt(SettingsActivity.keyWidth, SettingsActivity.defaultWidth)
        val h = preferences.getInt(SettingsActivity.keyHeight, SettingsActivity.defaultWidth)
        val t = preferences.getInt(SettingsActivity.keyTarget, SettingsActivity.defaultTarget)
        return Board(w, h, t)
    }
}

class MultiGameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_game)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val model: MultiGameModel by viewModels()
        board_view.board = model.board
        board_view.interactionManager = model.interactionManager
        textView_score.text = getString(R.string.score_formatter, model.win1, model.win2)

        model.interactionManager.reactor = object : WinEventListener {
            override fun onWin(winner: Cell) {
                when (winner) {
                    Cell.EMPTY -> Snackbar.make(board_view, R.string.winner_none, Snackbar.LENGTH_SHORT).show()
                    Cell.WHITE -> {
                        model.win1++
                        Snackbar.make(board_view, R.string.winner_white, Snackbar.LENGTH_SHORT).show()
                    }
                    Cell.BLACK -> {
                        model.win2++
                        Snackbar.make(board_view, R.string.winner_black, Snackbar.LENGTH_SHORT).show()
                    }
                }
                textView_score.text = getString(R.string.score_formatter, model.win1, model.win2)
                // Board - po opóźnieniu daj następną turę
                Handler().postDelayed(1000) {
                    model.board.clear()
                    board_view.invalidate()
                    model.interactionManager.clearFlags()
                }
                // Dodaj wynik do listy
                if (winner != Cell.EMPTY) {
                    model.viewModelScope.launch {
                        val database = AppDatabase.getInstance(this@MultiGameActivity)
                        val playerName = if (model.win1 > model.win2) "Kółko" else "Krzyżyk"
                        val id = if (model.rowId == null) 0 else model.rowId
                        val score = HighScore(id!!, playerName, max(model.win1, model.win2), System.currentTimeMillis() / 1000)
                        if (model.rowId == null) {
                            model.rowId = database.highScoresDao().add(score)
                        }
                        else database.highScoresDao().update(score)
                    }
                }
            }
        }
    }

    companion object {
        fun start(context: Context): Boolean {
            val starter = Intent(context, MultiGameActivity::class.java)
            context.startActivity(starter)
            return true
        }
    }
}
