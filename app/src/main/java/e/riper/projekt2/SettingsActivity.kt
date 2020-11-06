package e.riper.projekt2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.settings_activity.*
import kotlinx.android.synthetic.main.settings_content.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        preferences = getSharedPreferences(getSharedPreferencesName(this), Context.MODE_PRIVATE)
        textField_columns.setText(preferences.getInt(keyWidth, defaultWidth).toString())
        textField_rows.setText(preferences.getInt(keyHeight, defaultWidth).toString())
        textField_target.setText(preferences.getInt(keyTarget, defaultTarget).toString())

        button_preferences_save.setOnClickListener {
            val editor = preferences.edit()
            editor.putInt(keyWidth, textField_columns.text.toString().toInt())
            editor.putInt(keyHeight, textField_rows.text.toString().toInt())
            editor.putInt(keyTarget, textField_target.text.toString().toInt())
            editor.apply()
            finish()
        }
    }

    companion object {
        const val defaultWidth: Int = 10
        const val defaultTarget: Int = 5
        const val keyWidth = "width"
        const val keyHeight = "height"
        const val keyTarget = "target"

        fun start(context: Context): Boolean {
            val starter = Intent(context, SettingsActivity::class.java)
            context.startActivity(starter)
            return true
        }

        fun getSharedPreferencesName(context: Context): String {
            return context.packageName + "_preferences"
        }
    }
}
