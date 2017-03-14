package droidrunjump;

public class DroidConstants {
    public static final float GROUND_Y = 420f;
    public static final float GROUND_X = 64f;


    public static final float MOVEMENT_RATE         = 10.0f;//pixels per second
    public static final float AIR_TIME              = .5f;//seconds
    public static final float JUMP_HEIGHT           = -100.0f;//pixels

    //calculated from the equation y = a * t^2 + v * t based on the given jump height and air time
    public static final float INITIAL_JUMP_VELOCITY = 4 * JUMP_HEIGHT / AIR_TIME;//pixels per second
    public static final float DROID_FALL_ACCEL      = -4 * JUMP_HEIGHT / (AIR_TIME * AIR_TIME);//pixels per second per second

    public static final String DROID_X                       = "droid_x";
    public static final String DROID_Y                       = "droid_y";
    public static final String DROID_VY                      = "droid_vy";
    public static final String DROID_JUMPING                 = "droid_jumping";
    public static final String DROID_CUR_FRAME               = "droid_curFrame";
    public static final String DROID_CUR_FRAME_TIME          = "droid_curFrameTime";
    public static final int    ANIMATION_CYCLE_MS            = 150;
    public static final int    MAX_DROID_IMAGES              = 11;

    public static final int    EIGHT_BIT_MAX                 = 255;
    public static final float  FULL_VOLUME                   = 1.0f;
    public static final int    DEFAULT_PRIORITY              = 0;
    public static final int    LOOP_INDICATOR                = 0;
    public static final float  PLAYBACK_RATE                 = 1.0f;

    public static final String PREFS_NAME                      = "DRJPrefsFile";

    public static final int   MAX_POTHOLES       = 1;
    public static final long  SPAWN_POTHOLE_TIME = 350;
    public static final double SPAWN_POTHOLE_CHANCE  = 0.5;
    public static float MIN_POTHOLE_WIDTH = 127.0f;

    public static final int  SCORE_DEFAULT      = 5000;

    public static final int SCORE_STAR_BONUS = 200;
    public static final long SPAWN_STAR_TIME = 300;
    public static final double SPAWN_STAR_CHANCE = 0.7;

    public static final int MAX_STREAMS       = 4;
}
