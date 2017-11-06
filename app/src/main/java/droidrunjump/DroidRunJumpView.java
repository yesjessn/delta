package droidrunjump;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DroidRunJumpView extends SurfaceView implements SurfaceHolder.Callback {
    private DroidRunJumpThread m_thread;
    private Game m_game;
    private SurfaceHolder m_holder;

    public DroidRunJumpView(Context p_context, AttributeSet p_attrs) {
        super(p_context, p_attrs);

        m_holder = getHolder();
        m_holder.addCallback(this);

        m_game = new Game(p_context);
        m_thread = new DroidRunJumpThread(m_holder, m_game);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder p_holder, int p_format, int p_width,
                               int p_height) {
        m_thread.setSurfaceSize(p_width, p_height);
    }

    @Override
    public void surfaceCreated(SurfaceHolder p_holder) {
        m_thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder p_holder) {
        m_thread.stop();
        m_thread.waitForStop();
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent p_event)
    {
        return getThread().doTouchEvent(p_event);
    }

    public DroidRunJumpThread getThread() {
        return m_thread;
    }
}
