package phoenix.delta;

import java.util.Random;

public class Utilities
{
    private static Random m_rng = new Random();

    public static int getOrdinal(Enum p_state)
    {
        if(p_state == null)
        {
            return -1;
        }

        return p_state.ordinal();
    }

    public static float nextFloat()
    {
        return m_rng.nextFloat();
    }

    public static float random(float p_max)
    {
        return m_rng.nextFloat() * p_max;
    }

    public static float random(float p_min, float p_max)
    {
        return Math.round(p_min + random(p_max - p_min));
    }
}
