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
    var speed:Long = 300

    var pt = Point(0,15)


    var board = Array(LINHA) {
        Array(COLUNA){0}
    }

    var boardView = Array(LINHA){
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
                boardView[i][j] = inflater.inflate(R.layout.inflate_image_view, gridboard, false) as ImageView
                gridboard.addView( boardView[i][j])
            }
        }

        btnLeft.setOnClickListener {
            if(pt.y-1 >= 0) {
                pt.moveLeft()
            }
        }

        btnRigth.setOnClickListener {
            if(pt.y+1 < COLUNA) {
                pt.moveRight()

            }
        }

        btnDown.setOnClickListener {
            if(pt.x+1 < LINHA && board[pt.x+1][pt.y] != 1) {
                pt.moveDown()
            }
        }

        gameRun()
    }

    fun printGameBoard() {
        for (j in 1 until COLUNA) {
            board[LINHA-1][j] = 1
        }
    }

    fun gameRun(){
        printGameBoard()
        Thread{
            while(running){
                Thread.sleep(speed)
                runOnUiThread{
                    //limpa tela
                    for (i in 0 until LINHA) {
                        for (j in 0 until COLUNA) {
                            when(board[i][j]) {
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
                    if(pt.x+1 < LINHA && board[pt.x+1][pt.y] != 1) {
                        pt.moveDown()
                        boardView[pt.x][pt.y]!!.setImageResource(R.drawable.white)
                    }else{
                        boardView[pt.x][pt.y]!!.setImageResource(R.drawable.white)
                        board[pt.x][pt.y] = 1
                        pt.x = 0
                        pt.y = COLUNA/2
                    }

                }
            }
        }.start()
    }
}
