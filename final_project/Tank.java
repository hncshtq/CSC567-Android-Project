package com.mycompany.final_project;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by Tian on 2015/5/11.
 */
public class Tank extends GameObject {

    private Bitmap image;

    public Tank(Bitmap res,int x, int y, int w, int h)
    {
        image = res;
        super.x = x;
        super.y = y;
        width = w;
        height = h;

    }

    public void update()
    {
        x -= 5;
        if(x < 0)
        {
            clear();
        }
    }

    public void clear()
    {
        x = GamePanel.WIDTH + 10;
    }



    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
    }
}
