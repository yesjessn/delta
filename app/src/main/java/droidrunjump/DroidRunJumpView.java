package droidrunjump;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DroidRunJumpView extends SurfaceView
        implements SurfaceHolder.Callback
{
    private DroidRunJumpThread m_thread;

    private Game m_game;
    private SurfaceHolder m_holder;

    public DroidRunJumpView(Context p_context, AttributeSet p_attrs)
    {
        super(p_context, p_attrs);

        m_holder = getHolder();
        m_holder.addCallback(this);

        m_game = new Game(p_context);

        m_thread = null;

        setFocusable(true);
    }

    public void surfaceChanged(SurfaceHolder p_holder, int p_format, int p_width,
                               int p_height)
    {
        m_thread.setSurfaceSize(p_width, p_height);
    }

    public void surfaceCreated(SurfaceHolder p_holder)
    {
        m_thread.setRunning(true);
        m_thread.start();
    }

    public void surfaceDestroyed(SurfaceHolder p_holder)
    {
        boolean retry = true;
        m_thread.setRunning(false);
        while (retry)
        {
            try
            {
                m_thread.join();
                retry = false;
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        m_thread = null;
    }

    public boolean onTouchEvent(@NonNull MotionEvent p_event)
    {
        return getThread().doTouchEvent(p_event);
    }

    public DroidRunJumpThread getThread()
    {
        if (m_thread == null)
        {
            m_thread = new DroidRunJumpThread(m_holder, m_game);
        }
        return m_thread;
    }
}
