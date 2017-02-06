package droidrunjump;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class DroidRunJumpThread extends Thread
{
    private final SurfaceHolder m_surfaceHolder;
    private boolean       m_run;
    private Game m_game;

    public DroidRunJumpThread(SurfaceHolder p_surfaceHolder, Game p_game)
    {
        m_run = false;
        m_surfaceHolder = p_surfaceHolder;
        m_game = p_game;
    }

    public void setSurfaceSize(int p_width, int p_height)
    {
        synchronized (m_surfaceHolder)
        {
            m_game.setScreenSize(p_width, p_height);
        }
    }

    public void setRunning(boolean p_b)
    {
        m_run = p_b;
    }

    @Override
    public void run()
    {
        while (m_run)
        {
            Canvas c = null;
            try
            {
                c = m_surfaceHolder.lockCanvas(null);
                synchronized (m_surfaceHolder)
                {
                    m_game.run(c);
                }
            }
            finally
            {
                if (c != null)
                {
                    m_surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }


    boolean doTouchEvent(MotionEvent p_event)
    {
        boolean handled = false;

        synchronized (m_surfaceHolder)
        {
            switch (p_event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    m_game.doTouch();
                    handled = true;
                    break;
            }
        }

        return handled;
    }

    public void pause()
    {
        synchronized (m_surfaceHolder)
        {
            m_game.pause();
            m_run = false;
        }
    }

    public void resetGame()
    {
        synchronized (m_surfaceHolder)
        {
            m_game.initOrResetGame();
        }
    }

    public void saveGame(SharedPreferences.Editor p_editor)
    {
        synchronized (m_surfaceHolder)
        {
            m_game.save(p_editor);
        }
    }
}
