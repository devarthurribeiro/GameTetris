package br.ufrn.eaj.tads.gametetris.parts

abstract class Part(var x:Int, var y:Int) {

    var pointA = Point(x, y)
    var rotated = false
    lateinit var pointB: Point
    lateinit var pointC: Point
    lateinit var pointD: Point

    abstract fun moveDown()
    abstract fun moveLeft()
    abstract fun moveRight()
    abstract fun rotate()
}