package com.bignerdranch.android.newestbrickbreaker

class Brick(var row: Int, var column: Int, var width: Int, var height: Int) {
    var visibility: Boolean = true
    fun setInvisible() {
        visibility = false
    }
}