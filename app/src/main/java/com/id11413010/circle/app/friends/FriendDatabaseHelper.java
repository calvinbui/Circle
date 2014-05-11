package com.id11413010.circle.app.friends;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.id11413010.circle.app.Constants;
import com.id11413010.circle.app.R;

/**
 * This class enables data to be stored in the database
 *
 */
public class FriendDatabaseHelper extends SQLiteOpenHelper {

    /**
     * Initialise the database by creating table, column names and primary key
     */
    public FriendDatabaseHelper(Context context) {
        super(context, Constants.FRIENDS, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = ("create table " + Constants.FRIENDS + " ( "
                + Constants.COLUMN_ID + " integer primary key autoincrement,"
                + Constants.COLUMN_FIRSTNAME + " text not null,"
                + Constants.COLUMN_LASTNAME + " text not null,"
                + Constants.COLUMN_PICTURE + " text );");
        db.execSQL(table);
    }

    /**
     * When the table is updated remove old table
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.FRIENDS);
        onCreate(db);
    }

    public Cursor getUsers() {
        //opens a database connection and makes the database readable
        SQLiteDatabase db = getReadableDatabase();

        //create a new Cursor from a result set after querying the table
        Cursor cursor = db.query(Constants.FRIENDS, new String[] {Constants.COLUMN_ID, Constants.COLUMN_FIRSTNAME, Constants.COLUMN_LASTNAME}, null, null, null, null, null);
        //if the cursor is not empty
        if (cursor != null)
            //move the cursor to the first row
            cursor.moveToFirst();
        //returns the cursor
        return cursor;
    }

    public String[] from() {
        //create a String array with db keys
        String [] from = {
                Constants.COLUMN_FIRSTNAME, //subject name key
                Constants.COLUMN_LASTNAME, //subject number key
        };
        //returns the String array
        return from;
    }

    /**
     * The views that display information from the columns in the database
     * @return Returns an array of integers representing TextViews
     */
    // Ryan| This doesn't belong here. This has more to do with the activity.
    public int[] to() {
        //creates a new integer array with TextView values
        int [] to = {
                R.id.friend_firstName, //subject name TextView
                R.id.friend_lastName, //subject number TextView
        };
        //returns the integer array
        return to;
    }
}
