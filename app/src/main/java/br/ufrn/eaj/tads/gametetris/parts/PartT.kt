package br.ufrn.eaj.tads.gametetris.parts

class PartT (var row:Int, var col:Int): Part(row,col) {

    init {
        id = 1
        pointB = Point(row, col - 1);
        pointC = Point(row, col + 1);
        pointD = Point(row + 1, col);
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
        if(!rotated) {
            pointB.x--
            pointB.y++

            pointC.x++
            pointC.y--

            pointD.x--
            pointD.y--

            rotated = true
        } else {
            pointB.x++
            pointB.y--

            pointC.x--
            pointC.y++

            pointD.y++
            pointD.x++

            rotated = false
        }
    }
}