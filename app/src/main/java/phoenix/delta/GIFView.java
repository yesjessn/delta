package phoenix.delta;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.util.AttributeSet;
import android.view.View;

import java.io.InputStream;

public class GIFView extends View
{
    private Movie m_gifMovie;
    private int m_movieWidth, m_movieHeight;
    private long m_mMovieStart;

    public GIFView(Context p_context)
    {
        super(p_context);
        init(p_context);
    }

    public GIFView(Context p_context, AttributeSet p_attrs)
    {
        super(p_context, p_attrs);
        init(p_context);
    }

    public GIFView(Context p_context, AttributeSet p_attrs,
                   int p_defStyleAttr)
    {
        super(p_context, p_attrs, p_defStyleAttr);
        init(p_context);
    }

    private void init(Context p_context)
    {
        setFocusable(true);
        InputStream gifInputStream = p_context.getResources().openRawResource(+R.drawable.loading);

        m_gifMovie = Movie.decodeStream(gifInputStream);
        m_movieWidth = m_gifMovie.width();
        m_movieHeight = m_gifMovie.height();
    }

    @Override
    protected void onMeasure(int p_widthMeasureSpec,
                             int p_heightMeasureSpec)
    {
        setMeasuredDimension(m_movieWidth, m_movieHeight);
    }

    public static final int    DEFAULT_GIF_LENGTH            = 1000;

    @Override
    protected void onDraw(Canvas p_canvas)
    {
        long now = android.os.SystemClock.uptimeMillis();
        if (m_mMovieStart == 0)
        {   // first time
            m_mMovieStart = now;
        }

        if (m_gifMovie != null)
        {
            int dur = m_gifMovie.duration();
            if (dur == 0)
            {
                dur = DEFAULT_GIF_LENGTH;
            }

            int relTime = (int)((now - m_mMovieStart) % dur);

            m_gifMovie.setTime(relTime);

            m_gifMovie.draw(p_canvas, 0, 0);
            invalidate();
        }
    }
}