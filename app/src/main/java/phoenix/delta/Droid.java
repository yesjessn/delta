package phoenix.delta;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Droid {

    float x;
    float y;
    float vy;
    boolean jumping;
    boolean falling;

    float w;
    float h;

    final float startX = 530.0f;
    final float startY;
    final float initialVelocity = 30.0f;

    Game game;

    //
    // workshop 2
    //

    RectF rect;

    int curFrame;
    long curFrameTime = 0;

    // -- END workshop 2

    public Droid(Game game) {
        this.game = game;

        //
        // workshop 2
        //
        this.rect = new RectF();

        w = game.droidJumpImage.getWidth();
        h = game.droidJumpImage.getHeight();
        startY = game.groundY + h;

        // -- END workshop 2

        reset();
    }

    public void reset() {

        jumping = false;
        falling = false;

        x = startX;
        y = startY;

        rect.left = x;
        rect.top = y;
        rect.bottom = y + h;
        rect.right = x + w;

        curFrame = 0;
        curFrameTime = System.currentTimeMillis();
    }

    public void update() {

        //
        // first: handle collision detection with pastry and potholes
        //
        doCollisionDetection();
        checkPastry2Collision();

        //
        // handle falling
        //
        if (falling) {
            doPlayerFall();
        }

        //
        // handle jumping
        //
        if (jumping) {
            doPlayerJump();
        }

        //
        // does player want to jump?
        //
        if (game.playerTap && !jumping && !falling) {
            startPlayerJump();
            game.soundPool.play(game.droidJumpSnd, 1.0f, 1.0f, 0, 0, 1.0f);
        }

        //
        // workshop 2
        //

        //
        // update animation
        //
        long now = System.currentTimeMillis() - curFrameTime;
        if (now > 250) {
            curFrame++;
            if (curFrame > 3) {
                curFrame = 1;
            }
            curFrameTime = System.currentTimeMillis();
        }

        // -- END workshop 2
    }

    public void draw(Canvas canvas) {
        //canvas.drawRect(x, y, x + w, y + h, game.greenPaint);

        //
        // workshop 2
        //

        if (jumping || falling) {
            canvas.drawBitmap(game.droidJumpImage, x, y, game.clearPaint);
        }
        else {
            canvas.drawBitmap(game.droidImages[curFrame], x, y, game.clearPaint);
        }

        // -- END workshop 2
    }

    //
    // helper methods for workshop - not to be implemented by participants
    //
    private void doCollisionDetection() {

        float ey = y + h;

        for (Pothole p : game.potholes) {
            if (!p.alive) {
                continue;
            }

            float lx = x;
            float rx = x + w;

            if (
                // am I over the pothole?
                    (p.x < lx)

                            // am I still inside the pothole?
                            && ((p.x + p.w) > rx)

                            // have I fallen into the pothole?
                            && (p.y <= ey)

                    ) {

                game.initGameOver();
            }
        }

        //
        // workshop 2
        //

        //
        // check for pastry collision
        //
        rect.left = x;
        rect.top = y;
        rect.bottom = y + h;
        rect.right = x + w;

        if (game.pastry.alive && rect.intersect(game.pastry.rect)) {
            game.doPlayerEatPastry();
        }

        // -- END workshop 2
    }

    public void checkPastry2Collision() {
        rect.left = x;
        rect.top = y;
        rect.bottom = y + h;
        rect.right = x + w;

        if (game.pastry2.another && rect.intersect(game.pastry2.rect)) {
            game.doPlayerEatPastry2();
        }
    }

    private void doPlayerFall() {
        System.out.println("falling {y:" + y + ", vy:" + vy + ")");
        vy += 0.1f;
        y += vy;
        if (y > startY) {
            System.out.printf("hitting ground (%f) at (y: %f, vy: %f) with height: %f; startY: %f", game.groundY, y, vy, h, startY);
            y = startY;
            //falling = false;

            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            falling = false;

                        }
                    },
                    35
            );
        }
    }

    private void doPlayerJump() {
        System.out.println("jumping {y:" + y + ", vy:" + vy + ")");
        y -= vy;
        vy -= 2.0f;
        if (vy <= 0.0f) {
            jumping = false;
            falling = true;
        }
    }

    private void startPlayerJump() {
        jumping = true;
        game.playerTap = false;
        vy = initialVelocity;
    }

    //
    // workshop 2
    //

    public void restore(SharedPreferences savedState) {
        x = savedState.getFloat("droid_x", 0);
        y = savedState.getFloat("droid_y", 0);
        vy = savedState.getFloat("droid_vy", 0);
        jumping = savedState.getBoolean("droid_jumping", false);
        falling = savedState.getBoolean("droid_falling", false);
        curFrame = savedState.getInt("droid_curFrame", 0);
        curFrameTime = savedState.getLong("droid_curFrameTime",0 );
    }

    public void save(SharedPreferences.Editor map) {
        map.putFloat("droid_x", x);
        map.putFloat("droid_y", y);
        map.putFloat("droid_vy", vy);
        map.putBoolean("droid_jumping", jumping);
        map.putBoolean("droid_falling", falling);
        map.putInt("droid_curFrame", curFrame);
        map.putLong("droid_curFrameTime", curFrameTime);
    }

    // -- END workshop 2
}
