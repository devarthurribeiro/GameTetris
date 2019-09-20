package br.ufrn.eaj.tads.gametetris

import android.widget.ImageView
import androidx.lifecycle.ViewModel

class BoardViewModel : ViewModel() {
    val LINHA = 36
    val COLUNA = 20

    var boardView = Array(LINHA) {
        arrayOfNulls<ImageView>(COLUNA)
    }
}