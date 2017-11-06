package droidrunjump;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class DroidRunJumpThread implements Runnable
{
    private final SurfaceHolder m_surfaceHolder;
    private volatile boolean m_run;
    private Game m_game;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final Object selfLock = new Object();
    private volatile ScheduledFuture<?> self;

    public DroidRunJumpThread(SurfaceHolder p_surfaceHolder, Game p_game)
    {
        m_run = false;
        m_surfaceHolder = p_surfaceHolder;
        m_game = p_game;
    }

    public void start() {
        synchronized (selfLock) {
            if (self == null) {
                m_run = true;
                this.self = executor.scheduleAtFixedRate(this, 0, 25, TimeUnit.MILLISECONDS);
            }
        }
    }

    public void stop() {
        m_run = false;
    }

    public void waitForStop() {
        Future<?> f;
        synchronized (selfLock) {
            f = self;
        }
        if (f == null) {
            return;
        }
        try {
            f.get();
        } catch (Exception ignored) { }
    }

    public void setSurfaceSize(int p_width, int p_height) {
        synchronized (m_surfaceHolder)
        {
            m_game.setScreenSize(p_width, p_height);
        }
    }

    @Override
    public void run() {
        if (m_run) {
            Canvas c = null;
            try {
                c = m_surfaceHolder.lockCanvas();
                synchronized (m_surfaceHolder) {
                    m_game.run(c);
                }
            }
            finally {
                if (c != null) {
                    m_surfaceHolder.unlockCanvasAndPost(c);
                }
            }
        } else {
            synchronized (selfLock) {
                self.cancel(true);
                this.self = null;
            }
        }
    }

    boolean doTouchEvent(MotionEvent p_event) {
        boolean handled = false;

        synchronized (m_surfaceHolder) {
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
        synchronized (m_surfaceHolder) {
            m_game.pause();
        }
    }

    public void resetGame()
    {
        synchronized (m_surfaceHolder) {
            m_game.initOrResetGame();
        }
    }

    public void saveGame(SharedPreferences.Editor p_editor)
    {
        synchronized (m_surfaceHolder) {
            m_game.save(p_editor);
        }
    }
}
