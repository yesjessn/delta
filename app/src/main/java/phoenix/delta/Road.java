package phoenix.delta;

import android.content.SharedPreferences;
import android.graphics.Canvas;

public class Road
{
    private Game m_game;

    private final int MAX_DIVIDERS = 11;
    private float [] m_dividerX;

    public Road(Game p_game)
    {
        m_game = p_game;
        m_dividerX = new float[MAX_DIVIDERS];
    }

    public void reset()
    {
        float xOffset = 0.0f;
        for (int i=0; i<MAX_DIVIDERS; i++)
        {
            m_dividerX[i] = xOffset;
            xOffset += 80.0f;
        }
    }

    public void update()
    {
        for (int i = 0; i < MAX_DIVIDERS; i++)
        {
            m_dividerX[i] -= 5.0f;
            if (m_dividerX[i] < -70.0f)
            {
                m_dividerX[i] = m_game.getWidth() + 10.0f;
            }
        }
    }

    public void draw(Canvas p_canvas)
    {
        p_canvas.drawBitmap(m_game.getGrassImage(), 0, Constants.GROUND_Y + 170, m_game.getEmptyPaint());
    }

    public void drawBackground(Canvas p_canvas)
    {
        p_canvas.drawBitmap(m_game.getBackgroundImage(), 0, Constants.GROUND_Y - 275, m_game.getClearPaint());
    }

    public void save(SharedPreferences.Editor p_map)
    {
        for (int i=0; i<MAX_DIVIDERS; i++)
        {
            p_map.putFloat("road_div_" + i + "_x", m_dividerX[i]);
        }
    }
}