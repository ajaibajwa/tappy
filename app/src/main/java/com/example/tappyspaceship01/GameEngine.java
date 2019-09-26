package com.example.tappyspaceship01;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    // Android debug variables
    final static String TAG="TAPPY-SPACESHIP";

    // screen size
    int screenHeight;
    int screenWidth;

    int playerX;
    int playerY;

    int enemyX;
    int enemyY;

    Rect playerHitBox;
    Rect enemyHitBox;

    Bitmap playerImage;
    Bitmap enemyImage;

    // game state
    boolean gameIsRunning;

    // threading
    Thread gameThread;


    // drawing variables
    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;

    String fingerAction = "";




    // -----------------------------------
    // GAME SPECIFIC VARIABLES
    // -----------------------------------

    // ----------------------------
    // ## SPRITES
    // ----------------------------


    // ----------------------------
    // ## GAME STATS
    // ----------------------------

    public GameEngine(Context context, int w, int h) {
        super(context);


        this.holder = this.getHolder();
        this.paintbrush = new Paint();

        this.screenWidth = w;
        this.screenHeight = h;

        this.playerX = 100;
        this.playerY = 120;
         this.enemyX = this.screenWidth;
         this.enemyY = 120;

        this.playerImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.player_ship);


         this.playerHitBox = new Rect(this.playerX,this.playerY,this.playerX+playerImage.getWidth(),this.playerY+playerImage.getHeight());
         this.enemyImage = BitmapFactory.decodeResource(this.getContext().getResources(),
                R.drawable.alien_ship2);
         this.enemyHitBox = new Rect(this.enemyX,this.enemyY,this.enemyX+this.enemyImage.getWidth(),this.enemyY+this.enemyImage.getHeight());
        this.printScreenInfo();

        // @TODO: Add your sprites
        // @TODO: Any other game setup

    }


    private void printScreenInfo() {

        Log.d(TAG, "Screen (w, h) = " + this.screenWidth + "," + this.screenHeight);
    }

    private void spawnPlayer() {
        //@TODO: Start the player at the left side of screen
    }
    private void spawnEnemyShips() {
        Random random = new Random();

        //@TODO: Place the enemies in a random location

    }

    // ------------------------------
    // GAME STATE FUNCTIONS (run, stop, start)
    // ------------------------------
    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();
            this.setFPS();
        }
    }


    public void pauseGame() {
        gameIsRunning = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    public void startGame() {
        gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();
    }


    // ------------------------------
    // GAME ENGINE FUNCTIONS
    // - update, draw, setFPS
    // ------------------------------



    public void updatePositions() {
        // @TODO: Update position of player based on Mouse tap
        if(fingerAction=="tapped"){
            this.playerY = this.playerY - 50;
        }
        else if((fingerAction == "untapped")&&(playerY<screenHeight-300)){
            this.playerY = this.playerY +20;

        }
        this.enemyHitBox.left = this.enemyX;
        this.enemyHitBox.top = this.enemyY;
        this.enemyHitBox.right = this.enemyX + this.enemyImage.getWidth();
        this.enemyHitBox.bottom =  this.enemyY + this.enemyImage.getHeight();

        this.enemyX = this.enemyX-20;
        if(this.enemyX<=(0-this.enemyImage.getWidth())){
            this.enemyX = this.screenWidth;
            this.enemyY = 120;
        }


    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            //----------------

            // configure the drawing tools
            this.canvas.drawColor(Color.argb(255,255,255,255));
            paintbrush.setColor(Color.WHITE);



            // DRAW THE PLAYER HITBOX
            // ------------------------
            // 1. change the paintbrush settings so we can see the hitbox
            paintbrush.setColor(Color.BLUE);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);

            //draw playerImage
            canvas.drawBitmap(playerImage, this.playerX, this.playerY, paintbrush);
            //draw player hit box
            canvas.drawRect(playerHitBox,paintbrush);

            canvas.drawRect(enemyHitBox,paintbrush);


            canvas.drawBitmap(enemyImage, this.enemyX, this.enemyY, paintbrush);
            //----------------
            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    public void setFPS() {
        try {
            gameThread.sleep(100);
        }
        catch (Exception e) {

        }
    }

    // ------------------------------
    // USER INPUT FUNCTIONS
    // ------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?
        if (userAction == MotionEvent.ACTION_DOWN) {
            Log.d(TAG, "Person tapped the screen"); //player must move up.
            fingerAction = "tapped";

        }
        else if (userAction == MotionEvent.ACTION_UP) {
            Log.d(TAG, "Person lifted finger"); //player must move down.
            fingerAction = "untapped";
        }

        return true;
    }
}
