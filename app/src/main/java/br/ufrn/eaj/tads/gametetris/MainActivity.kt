package br.ufrn.eaj.tads.gametetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import br.ufrn.eaj.tads.gametetris.parts.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    val LINHA = 36
    val COLUNA = 20
    var running = true
    var speed: Long = 200

    var part: Part =
        PartS(0, 3)


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
            if (checkColisionLeft())
                part.moveLeft()

        }

        btnRigth.setOnClickListener {
            if (checkColisionRigth())
                part.moveRight()
        }

        btnDown.setOnClickListener {
            if (checkColisionX())
                part.moveDown()

        }

        btnRotate.setOnClickListener {
            if(checkColisionLeft() && checkColisionRigth())
                part.rotate()
        }

        gameRun()
    }

    fun printGameBoard() {
        for (j in 1 until COLUNA) {
            board[LINHA - 1][j] = 1
        }
    }

    fun getRadomPart():Part {
        var partId = Random.nextInt(0, 6)
        return when(partId) {
            0 -> {
                return PartT(0, COLUNA/2)
            }
            1 -> {
                return PartJ(0, COLUNA/2)
            }
            2 -> {
                return PartZ(0, COLUNA/2)
            }
            3 -> {
                return PartO(0, COLUNA/2)
            }
            4 -> {
                return PartS(0, COLUNA/2)
            }
            5 -> {
                return PartI(0, COLUNA/2)
            }
            6  -> return PartL(0, COLUNA/2)
            else -> PartO(0, COLUNA/2)
        }
    }

    fun printPart() {
        boardView[part.pointA.x][part.pointA.y]!!.setImageResource(R.drawable.green)
        boardView[part.pointB.x][part.pointB.y]!!.setImageResource(R.drawable.green)
        boardView[part.pointC.x][part.pointC.y]!!.setImageResource(R.drawable.green)
        boardView[part.pointD.x][part.pointD.y]!!.setImageResource(R.drawable.green)
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
        //printGameBoard()
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
                                    boardView[i][j]!!.setImageResource(R.drawable.green)
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
                        part = getRadomPart()
                    }

                }
            }
        }.start()
    }
}
