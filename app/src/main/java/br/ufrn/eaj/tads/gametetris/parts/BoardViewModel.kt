package br.ufrn.eaj.tads.gametetris.parts

import androidx.lifecycle.ViewModel

class BoardViewModel : ViewModel() {
    val LINHA = 36
    val COLUNA = 20
    var board = Array(LINHA) {
        Array(COLUNA) { 0 }
    }
}