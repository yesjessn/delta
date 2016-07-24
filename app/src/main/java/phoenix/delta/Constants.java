package phoenix.delta;

public class Constants
{
    public static final String SESSION   = "SESSION";

    // file to upload
    public static final String FILE_NAME = "testing.txt";

    // BOX INFO - DON'T CHANGE THESE
    public static final String DELTA_BOX_CLIENT_ID     = "lb35b8rol4cairzf4rk3uqr5vc6kps34";
    public static final String DELTA_BOX_CLIENT_SECRET = "qMvy31CNvoZGyfLkTELq2XHilC5DedBn";
    public static final String DELTA_BOX_REDIRECT_URL  = "https://app.box" + "" +
            ".com/static/sync_redirect.html";

    // All Static variables
    // Database Version
    public static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "UserDB";

    // Contacts table name
    public static final String USERS = "users";

    // Contacts Table Columns names
    public static final String KEY_ID        = "id";
    public static final String KEY_USERNAME  = "username";
    public static final String KEY_PASSWORD  = "password";
    public static final String KEY_USER_TYPE = "user_type";

    public static final long PASSPHRASE = 2015; // changeable

    public static final float START_X               = 530.0f;
    public static final float START_Y               = 502.5f;

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
    public static final int    DEFAULT_NUM_TRIALS            = 10;
    public static final int    DEFAULT_INSTANT_GAME_TIME_SEC = 5;//change this back to 5
    // when done
    public static final int    DEFAULT_TIME_INC_SEC          = 5;
    public static final int    EIGHT_BIT_MAX                 = 255;
    public static final float  FULL_VOLUME                   = 1.0f;
    public static final int    DEFAULT_PRIORITY              = 0;
    public static final int    LOOP_INDICATOR                = 0;
    public static final float  PLAYBACK_RATE                 = 1.0f;
    public static final String READY_SET                     = "READY SET";
    public static final String GO                            = "GO!";
    public static final int    GO_TIME_MS                    = 500;
    public static final double SPAWN_POTHOLE_CHANCE          = .5;
    public static final int    DEFAULT_GIF_LENGTH            = 1000;
    public static float MIN_POTHOLE_WIDTH = 127.0f;

    public static final String PREFS_NAME                      = "DRJPrefsFile";
    public static final int    DEFAULT_INIT_TRIAL_DURATION_SEC = 30;
    public static final int    DEFAULT_DELAYED_GAME_TIME_SEC   = 30;
    public static final int    DEFAULT_TRIALS_BEFORE_INC       = 3;


    public static final int   MAX_POTHOLES       = 1;
    public static final long  SPAWN_POTHOLE_TIME = 750;
    public static final float GROUND_Y           = 400;

    public static final int  SCORE_DEFAULT      = 5000;
    public static final int  SCORE_PASTRY_BONUS = 200;
    public static final long SPAWN_PASTRY_TIME  = 750;

    public static final int MAX_STREAMS       = 4;
    public static final int GET_READY_TIME_MS = 1000;
}
