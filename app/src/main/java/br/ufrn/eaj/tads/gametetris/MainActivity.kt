package br.ufrn.eaj.tads.gametetris

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProviders
import br.ufrn.eaj.tads.gametetris.parts.*
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    val LINHA = 36
    val COLUNA = 20
    var running = true
    var speed: Long = 200
    var points = 0

    var part: Part = PartI(0, 3) //getRadomPart()


    val vm : BoardViewModel by lazy {
        ViewModelProviders.of(this)[BoardViewModel::class.java]
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

    override fun onPause() {
        super.onPause()
        running =   false
    }

    override fun onRestart() {
        super.onRestart()
        running = true
        gameRun()
    }
    fun printGameBoard() {
        for (j in 1 until COLUNA) {
            vm.board[LINHA - 1][j] = 1
            vm.board[LINHA - 2][j] = 1
            vm.board[LINHA - 3][j] = 1
            vm.board[LINHA - 4][j] = 1
            vm.board[LINHA - 5][j] = 1
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
            else -> return PartO(0, COLUNA/2)
        }
    }

    fun printPart(id: Int) {
        boardView[part.pointA.x][part.pointA.y]!!.setImageResource(getPixel(id))
        boardView[part.pointB.x][part.pointB.y]!!.setImageResource(getPixel(id))
        boardView[part.pointC.x][part.pointC.y]!!.setImageResource(getPixel(id))
        boardView[part.pointD.x][part.pointD.y]!!.setImageResource(getPixel(id))
    }

    fun checkColisionX(): Boolean {
        return ((part.pointA.x + 1 < LINHA && vm.board[part.pointA.x + 1][part.pointA.y] < 1) &&
                (part.pointB.x + 1 < LINHA && vm.board[part.pointB.x + 1][part.pointB.y] < 1) &&
                (part.pointC.x + 1 < LINHA && vm.board[part.pointC.x + 1][part.pointC.y] < 1) &&
                (part.pointD.x + 1 < LINHA && vm.board[part.pointD.x + 1][part.pointD.y] < 1))
    }

    fun checkColisionRigth(): Boolean {
        return ((part.pointA.y + 1 < COLUNA && vm.board[part.pointA.x][part.pointA.y + 1] < 1) &&
                (part.pointB.y + 1 < COLUNA && vm.board[part.pointB.x][part.pointB.y + 1] < 1) &&
                (part.pointC.y + 1 < COLUNA && vm.board[part.pointC.x][part.pointC.y + 1] < 1) &&
                (part.pointD.y + 1 < COLUNA && vm.board[part.pointD.x][part.pointD.y + 1] < 1))
    }

    fun checkColisionLeft(): Boolean {
        return ((part.pointA.y - 1 >= 0 && vm.board[part.pointA.x][part.pointA.y - 1] < 1) &&
                (part.pointB.y - 1 >= 0 && vm.board[part.pointB.x][part.pointB.y - 1] < 1) &&
                (part.pointC.y - 1 >= 0 && vm.board[part.pointC.x][part.pointC.y - 1] < 1) &&
                (part.pointD.y - 1 >= 0 && vm.board[part.pointD.x][part.pointD.y - 1] < 1))
    }

    fun destroy(row: Int) {
        vm.board[row] = Array(COLUNA) { 0 }
        for (i in row downTo 1) {
            vm.board[i] = vm.board[i-1]
        }
        points += COLUNA
        txtPoints.text = "Pontos: $points"
    }

    fun getPixel(id:Int): Int {
        return  when(id) {
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

    fun gameRun() {
        //printGameBoard()
        Thread {
            while (running) {
                Thread.sleep(speed)
                runOnUiThread {
                    //limpa tela
                    for (i in 0 until LINHA) {
                        for (j in 0 until COLUNA) {
                            boardView[i][j]!!.setImageResource(getPixel(vm.board[i][j]))
                        }
                    }
                    //move peça atual
                    if (checkColisionX()) {
                        part.moveDown()
                        printPart(part.id)
                    } else {
                        printPart(part.id)
                        vm.board[part.pointA.x][part.pointA.y] = part.id
                        vm.board[part.pointB.x][part.pointB.y] = part.id
                        vm.board[part.pointC.x][part.pointC.y] = part.id
                        vm.board[part.pointD.x][part.pointD.y] = part.id
                        part = getRadomPart()
                    }

                    for (i in 0 until LINHA) {
                        var cont = 0
                        for (j in 0 until COLUNA) {
                            if(vm.board[i][j] == 0)
                                break
                            else{
                                cont++
                                if(cont === 20) {
                                    destroy(i)
                                }
                            }

                        }
                    }
                }
            }
        }.start()
    }
}
