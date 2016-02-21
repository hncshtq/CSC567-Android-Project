package com.mycompany.final_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Tian on 2015/5/11.
 */
public class GameOver extends GameObject {
    private Animation animation = new Animation();
    private Bitmap spritesheet;


    public GameOver(Bitmap res, int x, int y, int w, int h, int numFrames)
    {
        super.x = x;
        super.y = y;

        width = w;
        height = h;

        Bitmap[] image = new Bitmap[numFrames];

        spritesheet = res;

        for(int i =0; i<image.length; i++)
        {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i *height, width, height);
        }

        animation.setFrames(image);

    }

    public void update()
    {
        animation.update();
    }


    public void draw(Canvas canvas)
    {
        try{
            canvas.drawBitmap(animation.getImage(), x,y, null);
        }catch (Exception e){}
    }


    @Override
    public int getWidth()
    {
        //offset slightly for more realistic collision detection
        return  width-10;
    }
}
