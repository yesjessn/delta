package droidrunjump;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;


public class Road
{
    private Bitmap image;
    private float x, y, dx;
    private Game m_game;

    public Road(Game p_game)
    {
        m_game = p_game;
        image = m_game.getBackgroundImage();
        dx = -DroidConstants.MOVEMENT_RATE;
        x = 0;
        y = 0;
    }
    public void update()
    {
        //background moves across screen
        x += dx;
        //resets background to original spot
        if(x<-image.getWidth()){
            x = 0;
        }
    }
    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(image, x, y, null);
        //when x is not in starting position...
        if(x<0)
        {
            //...puts new background right after the old one
            canvas.drawBitmap(image, x + image.getWidth(), y, null);
        }
    }


    public void save(SharedPreferences.Editor p_map)
    {
        p_map.putFloat("road_x", x);
    }

    public void reset() {
        x = 0;
    }
}
