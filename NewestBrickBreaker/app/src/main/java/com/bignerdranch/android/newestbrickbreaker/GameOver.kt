package com.bignerdranch.android.newestbrickbreaker

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class GameOver : AppCompatActivity() {
    var tvPoints: TextView? = null
    var imgBtn1: ImageButton? = null
    var imgBtn2: ImageButton? = null
    var ivNewHighest: ImageView? = null
    var ivLooser: ImageView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game_over)
        imgBtn1 = findViewById<ImageButton>(R.id.imgBtn1)
        imgBtn2 = findViewById<ImageButton>(R.id.imgBtn2)


        ivNewHighest = findViewById<ImageView>(R.id.ivNewHeighest)
        ivLooser = findViewById<ImageView>(R.id.ivLooser)
        tvPoints = findViewById<TextView>(R.id.tvPoints)
        val points = intent.extras!!.getInt("points")
        if (points == 240) {
            ivNewHighest.setVisibility(View.VISIBLE)
            val anim = AnimationUtils.loadAnimation(this@GameOver, R.xml.animation6)
            ivNewHighest.startAnimation(anim)
        } else {
            ivLooser.setVisibility(View.VISIBLE)
            val anima = AnimationUtils.loadAnimation(this@GameOver, R.xml.animation6)
            ivLooser.startAnimation(anima)
        }
        tvPoints.setText("" + points)
    }

    fun restart(view: View?) {
        val imageanim = AnimationUtils.loadAnimation(this@GameOver, R.anim.animation5)
        imgBtn1!!.startAnimation(imageanim)
        val intent = Intent(
            this@GameOver,
            MainActivity::class.java
        )
        startActivity(intent)
        finish()
    }

    fun exit(view: View?) {
        val imageanime = AnimationUtils.loadAnimation(this@GameOver, R.anim.animation5)
        imgBtn2!!.startAnimation(imageanime)
        finish()
    }
}