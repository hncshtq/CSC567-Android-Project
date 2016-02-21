package com.mycompany.final_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Tian on 2015/5/11.
 */
public class MyBullet extends GameObject{
    public int r;
    public int s;
    public MyBullet(int x, int y)
    {
        r = 5;
        s = 5;
        super.x = x;
        super.y = y;
    }

    public void update()
    {
        x = x + s;
    }

    public void draw(Canvas canvas)
    {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setAlpha(100);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(x-r, y-r, r, paint);
    }

}
