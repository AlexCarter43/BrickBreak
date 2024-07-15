package com.bignerdranch.android.newestbrickbreaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    var txt1: TextView? = null
    var txt2: TextView? = null
    var startbtn: ImageButton? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        txt1 = findViewById<TextView>(R.id.txt1)
        txt2 = findViewById<TextView>(R.id.txt2)
        startbtn = findViewById<ImageButton>(R.id.startbtn)

        val myanimation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.animation2)
        txt1.startAnimation(myanimation)
        val myanimatione = AnimationUtils.loadAnimation(this@MainActivity, R.anim.animation1)
        txt2.startAnimation(myanimatione)
    }

    fun startGame(view: View?) {
        val btn = AnimationUtils.loadAnimation(this@MainActivity, R.anim.animation6)
        startbtn!!.startAnimation(btn)
        val gameView: GameView = GameView(this)
        setContentView(gameView)
    }
}