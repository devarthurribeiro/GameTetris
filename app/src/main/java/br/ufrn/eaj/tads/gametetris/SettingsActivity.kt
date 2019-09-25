package br.ufrn.eaj.tads.gametetris

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    val PREFS = "game_settings"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val speed = settings.getLong("speed", 200L)

        if (speed == 100L)
            rdGameMode.check(d3.id)
        if (speed == 200L)
            rdGameMode.check(d2.id)
        if (speed == 300L)
            rdGameMode.check(d1.id)

    }

    override fun onStop() {
        super.onStop()
        val settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val editor = settings.edit()

        when (rdGameMode.checkedRadioButtonId) {
            d1.id -> {
                editor.putLong("speed", 300)
            }
            d2.id -> {
                editor.putLong("speed", 200)
            }
            d3.id -> {
                editor.putLong("speed", 100)
            }
        }

        editor.commit()
    }
}
