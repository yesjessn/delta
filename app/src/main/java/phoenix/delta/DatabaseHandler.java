package phoenix.delta;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper implements Serializable {

    private static DatabaseHandler sInstance;

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "UserDB";

    // Contacts table name
    private static final String USERS = "users";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_USER_TYPE = "user_type";

    public static synchronized DatabaseHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE =
                 "CREATE TABLE " + USERS + "("
                + KEY_ID        + "INTEGER PRIMARY KEY,"
                + KEY_USERNAME  + " TEXT,"
                + KEY_PASSWORD  + " TEXT,"
                + KEY_USER_TYPE + " INTEGER" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + USERS);

        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    void addUser(DUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername()); // Contact Name
        values.put(KEY_PASSWORD, user.getPassword()); // Contact Phone
        values.put(KEY_USER_TYPE, user.getType());

        // Inserting Row
        db.insert(USERS, null, values);
        db.close(); // Closing database connection
    }

    /*
    DUser getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                USERS,
                new String[] { KEY_ID, KEY_USERNAME, KEY_PASSWORD, KEY_USER_TYPE },
                KEY_ID + "=?",
                new String[] { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DUser user = new DUser(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), Integer.parseInt(cursor.getString(3)));
        // return contact
        return user;
    }*/

    public String toString () {
        return "" + getUsersCount();
    }

    public List<DUser> getAllUsers() {
        List<DUser> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + USERS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DUser user = new DUser();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setPassword(cursor.getString(2));
                user.setType(Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return contact list
        return userList;
    }

    // Updating single contact
    public int updateUser(DUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_PASSWORD, user.getPassword());
        values.put(KEY_USER_TYPE, user.getType());

        // updating row
        return db.update(USERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.getId())});
    }

    // Deleting single contact
    public void deleteUser(DUser user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(USERS, KEY_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // Getting contacts Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + USERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    /** FUNCTION I ADDED **/
    public boolean isUserInDB (DUser user) {

        List<DUser> allUsers = this.getAllUsers();
        boolean found = false;

        for(int i = 0; i < allUsers.size(); i++) {
            DUser u = allUsers.get(i);
            if(u.getUsername().compareTo(user.getUsername()) == 0 &&
                    u.getPassword().compareTo(user.getPassword()) == 0 &&
                    u.getType() == u.getType()) {
                found = true;
                //user.setId(u.getId());
            }
        }

        return found;
    }


}
