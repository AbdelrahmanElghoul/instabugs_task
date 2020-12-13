package com.example.instabugs_task.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Database  extends SQLiteOpenHelper {
    private final String TAG="sql db ";
    private static final String Table_Name="table_words";
    private static final String Col_1="word";
    private static final String Col_2="count";
    private Context context;
    private  static final int version=1;
    private static final String DataBase_Name="WordsCount.dp";

    public Database(Context context, int version) {
        super( context, DataBase_Name, null, version );
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "Create Table "
                +Table_Name+" ("
                +Col_1+ "String  Primary Key unique, "
                +Col_2+" int)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "Drop Table if exists "+ Table_Name );
        onCreate( db );
    }

    private void Add(String word,int count){
        try{
            SQLiteDatabase dp=getWritableDatabase();
            ContentValues value=new ContentValues();
            value.put(Col_1,word);
            value.put( Col_2,count);
            dp.insert( Table_Name,null,value );
            dp.close();
        }
        catch (Exception e){
            Toast.makeText( context,e.getMessage(),Toast.LENGTH_LONG ).show();
            Log.e(TAG,e.getMessage());
        }

    }
    /**
     * Add all
     * - save word in sharedPreference
     * - drop table
     * - append to table
     * - delete sharedPreference
     * */
    public void AddAll(HashMap<String,Integer> map){
        try{
            //todo add thread
            SQLiteDatabase db = this.getWritableDatabase();
            db.execSQL("DROP TABLE IF EXISTS "+Table_Name);
            onCreate( db );

            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
//                System.out.println(pair.getKey() + " = " + pair.getValue());
                Add(pair.getKey().toString(),Integer.parseInt(pair.getValue().toString()));
                it.remove(); // avoids a ConcurrentModificationException
            }

            DatabaseBackup.deleteBackup(context);
        }
        catch (Exception e){
            Toast.makeText( context,e.getMessage(),Toast.LENGTH_LONG ).show();
            Log.e(TAG,e.getMessage());
        }

    }

    public HashMap<String,Integer> getAll(){
        HashMap<String,Integer> map=new HashMap<>();
        try {
            SQLiteDatabase db=getReadableDatabase();
            Cursor cursor=db.rawQuery( "select * from "+Table_Name ,null);
            cursor.moveToFirst();
            do {
                String word = cursor.getString( 0 );
                int count = cursor.getInt( 1 );
                map.put(word,count);
            } while (cursor.moveToNext());
            db.close();

        }
        catch (Exception  e){
            Toast.makeText( context,e.getMessage(),Toast.LENGTH_LONG ).show();
        }

        return map;
    }


}
