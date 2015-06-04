package phoenix.delta;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Pastry {

    float x, y;
    float w, h;
    boolean alive;
    boolean another;

    Game game;
    RectF rect;

    public Pastry(Game game) {
        this.game = game;

        w = game.pastryImage.getWidth();
        h = game.pastryImage.getHeight();

        rect = new RectF();
    }

    public void reset() {
        alive = false;
    }

    public void reset2() {
        another = false;
    }

    public void spawn() {
        x = game.width + w;
        y = game.groundY - h + game.random(h - 40, 80.0f);
        rect.top = y;
        rect.bottom = y + h;
        alive = true;
    }

    public void spawn2() {
        x = game.width + w + game.random(170.0f, 250.0f);
        y = game.groundY - h + game.random(h - 40, 80.0f);
        rect.top = y;
        rect.bottom = y + h;
        another = true;
    }

    public void update() {

        //
        // pastry move from right to left
        //

        x -= 7.0f;
        rect.left = x;
        rect.right = x + w;

        //
        // if pastry beyond left hand side of display then disable it
        //
        if (x < -w) {
            alive = false;
        }
    }

    public void update2() {
        //
        // pastry move from right to left
        //

        x -= 7.0f;
        rect.left = x;
        rect.right = x + w;

        //
        // if pastry beyond left hand side of display then disable it
        //
        if (x < -w)
            another = false;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(game.pastryImage, rect.left, rect.top, game.clearPaint);
    }

    public void restore(SharedPreferences savedState) {
        x = savedState.getFloat("ps_x", 0);
        y = savedState.getFloat("ps_y", 0);
        w = savedState.getFloat("ps_w", 0);
        h = savedState.getFloat("ps_h", 0);
        alive = savedState.getBoolean("ps_alive", false);
        another = savedState.getBoolean("ps_another", false);
    }

    public void save(SharedPreferences.Editor map) {
        map.putFloat("ps_x", x);
        map.putFloat("ps_y", y);
        map.putFloat("ps_w", w);
        map.putFloat("ps_h", h);
        map.putBoolean("ps_alive", alive);
        map.putBoolean("ps_another", another);
    }
}

