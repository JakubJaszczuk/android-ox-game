package e.riper.projekt2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

        button_single_game_start.setOnClickListener {
            GameActivity.start(this)
        }

        button_multi_game_start.setOnClickListener {
            MultiGameActivity.start(this)
        }

		button_leaderboards_open.setOnClickListener {
			LeaderboardsActivity.start(this)
		}

		button_preferences.setOnClickListener {
			SettingsActivity.start(this)
		}

		button_exit_app.setOnClickListener {
			finishAffinity()
		}
	}
}
