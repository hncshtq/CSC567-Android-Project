package com.mycompany.final_project;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Tian on 2015/5/13.
 */
public class GameButton extends GameObject{
        public int r;
        public int c;
        public GameButton(int x, int y)
        {
            r = 30;
            super.x = x;
            super.y = y;
        }

        public void draw(Canvas canvas)
        {
            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            paint.setAlpha(50);
            paint.setStyle(Paint.Style.FILL);
            //WIDTH-50,HEIGHT-50
            canvas.drawCircle(x-r, y-r, r, paint);
        }

}
