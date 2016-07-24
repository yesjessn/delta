package phoenix.delta;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class DatabaseHandler extends SQLiteOpenHelper
        implements Serializable
{


    private DatabaseHandler(Context p_context)
    {
        super(p_context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase p_db)
    {
        String CREATE_CONTACTS_TABLE =
                 "CREATE TABLE " + Constants.USERS + "("
                + Constants.KEY_ID        + "INTEGER PRIMARY KEY,"
                + Constants.KEY_USERNAME  + " TEXT,"
                + Constants.KEY_PASSWORD  + " TEXT,"
                + Constants.KEY_USER_TYPE + " INTEGER" + ")";
        p_db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase p_db, int p_oldVersion, int p_newVersion)
    {
        // Drop older table if existed
        p_db.execSQL("DROP TABLE IF EXISTS " + Constants.USERS);

        // Create tables again
        onCreate(p_db);
    }

    public String toString ()
    {
        return "" + getUsersCount();
    }

    // Getting contacts Count
    public int getUsersCount()
    {
        String countQuery = "SELECT  * FROM " + Constants.USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}