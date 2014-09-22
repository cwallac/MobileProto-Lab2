package com.chriswallace.fragmentactivity.Database;


        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.util.Log;

        import java.util.ArrayList;

/**
 * Created by chris on 12/23/13.
 */
public class HandlerDatabase {
    //Database Model
    private ModelDatabase model;

    //Database
    private SQLiteDatabase database;
    private SQLiteDatabase db;

    //All Fields
    private String[] allColumns = {
            ModelDatabase.ID,
            ModelDatabase.USERNAME,
            ModelDatabase.CHATS,
            ModelDatabase.TIMEDATE
    };

    //Public Constructor - create connection to Database
    public HandlerDatabase(Context context){
        Log.d("HANDLER","HANDLER IS RUNNING");
        model = new ModelDatabase(context);
    }


    public void addInfoToDatabase(String username, String message, String Date){
        ContentValues values = new ContentValues();
        values.put(ModelDatabase.USERNAME, username);
        values.put(ModelDatabase.CHATS, message);
        values.put(ModelDatabase.TIMEDATE, Date);

        database.insertWithOnConflict(ModelDatabase.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }



    public ArrayList<ChatEntry> getAllMessages(){
        database = model.getWritableDatabase();
        return sweepCursor(database.query(ModelDatabase.TABLE_NAME, allColumns, null, null, null, null, null));
    }


    public void modifyChats(String chat, int row ,String username){

        db = model.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ModelDatabase.CHATS,chat);
        values.put(ModelDatabase.USERNAME,username);



        int test = db.update(ModelDatabase.TABLE_NAME, values, "id" +" = ?" , new String[] {Integer.toString(row+1)}); //DATABASE START AT 1 not 0
        //APPARENTLY ID MAY CHANGE IN A
        //NON EXPECTED WAY SO THIS IS NOT IDEAL, INSTEAD GET TEXT FROM THE CLICKED LISTVIEW AND VERIFY WITH THAT TEXT AND TIMESTAMP AND USER
        Log.d("STATUS",Integer.toString(test)); //LOG VERIFYING IT ADDED

    }



    public void deleteDataBase(){


        db = model.getWritableDatabase();

        db.delete("chats",null,null);

        //db.execSQL("delete from "+ ModelDatabase.TABLE_NAME);
    }

    /**
     * Additional Helpers
     */
    //Sweep Through Cursor and return a List of Kitties
    private ArrayList<ChatEntry> sweepCursor(Cursor cursor){
        ArrayList<ChatEntry> chats = new ArrayList<ChatEntry>();

        //Get to the beginning of the cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            //Get the Kitty
            ChatEntry chat = new ChatEntry(
                    cursor.getString(1),
                    cursor.getString(2),

                    cursor.getString(3)
            );
            //Add the Kitty
            chats.add(chat);
            //Go on to the next Kitty
            cursor.moveToNext();
        }
        return chats;
    }

    //Get Writable Database - open the database
    public void open(){
        database = model.getWritableDatabase();
    }
}
