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

    public static final int    DEFAULT_GIF_LENGTH            = 1000;
}
