package com.chriswallace.fragmentactivity.Database;




        import android.content.Context;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;


/**
 * Created by chris on 12/23/13.
 */
public class ModelDatabase extends SQLiteOpenHelper {
    //Table Name
    public static final String TABLE_NAME = "chats";

    //Table Fields
    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String CHATS = "chats";
    public static final String TIMEDATE = "timestamp";

    //Database Info
    private static final String DATABASE_NAME = "ChatsDatabase";
    private static final int DATABASE_VERSION = 1;

    // ModelDatabase creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_NAME + "("
            + ID + " AUTOINCREMENT INTEGER PRIMARY KEY NOT NULL, "
            + USERNAME + " TEXT NOT NULL, "
            + CHATS + " TEXT NOT NULL, "
            + TIMEDATE + " TEXT NOT NULL ); ";


    //Default Constructor
    public ModelDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //OnCreate Method - creates the ModelDatabase
    public void onCreate(SQLiteDatabase database){
        Log.d("DATAbASE",DATABASE_CREATE);
        database.execSQL(DATABASE_CREATE);

    }
    @Override
    //OnUpgrade Method - upgrades ModelDatabase if applicable
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion){
        Log.w(ModelDatabase.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }
}
