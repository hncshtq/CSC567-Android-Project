package com.mycompany.final_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Tian on 2015/5/11.
 */
public class EnermyBullet extends GameObject{
    public int r;
    public int s;
    public EnermyBullet(int x, int y,int speed)
    {
        r = 5;
        s = speed;
        super.x = x;
        super.y = y;
    }

    public void update()
    {
        x = x - s;
    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x-r, y-r, r, paint);
    }

}
