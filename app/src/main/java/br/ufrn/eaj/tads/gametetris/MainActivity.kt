package br.ufrn.eaj.tads.gametetris

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import br.ufrn.eaj.tads.gametetris.parts.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    val PREFS = "game_settings"
    val ROW = 36
    val COL = 20

    var gameOver = false
    var running = true
    var speed: Long = 200
    var partsNumber = 7
    var points = 0
    var partId = Random.nextInt(0, partsNumber - 1)
    var part: Part = getRadomPart(partId, 0, COL / 2)

    val vm: BoardViewModel by lazy {
        ViewModelProviders.of(this)[BoardViewModel::class.java]
    }

    var boardView = Array(ROW) {
        arrayOfNulls<ImageView>(COL)
    }

    var boardViewNextPart = Array(2) {
        arrayOfNulls<ImageView>(4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridboard.rowCount = ROW
        gridboard.columnCount = COL
        gridboardNextPart.rowCount = 2
        gridboardNextPart.columnCount = 4

        var bundle :Bundle ?=intent.extras

        if (bundle!=null && GameState.saved) {
            vm.board = GameState.board
            points = GameState.points
            part = GameState.part
        } else {
            GameState.resetState()
        }

        val settings = getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        speed = settings.getLong("speed", 200)
        partsNumber = settings.getInt("partNumber", 7)

        val inflater = LayoutInflater.from(this)

        for (i in 0 until ROW) {
            for (j in 0 until COL) {
                boardView[i][j] =
                    inflater.inflate(R.layout.inflate_image_view, gridboard, false) as ImageView
                gridboard.addView(boardView[i][j])
            }
        }

        for (i in 0 until 2) {
            for (j in 0 until 4) {
                boardViewNextPart[i][j] =
                    inflater.inflate(
                        R.layout.inflate_image_view,
                        gridboardNextPart,
                        false
                    ) as ImageView
                gridboardNextPart.addView(boardViewNextPart[i][j])
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
            if (checkColisionX() && checkColisionLeft() && checkColisionRigth())
                part.rotate()
        }

        gameRun()
    }

    override fun onPause() {
        super.onPause()
        if(!gameOver) {
            GameState.points = points
            GameState.board = vm.board
            GameState.part = part
            GameState.saved = true
        } else
            GameState.resetState()
        running = false
    }

    override fun onRestart() {
        super.onRestart()
        running = true
        gameRun()
    }

    fun printGameBoard() {
        for (j in 1 until COL) {
            vm.board[ROW - 1][j] = 1
            vm.board[ROW - 2][j] = 1
            vm.board[ROW - 3][j] = 1
            vm.board[ROW - 4][j] = 1
            vm.board[ROW - 5][j] = 1
        }
    }

    fun getRadomPart(id: Int, row: Int, col: Int): Part {

        val p = when (id) {
            0 -> {
                return PartT(row, col)
            }
            1 -> {
                return PartJ(row, col)
            }
            2 -> {
                return PartZ(row, col)
            }
            3 -> {
                return PartO(row, col)
            }
            4 -> {
                return PartS(row, col)
            }
            5 -> {
                return PartI(row, col)
            }
            6 -> return PartL(row, col)
            else -> return PartO(row, col)
        }

        return p
    }

    fun printPart(id: Int) {
        boardView[part.pointA.x][part.pointA.y]!!.setImageResource(getPixel(id))
        boardView[part.pointB.x][part.pointB.y]!!.setImageResource(getPixel(id))
        boardView[part.pointC.x][part.pointC.y]!!.setImageResource(getPixel(id))
        boardView[part.pointD.x][part.pointD.y]!!.setImageResource(getPixel(id))
    }

    fun printNextPart(id: Int) {
        clearScreenNextPart()
        val partNext = getRadomPart(id, 0, 1)
        boardViewNextPart[partNext.pointA.x][partNext.pointA.y]!!.setImageResource(getPixel(partNext.id))
        boardViewNextPart[partNext.pointB.x][partNext.pointB.y]!!.setImageResource(getPixel(partNext.id))
        boardViewNextPart[partNext.pointC.x][partNext.pointC.y]!!.setImageResource(getPixel(partNext.id))
        boardViewNextPart[partNext.pointD.x][partNext.pointD.y]!!.setImageResource(getPixel(partNext.id))
    }

    fun checkColisionX(): Boolean {
        return ((part.pointA.x + 1 < ROW && vm.board[part.pointA.x + 1][part.pointA.y] < 1) &&
                (part.pointB.x + 1 < ROW && vm.board[part.pointB.x + 1][part.pointB.y] < 1) &&
                (part.pointC.x + 1 < ROW && vm.board[part.pointC.x + 1][part.pointC.y] < 1) &&
                (part.pointD.x + 1 < ROW && vm.board[part.pointD.x + 1][part.pointD.y] < 1))
    }

    fun checkColisionRigth(): Boolean {
        return ((part.pointA.y + 1 < COL && vm.board[part.pointA.x][part.pointA.y + 1] < 1) &&
                (part.pointB.y + 1 < COL && vm.board[part.pointB.x][part.pointB.y + 1] < 1) &&
                (part.pointC.y + 1 < COL && vm.board[part.pointC.x][part.pointC.y + 1] < 1) &&
                (part.pointD.y + 1 < COL && vm.board[part.pointD.x][part.pointD.y + 1] < 1))
    }

    fun checkColisionLeft(): Boolean {
        return ((part.pointA.y - 1 >= 0 && vm.board[part.pointA.x][part.pointA.y - 1] < 1) &&
                (part.pointB.y - 1 >= 0 && vm.board[part.pointB.x][part.pointB.y - 1] < 1) &&
                (part.pointC.y - 1 >= 0 && vm.board[part.pointC.x][part.pointC.y - 1] < 1) &&
                (part.pointD.y - 1 >= 0 && vm.board[part.pointD.x][part.pointD.y - 1] < 1))
    }

    fun destroy(row: Int) {
        vm.board[row] = Array(COL) { 0 }
        for (i in row downTo 1) {
            vm.board[i] = vm.board[i - 1]
        }
        points += COL
        txtPoints.text = "Pontos: $points"
    }

    fun showGameOver() {
        startActivity(Intent(this, GameOverActivity::class.java))
        finish()
    }

    fun checkToDestroy() {
        for (i in 0 until ROW) {
            var cont = 0
            for (j in 0 until COL) {
                if (vm.board[i][j] == 0)
                    break
                else {
                    cont++
                    if (cont == COL)
                        destroy(i)
                }
            }
        }
    }

    fun getPixel(id: Int): Int {
        return when (id) {
            0 -> {
                R.drawable.blackg1
            }
            1 -> {
                R.drawable.p1
            }
            2 -> {
                R.drawable.p2
            }
            3 -> {
                R.drawable.p3
            }
            4 -> {
                R.drawable.p4
            }
            5 -> {
                R.drawable.p5
            }
            6 -> {
                R.drawable.p6
            }
            7 -> {
                R.drawable.p7
            }
            else -> {
                R.drawable.p7
            }
        }
    }

    fun clearScreen() {
        for (i in 0 until ROW) {
            for (j in 0 until COL) {
                boardView[i][j]!!.setImageResource(getPixel(vm.board[i][j]))
            }
        }
    }

    fun clearScreenNextPart() {
        for (i in 0 until 2) {
            for (j in 0 until 4) {
                boardViewNextPart[i][j]!!.setImageResource(getPixel(vm.board[i][j]))
            }
        }
    }

    fun movePart() {
        if (checkColisionX()) {
            if (part.pointA.x == 0) {
                partId = Random.nextInt(0, partsNumber - 1)

                printNextPart(partId)
            }
            part.moveDown()
            printPart(part.id)
        } else {
            if (part.pointA.x == 0) {
                running = false
                Thread.interrupted()
                gameOver = true
            }

            printPart(part.id)

            vm.board[part.pointA.x][part.pointA.y] = part.id
            vm.board[part.pointB.x][part.pointB.y] = part.id
            vm.board[part.pointC.x][part.pointC.y] = part.id
            vm.board[part.pointD.x][part.pointD.y] = part.id

            part = getRadomPart(partId, 0, COL / 2)
        }
    }

    fun gameRun() {
        //printGameBoard()
        clearScreenNextPart()
        printNextPart(partId)
        Thread {
            while (running) {
                Thread.sleep(speed)
                runOnUiThread {
                    clearScreen()
                    movePart()
                    checkToDestroy()
                }
            }
            if (gameOver)
                showGameOver()
        }.start()
    }
}
