package br.ufrn.eaj.tads.gametetris.parts

class PartL(var row:Int, var col:Int): Part(row,col) {

    init {
        pointB = Point(row + 1, col);
        pointC = Point(row + 2, col);
        pointD = Point(row + 2, col + 1);
    }

    override fun moveDown() {
        pointA.moveDown()
        pointB.moveDown()
        pointC.moveDown()
        pointD.moveDown()
    }

    override fun moveLeft() {
        pointA.moveLeft()
        pointB.moveLeft()
        pointC.moveLeft()
        pointD.moveLeft()
    }

    override fun moveRight() {
        pointA.moveRight()
        pointB.moveRight()
        pointC.moveRight()
        pointD.moveRight()
    }

    override fun rotate() {

    }

}