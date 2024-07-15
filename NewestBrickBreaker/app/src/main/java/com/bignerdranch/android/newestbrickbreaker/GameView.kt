package com.bignerdranch.android.newestbrickbreaker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.media.MediaPlayer
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import java.util.Random


class GameView(var context: Context) : View(context) {
    var ballX: Float
    var ballY: Float
    var velocity: Velocity = Velocity(25, 32)
    var handler: Handler = Handler()
    val UPDATE_MILLIS: Long = 30
    var runnable: Runnable = Runnable {
        invalidate()
        //handler.postDelayed(this, UPDATE_MILLIS);
    }
    var textPaint: Paint = Paint()
    var healthPaint: Paint = Paint()
    var brickPaint: Paint = Paint()
    var TEXT_SIZE: Float = 120f
    var paddleX: Float
    var paddleY: Float
    var oldX: Float = 0f
    var oldPaddleX: Float = 0f
    var points: Int = 0
    var life: Int = 3
    var ball: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.ball)
    var paddle: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.paddle)
    var dWidth: Int
    var dHeight: Int
    var ballWidth: Int
    var ballHeight: Int
    var mpHit: MediaPlayer? = MediaPlayer.create(context, R.raw.hit)
    var mpMiss: MediaPlayer? = MediaPlayer.create(context, R.raw.miss)
    var mpBreak: MediaPlayer? = MediaPlayer.create(context, R.raw.breaking)
    var random: Random
    var bricks: Array<Brick?> = arrayOfNulls<Brick>(30)
    var numBricks: Int = 0
    var brokenBricks: Int = 0
    var gameOver: Boolean = false

    init {
        textPaint.color = Color.BLACK
        textPaint.textSize = TEXT_SIZE
        textPaint.textAlign = Paint.Align.LEFT
        healthPaint.color = Color.GREEN
        brickPaint.color = Color.argb(255, 249, 129, 0)
        val display = (getContext() as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        dWidth = size.x
        dHeight = size.y
        random = Random()
        ballX = random.nextInt(dWidth - 50).toFloat()
        ballY = (dHeight / 3).toFloat()
        paddleY = ((dHeight * 4) / 5).toFloat()
        paddleX = (dWidth / 2 - paddle.width / 2).toFloat()
        ballWidth = ball.width
        ballHeight = ball.height
        createBricks()
    }

    private fun createBricks() {
        val brickWidth = dWidth / 8
        val brickHeight = dHeight / 16
        for (column in 0..7) {
            for (row in 0..2) {
                bricks[numBricks] = Brick(row, column, brickWidth, brickHeight)
                numBricks++
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        ballX += velocity.x.toFloat()
        ballY += velocity.y.toFloat()
        if ((ballX >= dWidth - ball.width) || ballX <= 0) {
            velocity.x = velocity.x * -1
        }
        if (ballY <= 0) {
            velocity.y = velocity.y * -1
        }
        if (ballY > paddleY + paddle.height) {
            ballX = (1 + random.nextInt(dWidth - ball.width - 1)).toFloat()
            ballY = (dHeight / 3).toFloat()
            if (mpMiss != null) {
                mpMiss!!.start()
            }
            velocity.x = xVelocity()
            velocity.y = 32
            life--
            if (life == 0) {
                gameOver = true
                launchGameOver()
            }
        }
        if (((ballX + ball.width) >= paddleX)
            && (ballX <= paddleX + paddle.width)
            && (ballY + ball.height >= paddleY)
            && (ballY + ball.height <= paddleY + paddle.height)
        ) {
            if (mpHit != null) {
                mpHit!!.start()
            }
            velocity.x = velocity.x + 1
            velocity.y = (velocity.y + 1) * -1
        }
        canvas.drawBitmap(ball, ballX, ballY, null)
        canvas.drawBitmap(paddle, paddleX, paddleY, null)
        for (i in 0 until numBricks) {
            if (bricks[i].getVisibility()) {
                canvas.drawRect(
                    bricks[i].column * bricks[i].width + 1,
                    bricks[i].row * bricks[i].height + 1,
                    bricks[i].column * bricks[i].width + bricks[i].width - 1,
                    bricks[i].row * bricks[i].height + bricks[i].height - 1,
                    brickPaint
                )
            }
        }
        canvas.drawText("" + points, 20f, TEXT_SIZE, textPaint)
        if (life == 2) {
            healthPaint.color = Color.YELLOW
        } else if (life == 1) {
            healthPaint.color = Color.RED
        }
        canvas.drawRect(
            (dWidth - 200).toFloat(),
            30f,
            (dWidth - 200 + 60 * life).toFloat(),
            80f,
            healthPaint
        )
        for (i in 0 until numBricks) {
            if (bricks[i].getVisibility()) {
                if (ballX + ballWidth >= bricks[i].column * bricks[i].width && ballX <= bricks[i].column * bricks[i].width + bricks[i].width && ballY <= bricks[i].row * bricks[i].height + bricks[i].height && ballY >= bricks[i].row * bricks[i].height) {
                    if (mpBreak != null) {
                        mpBreak!!.start()
                    }
                    velocity.y = (velocity.y + 1) * -1
                    bricks[i].setInvisible()
                    points += 10
                    brokenBricks++
                    if (brokenBricks == 24) {
                        launchGameOver()
                    }
                }
            }
        }
        if (brokenBricks == numBricks) {
            gameOver = true
        }
        if (!gameOver) {
            handler.postDelayed(runnable, UPDATE_MILLIS)
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x
        val touchY = event.y
        if (touchY >= paddleY) {
            val action = event.action
            if (action == MotionEvent.ACTION_DOWN) {
                oldX = event.x
                oldPaddleX = paddleX
            }
            if (action == MotionEvent.ACTION_MOVE) {
                val shift = oldX - touchX
                val newPaddleX = oldPaddleX - shift
                paddleX = if (newPaddleX <= 0) 0f
                else if (newPaddleX >= dWidth - paddle.width) (dWidth - paddle.width).toFloat()
                else newPaddleX
            }
        }
        return true
    }

    private fun launchGameOver() {
        handler.removeCallbacksAndMessages(null)
        val intent = Intent(context, GameOver::class.java)
        intent.putExtra("points", points)
        context.startActivity(intent)
        (context as Activity).finish()
    }

    private fun xVelocity(): Int {
        val values = intArrayOf(-35, -30, -25, 25, 30, 35)
        val index = random.nextInt(6)
        return values[index]
    }
}