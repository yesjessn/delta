package droidrunjump;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.util.Log;


public class Pothole {

    private float x, y;
    private float w, h;
    private boolean alive;

    private Game game;

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public float getW()
    {
        return w;
    }

    public float getH()
    {
        return h;
    }

    public boolean isAlive()
    {
        return alive;
    }

    public void reset()
    {
        alive = false;
    }

    public void spawn(float xOffset) {

        //
        // spawn a pothole starting beyond right side of the display
        // apply additional xOffset and vary the width of the pothole
        //

        w = DroidConstants.MIN_POTHOLE_WIDTH;
        x = game.getBackgroundImage().getWidth() + xOffset;
        Log.i("DRJ", "Spawning pothole at " + x);
        alive = true;
    }

    public void update() {

        //
        // potholes move from right to left
        //

        x -= DroidConstants.MOVEMENT_RATE;


        //
        // if pothole beyond left hand side of display then disable it
        //
        if (x < -w) {
            alive = false;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(x, y, x + w, y + h, game.getClearPaint());
    }

    //
    // workshop 2 code
    //

    int id;

    public Pothole(int id, Game game) {
        this.id = id;
        this.game = game;
        int raisedPx = 4; // Raise the pothole up a little off the ground to cover the background
        y = DroidConstants.GROUND_Y - raisedPx;
        h = game.getBackgroundImage().getHeight() - y + raisedPx;
        alive = false;
    }

    public void restore(SharedPreferences savedState) {
        x = savedState.getFloat("ph_" + id + "_x", 0);
        y = savedState.getFloat("ph_" + id + "_y", 0);
        w = savedState.getFloat("ph_" + id + "_w", 0);
        h = savedState.getFloat("ph_" + id + "_h", 0);
        alive = savedState.getBoolean("ph_" + id + "_alive", false);
    }

    public void save(SharedPreferences.Editor map) {
        map.putFloat("ph_" + id + "_x", x);
        map.putFloat("ph_" + id + "_y", y);
        map.putFloat("ph_" + id + "_w", w);
        map.putFloat("ph_" + id + "_h", h);
        map.putBoolean("ph_" + id + "_alive", alive);
    }
}

