package phoenix.delta;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Droid
{

    private float m_x;
    private float   m_y;
    private float   m_vy;
    private boolean m_isJumping;

    private float m_w;
    private float m_h;

    private Game m_game;

    private RectF m_rect;

    private int m_curFrame;
    private long m_lastFrameSwitch;
    private long m_lastUpdate;

    public Droid(Game p_game)
    {
        m_game = p_game;

        m_rect = new RectF();

        m_w = m_game.getDroidJumpImage().getWidth();
        m_h = m_game.getDroidJumpImage().getHeight();

        reset();
    }

    public void reset()
    {
        m_isJumping = false;

        m_x = Constants.START_X;
        m_y = Constants.START_Y;

        m_rect.left = m_x;
        m_rect.top = m_y;
        m_rect.bottom = m_y + m_h;
        m_rect.right = m_x + m_w;

        m_curFrame = 0;
        m_lastFrameSwitch = System.currentTimeMillis();
        m_lastUpdate = System.currentTimeMillis();
    }

    public void update()
    {
        System.out.println(m_vy + " " + m_y);
        long updateTime = System.currentTimeMillis();
        long msSinceLastUpdate = updateTime - m_lastUpdate;
        m_lastUpdate = updateTime;

        // first: handle collision detection with pastry and potholes
        doCollisionDetection();
        checkPastry2Collision();

        // handle jumping
        if (m_isJumping)
        {
            doPlayerJump(msSinceLastUpdate);
        }

        // does player want to jump?
        if (m_game.getPlayerTapFlag() && !m_isJumping)
        {
            startPlayerJump();
            m_game.getSoundPool().play(m_game.getDroidJumpSnd(), 1.0f, 1.0f, 0, 0, 1.0f);
        }

        // update animation
        long timeOnFrame = updateTime - m_lastFrameSwitch;
        if (timeOnFrame > Constants.ANIMATION_CYCLE_MS)
        {
            m_curFrame++;
            if(m_curFrame >= Constants.MAX_DROID_IMAGES)
            {
                m_curFrame = 0;
            }
            m_lastFrameSwitch = updateTime;
        }
    }

    public void draw(Canvas p_canvas)
    {
        if (m_isJumping)
        {
            p_canvas.drawBitmap(m_game.getDroidJumpImage(), m_x, m_y, m_game.getClearPaint());
        }
        else
        {
            p_canvas.drawBitmap(m_game.getDroidImages()[m_curFrame], m_x, m_y,
                                m_game.getClearPaint());
        }
    }

    //
    // helper methods for workshop - not to be implemented by participants
    //
    private void doCollisionDetection()
    {
        float ey = m_y + m_h;

        for (Pothole p : m_game.getPotholes())
        {
            if (!p.isAlive())
            {
                continue;
            }

            float lx = m_x;
            float rx = m_x + m_w;

            if ((p.getX() < lx) // am I over the pothole?
                    && ((p.getX() + p.getW()) > rx) // am I still inside the pothole?
                    && (p.getY() <= ey)) // have I fallen into the pothole?
            {
                m_game.initGameOver();
            }
        }

        // check for pastry collision
        m_rect.left = m_x;
        m_rect.top = m_y;
        m_rect.bottom = m_y + m_h;
        m_rect.right = m_x + m_w;

        if (m_game.getPastry().isAlive() && m_rect.intersect(m_game.getPastry().getRect()))
        {
            m_game.doPlayerEatPastry();
        }
    }

    public void checkPastry2Collision()
    {
        m_rect.left = m_x;
        m_rect.top = m_y;
        m_rect.bottom = m_y + m_h;
        m_rect.right = m_x + m_w;

        if (m_game.getPastry2().isAnother() && m_rect.intersect(m_game.getPastry2().getRect()))
        {
            m_game.doPlayerEatPastry2();
        }
    }

    private void doPlayerJump(long p_millisSinceLastUpdate)
    {
        float secsSinceLastUpdate = p_millisSinceLastUpdate / 1000.0f;
        System.out.println("delta m_vy: " + (secsSinceLastUpdate * Constants.DROID_FALL_ACCEL));
        m_vy += secsSinceLastUpdate * Constants.DROID_FALL_ACCEL;
        System.out.println("delta m_y: " + (secsSinceLastUpdate * m_vy));
        m_y += secsSinceLastUpdate * m_vy;
        if (m_y > Constants.START_Y)
        {
            m_y = Constants.START_Y;
            m_vy = 0;
            m_isJumping = false;
        }
    }

    private void startPlayerJump()
    {
        m_isJumping = true;
        m_game.setPlayerTapFlag(false);
        m_vy = Constants.INITIAL_JUMP_VELOCITY;
    }

    public void save(SharedPreferences.Editor map) {
        map.putFloat(Constants.DROID_X, m_x);
        map.putFloat(Constants.DROID_Y, m_y);
        map.putFloat(Constants.DROID_VY, m_vy);
        map.putBoolean(Constants.DROID_JUMPING, m_isJumping);
        map.putInt(Constants.DROID_CUR_FRAME, m_curFrame);
        map.putLong(Constants.DROID_CUR_FRAME_TIME, m_lastFrameSwitch);
    }
}
