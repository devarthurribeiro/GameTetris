package br.ufrn.eaj.tads.gametetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T


class MainActivity : AppCompatActivity() {

    val LINHA = 36
    val COLUNA = 26
    var running = true
    var speed: Long = 300

    var part = PartL(0, 15)


    var board = Array(LINHA) {
        Array(COLUNA) { 0 }
    }

    var boardView = Array(LINHA) {
        arrayOfNulls<ImageView>(COLUNA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridboard.rowCount = LINHA
        gridboard.columnCount = COLUNA

        val inflater = LayoutInflater.from(this)

        for (i in 0 until LINHA) {
            for (j in 0 until COLUNA) {
                boardView[i][j] =
                    inflater.inflate(R.layout.inflate_image_view, gridboard, false) as ImageView
                gridboard.addView(boardView[i][j])
            }
        }

        btnLeft.setOnClickListener {
            if (checkColisionLeft()) {
                part.moveLeft()
            }
        }

        btnRigth.setOnClickListener {
            if (checkColisionRigth()) {
                part.moveRight()

            }
        }

        btnDown.setOnClickListener {
            if (part.pointA.x + 1 < LINHA && board[part.pointA.x + 1][part.pointA.y] != 1) {
                part.moveDown()
            }
        }

        gameRun()
    }

    fun printGameBoard() {
        for (j in 1 until COLUNA) {
            board[LINHA - 1][j] = 1
        }
    }

    fun printPart() {
        boardView[part.pointA.x][part.pointA.y]!!.setImageResource(R.drawable.white)
        boardView[part.pointB.x][part.pointB.y]!!.setImageResource(R.drawable.white)
        boardView[part.pointC.x][part.pointC.y]!!.setImageResource(R.drawable.white)
        boardView[part.pointD.x][part.pointD.y]!!.setImageResource(R.drawable.white)
    }

    fun checkColisionX(): Boolean {
        return ((part.pointA.x + 1 < LINHA && board[part.pointA.x + 1][part.pointA.y] != 1) &&
                (part.pointB.x + 1 < LINHA && board[part.pointB.x + 1][part.pointB.y] != 1) &&
                (part.pointC.x + 1 < LINHA && board[part.pointC.x + 1][part.pointC.y] != 1) &&
                (part.pointD.x + 1 < LINHA && board[part.pointD.x + 1][part.pointD.y] != 1))
    }

    fun checkColisionRigth(): Boolean {
        return ((part.pointA.y + 1 < COLUNA && board[part.pointA.x][part.pointA.y + 1] != 1) &&
                (part.pointB.y + 1 < COLUNA && board[part.pointB.x][part.pointB.y + 1] != 1) &&
                (part.pointC.y + 1 < COLUNA && board[part.pointC.x][part.pointC.y + 1] != 1) &&
                (part.pointD.y + 1 < COLUNA && board[part.pointD.x][part.pointD.y + 1] != 1))
    }

    fun checkColisionLeft(): Boolean {
        return ((part.pointA.y - 1 >= 0 && board[part.pointA.x][part.pointA.y - 1] != 1) &&
                (part.pointB.y - 1 >= 0 && board[part.pointB.x][part.pointB.y - 1] != 1) &&
                (part.pointC.y - 1 >= 0 && board[part.pointC.x][part.pointC.y - 1] != 1) &&
                (part.pointD.y - 1 >= 0 && board[part.pointD.x][part.pointD.y - 1] != 1))
    }

    fun gameRun() {
        printGameBoard()
        Thread {
            while (running) {
                Thread.sleep(speed)
                runOnUiThread {
                    //limpa tela
                    for (i in 0 until LINHA) {
                        for (j in 0 until COLUNA) {
                            when (board[i][j]) {
                                0 -> {
                                    boardView[i][j]!!.setImageResource(R.drawable.black)
                                }
                                1 -> {
                                    boardView[i][j]!!.setImageResource(R.drawable.white)
                                }
                            }
                        }
                    }
                    //move peça atual
                    if (checkColisionX()) {
                        part.moveDown()
                        printPart()
                    } else {
                        printPart()
                        board[part.pointA.x][part.pointA.y] = 1
                        board[part.pointB.x][part.pointB.y] = 1
                        board[part.pointC.x][part.pointC.y] = 1
                        board[part.pointD.x][part.pointD.y] = 1
                        part = PartL(0, 15)
                    }

                }
            }
        }.start()
    }
}
