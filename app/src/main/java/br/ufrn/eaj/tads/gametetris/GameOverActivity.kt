package br.ufrn.eaj.tads.gametetris

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_game_over.*

class GameOverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_over)

        newGame.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        exitGame.setOnClickListener {
            finish()
        }
    }
}
