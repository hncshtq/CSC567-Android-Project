package com.mycompany.final_project;


import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;


public class GamePanel extends SurfaceView implements SurfaceHolder.Callback
{
    public static final int WIDTH = 856;
    public static final int HEIGHT = 480;
    public static final int MOVESPEED = -5;
    private long smokeStartTime;
    private long bulletStartTime;
    private long missileStartTime;

    private MainThread thread;
    private Background bg;
    private Player player;
    private Tank  tank;
    private GameOver lose;
    private int local;

    private ArrayList<Smokepuff> smoke;
    private ArrayList<Missile> missiles;
    private ArrayList<Enermy> enermy;
    private ArrayList<MyBullet> myBullets;
    private ArrayList<EnermyBullet> enermyBullets;

    private GameButton myButton1;
    private GameButton myButton2;
    private GameButton myButton3;


    private Random rand = new Random();
    private boolean newGameCreated;


    private Explosion explosion;
    private ArrayList<Explosion> enermy_explosion;
    private long startReset;
    private boolean reset;
    private boolean disappear;
    private boolean started;
    private boolean firstTime;
    private boolean bullet;
    private int best;




    public GamePanel(Context context)
    {
        super(context);


        //add the callback to the surfaceholder to intercept events
        getHolder().addCallback(this);

        //make gamePanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){}

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        boolean retry = true;
        int counter = 0;
        while(retry && counter<1000)
        {
            counter++;
            try{thread.setRunning(false);
                thread.join();
                retry = false;
                thread = null;

            }catch(InterruptedException e){e.printStackTrace();}

        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder){

        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.grassbg1));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.helicopter), 65, 25, 3);
        tank = new Tank(BitmapFactory.decodeResource(getResources(), R.drawable.tank2),WIDTH+10,HEIGHT-80, 75,60);
        smoke = new ArrayList<Smokepuff>();
        missiles = new ArrayList<Missile>();
        enermy = new ArrayList<Enermy>();
        enermy_explosion = new ArrayList<Explosion>();

        myBullets = new ArrayList<MyBullet>();
        enermyBullets = new ArrayList<EnermyBullet>();

        myButton1 = new GameButton(WIDTH-50,HEIGHT-150);
        myButton2 = new GameButton(WIDTH-50,HEIGHT-50);
        myButton3 = new GameButton(150,HEIGHT-50);



        smokeStartTime=  System.nanoTime();
        missileStartTime = System.nanoTime();
        bulletStartTime = System.nanoTime();

        firstTime = true;
        bullet = true;
        lose = new GameOver(BitmapFactory.decodeResource(getResources(), R.drawable.lost),WIDTH/2,HEIGHT/2,265,95,11);



        thread = new MainThread(getHolder(), this);



        //we can safely start the game loop
        thread.setRunning(true);
        thread.start();

    }
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int x = (int)event.getX();
        int y = (int)event.getY();
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(firstTime) {
                firstTime = false;
            }
            if(!player.getPlaying() && newGameCreated && reset)
            {
                disappear = false;
                player.setPlaying(true);
                player.setUp(true);
            }
            if(player.getPlaying())
            {
                if(!started)started = true;
                reset = false;

                if(y>= getHeight()-400-80 && y <=getHeight()-400+80 && x>= getWidth()-153-80 && x<= getWidth()-153+80)
                    player.setUp(true);
                if(y>= getHeight()-170-80 && y <=getHeight()-170+80 && x>= getWidth()-153-80 && x<= getWidth()-153+80)
                    player.setDown(true);
                if(y>= getHeight()-170-80 && y <=getHeight()-170+80 && x>= 250-80 && x<= 250+80){
                    if(bullet){
                        myBullets.add(new MyBullet(player.getX()+40, player.getY()+15));
                        bullet = false;
                    }
                }



            }
            return true;
        }
        if(event.getAction()==MotionEvent.ACTION_UP)
        {

            player.setUp(false);
            player.setDown(false);
            return true;
        }

        return super.onTouchEvent(event);
    }

    public void newGame()
    {
        missiles.clear();
        enermy.clear();
        smoke.clear();
        tank.clear();
        myBullets.clear();
        enermyBullets.clear();
        enermy_explosion.clear();
        bullet = true;

        player.setY(HEIGHT/2);
        local = player.getScore() * 3;
        if(player.getScore()*3 > best)
        {
            best = player.getScore()*3;
        }
        player.resetScore();

        newGameCreated = true;
    }

    public void update()

    {
        if(player.getPlaying()) {
            bg.update();
            player.update();
            tank.update();
            lose.update();



            //add missiles on timer
            long missileElapsed = (System.nanoTime()-missileStartTime)/1000000;
            if(missileElapsed >(2000 - player.getScore()/4)){

                //first missile always goes down the middle
                if(missiles.size()==0)
                {
                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.
                            missile),WIDTH + 10, HEIGHT/2, 45, 15, player.getScore(), 13));
                }
                else
                {

                    missiles.add(new Missile(BitmapFactory.decodeResource(getResources(),R.drawable.missile),
                            WIDTH+10, (int)(rand.nextDouble()*(HEIGHT - 65)),45,15, player.getScore(),13));
                }

                //enermy
                if(enermy.size()==0)
                {
                    enermy.add(new Enermy(BitmapFactory.decodeResource(getResources(),R.drawable.
                            enermy),WIDTH + 10, 0, 104, 50, player.getScore(), 4));
                }
                else
                {

                    enermy.add(new Enermy(BitmapFactory.decodeResource(getResources(),R.drawable.enermy),
                            WIDTH+10, (int)(rand.nextDouble()*(HEIGHT - 65)),104, 50, player.getScore(),4));
                }

                //reset timer
                missileStartTime = System.nanoTime();
            }

            //update enermy bullet
            long elapsed_bullet = (System.nanoTime() - bulletStartTime)/1000000;
            if(elapsed_bullet > 500) {
                for (int j = 0; j < (int)(rand.nextDouble()*enermy.size()); j++) {
                    enermyBullets.add(new EnermyBullet(enermy.get(j).getX(), enermy.get(j).getY()+20,enermy.get(j).getSpeed()+10));
                }
                bulletStartTime = System.nanoTime();
            }

            for(int i = 0; i < enermyBullets.size(); i ++){
                enermyBullets.get(i).update();
                if(collision(enermyBullets.get(i),player))
                {
                    enermyBullets.remove(i);
                    player.setPlaying(false);
                    break;
                }
                if(enermyBullets.get(i).getX()<-10)
                {
                    enermyBullets.remove(i);
                    break;
                }
            }

            //loop through every missile and check collision and remove
            for(int i = 0; i<missiles.size();i++)
            {
                //update missile
                missiles.get(i).update();

                if(collision(missiles.get(i),player))
                {
                    missiles.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove missile if it is way off the screen
                if(missiles.get(i).getX()<-100)
                {
                    missiles.remove(i);
                    break;
                }
            }

            //update enermy plane
            for(int i = 0; i<enermy.size();i++)
            {
                enermy.get(i).update();

                if(collision(enermy.get(i),player))
                {
                    enermy.remove(i);
                    player.setPlaying(false);
                    break;
                }
                //remove missile if it is way off the screen
                if(enermy.get(i).getX()<-100)
                {
                    enermy.remove(i);
                    break;
                }
            }


            for(int i = 0; i<myBullets.size();i++) {
                for (int j = 0; j < enermy.size(); j++) {
                    //update missile
                    myBullets.get(i).update();

                    if (collision(myBullets.get(i), enermy.get(j))) {
                        //myBullets.remove(i);
                        enermy_explosion.add(new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion), enermy.get(j).getX(),
                                enermy.get(j).getY()-30, 100, 100,25));

                        enermy.remove(j);
                        myBullets.remove(i);
                        bullet = true;

                    }

                    if(myBullets.get(i).getX() > WIDTH)
                    {
                        myBullets.remove(i);
                        bullet = true;
                    }
                }
            }

            for(int i = 0; i < enermy_explosion.size(); i++)
            {
                enermy_explosion.get(i).update();
            }


            //check the hit or not the tank
            if(collision(tank,player))
            {
                player.setPlaying(false);

            }



            //add smoke puffs on timer
            long elapsed_smoke = (System.nanoTime() - smokeStartTime)/1000000;
            if(elapsed_smoke > 120) {
                smoke.add(new Smokepuff(player.getX(), player.getY() + 10));
                smokeStartTime = System.nanoTime();
            }

            for(int i = 0; i<smoke.size();i++)
            {
                smoke.get(i).update();
                if(smoke.get(i).getX()<-10)
                {
                    smoke.remove(i);
                }
            }


        }
        else
        {
            if(!reset)
            {
                newGameCreated = false;
                startReset =System.nanoTime();
                reset = true;
                disappear = true;
                explosion = new Explosion(BitmapFactory.decodeResource(getResources(),R.drawable.explosion), player.getX(),
                        player.getY()-30, 100, 100,25);
            }

            explosion.update();
            long resetElapsed = (System.nanoTime() - startReset) / 1000000;

            if(resetElapsed > 2500 && !newGameCreated)
            {
                newGame();
            }

        }
    }



    public boolean collision(GameObject a, GameObject b)
    {
        if(Rect.intersects(a.getRectangle(),b.getRectangle()))
        {
            return true;
        }
        return false;
    }
    @Override
    public void draw(Canvas canvas)
    {
        final float scaleFactorX = getWidth()/(WIDTH*1.f);
        final float scaleFactorY = getHeight()/(HEIGHT*1.f);

        if(canvas!=null) {
            final int savedState = canvas.save();



            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            if(!disappear){
                player.draw(canvas);
                myButton1.draw(canvas);
                myButton2.draw(canvas);
                myButton3.draw(canvas);
            }
            tank.draw(canvas);


            //draw smokepuffs
            for(Smokepuff sp: smoke)
            {
                sp.draw(canvas);
            }
            //draw missiles
            for(Missile m: missiles)
            {
                m.draw(canvas);
            }

            for(Enermy e: enermy)
            {
                e.draw(canvas);
            }

            for(MyBullet mb: myBullets)
            {
                mb.draw(canvas);
            }

            for(EnermyBullet eb:enermyBullets)
            {
                eb.draw(canvas);
            }

            for(Explosion ex:enermy_explosion)
            {
                ex.draw(canvas);
            }


            if(started)
            {
                explosion.draw(canvas);
            }



            drawText(canvas);



            canvas.restoreToCount(savedState);
        }
    }





    public void drawText(Canvas canvas)
    {
        if(player.getPlaying())
        {
            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setTextSize(30);
            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("DISTANCE: " + (player.getScore() * 3), 10, HEIGHT - 10, paint);
            canvas.drawText("BEST: " + best, WIDTH - 215, HEIGHT - 10, paint);
        }

        if(firstTime)
        {
            disappear = false;
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("PRESS TO START", WIDTH/2-150, HEIGHT/2, paint1);

            paint1.setTextSize(20);
            canvas.drawText("PRESS RIGHT_UPPER BUTTON TO GO UP", WIDTH/2-150, HEIGHT/2 +20, paint1);
            canvas.drawText("PRESS RIGHT_LOWER BUTTON TO GO BOTTOM", WIDTH/2-150, HEIGHT/2+40, paint1);
            canvas.drawText("PRESS LEFT BUTTON TO SHOOT", WIDTH/2-150, HEIGHT/2+60, paint1);
            myButton1.draw(canvas);
            myButton2.draw(canvas);
            myButton3.draw(canvas);

        }

        if(!player.getPlaying() && newGameCreated && reset&&!firstTime)
        {
            disappear = true;
            Paint paint1 = new Paint();
            paint1.setTextSize(40);
            paint1.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            canvas.drawText("GAME OVER", WIDTH/2-125, HEIGHT/2-20, paint1);
            paint1.setTextSize(80);
            canvas.drawText(""+local, WIDTH/2-125,HEIGHT/2 +80, paint1);
            paint1.setTextSize(20);
            canvas.drawText("BEST: " + best,WIDTH/2-125, HEIGHT/2 + 120, paint1);
            paint1.setTextSize(20);
            canvas.drawText("PRESS TO STAR", WIDTH/2-125, HEIGHT/2 +140, paint1);


        }
    }








}