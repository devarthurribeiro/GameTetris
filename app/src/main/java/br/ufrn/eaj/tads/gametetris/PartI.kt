package br.ufrn.eaj.tads.gametetris

class PartI(var row:Int, var col:Int):Part(row,col) {

    init {
        pointB = Point(row, col-1);
        pointC = Point(row, col+1);
        pointD = Point(row,col+2);
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