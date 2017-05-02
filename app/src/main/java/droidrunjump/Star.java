package droidrunjump;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import phoenix.delta.Utilities;

public class Star
{
    private float m_x, m_y;
    private float m_w, m_h;
    private boolean m_alive;

    private Game m_game;
    private RectF m_rect;

    public Star(Game game)
    {
        m_game = game;

        m_w = game.getStarImage().getWidth();
        m_h = game.getStarImage().getHeight();

        m_rect = new RectF();
    }

    public RectF getRect()
    {
        return m_rect;
    }

    public void spawn()
    {
        m_x = m_game.getBackgroundImage().getWidth();
        m_y = Utilities.random(DroidConstants.GROUND_Y - m_game.getDroid().getHeight() + DroidConstants.JUMP_HEIGHT, DroidConstants.GROUND_Y) - m_h;
        m_rect.top = m_y;
        m_rect.bottom = m_y + m_h;
        m_alive = true;
        Log.i("DRJ", "Spawning star at " + m_x);
    }

    public void update()
    {
        m_x -= DroidConstants.MOVEMENT_RATE;
        m_rect.left = m_x;
        m_rect.right = m_x + m_w;

        if (m_x < -m_w)
        {
            m_alive = false;
        }
    }

    public boolean isAlive()
    {
        return m_alive;
    }

    public void setAlive(boolean p_alive)
    {
        m_alive = p_alive;
    }

    public void draw(Canvas canvas)
    {
        canvas.drawBitmap(m_game.getStarImage(), m_rect.left, m_rect.top, m_game.getClearPaint());
    }

    public void save(SharedPreferences.Editor map, int i)
    {
        map.putFloat("ps_x", m_x);
        map.putFloat("ps_y", m_y);
        map.putFloat("ps_w", m_w);
        map.putFloat("ps_h", m_h);
        map.putBoolean("ps_alive", m_alive);
        map.putInt("ps_star", i);
    }
}

