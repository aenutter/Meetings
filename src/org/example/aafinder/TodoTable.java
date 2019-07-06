package org.example.aafinder;

import android.database.sqlite.SQLiteDatabase;
//import android.util.Log;

public class TodoTable {

  // Database table
  public static final String TABLE_TODO = "todo";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_CATEGORY = "category";
  public static final String COLUMN_SUMMARY = "summary";
  public static final String COLUMN_DESCRIPTION = "description";
  
  public static final String COLUMN_FOREIGN_KEY = "foreign_key";
  public static final String COLUMN_START_TIME = "start_time";
  public static final String COLUMN_TYPE = "type";
  public static final String COLUMN_GROUP_NAME = "group_name";
  public static final String COLUMN_STREET = "street";
  public static final String COLUMN_PHONE = "phone";
  public static final String COLUMN_CITY = "city";
  public static final String COLUMN_STATE = "state";
  public static final String COLUMN_DAY = "day";
  public static final String COLUMN_LATITUDE = "latitude";
  public static final String COLUMN_LONGITUDE = "longitude";
  public static final String COLUMN_FELLOWSHIP = "fellowship";
  public static final String COLUMN_LOCATION = "location";
  public static final String COLUMN_ZIP = "zip";

  // Database creation SQL statement
  private static final String DATABASE_CREATE = "create table " 
      + TABLE_TODO
      + "(" 
      + COLUMN_ID + " integer primary key autoincrement, " 
      + COLUMN_CATEGORY + " text not null, " 
      + COLUMN_SUMMARY + " text not null, " 
      + COLUMN_DESCRIPTION + " text not null, "
      + COLUMN_FOREIGN_KEY + " text not null, " 
      + COLUMN_START_TIME + " text not null, "
      + COLUMN_TYPE + " text not null, "
      + COLUMN_GROUP_NAME + " text not null, "
      + COLUMN_STREET + " text not null, "
      + COLUMN_PHONE + " text not null, "
      + COLUMN_CITY + " text not null, "
      + COLUMN_STATE + " text not null, "
      + COLUMN_DAY + " text not null, "
      + COLUMN_LATITUDE + " text not null,"
      + COLUMN_LONGITUDE + " text not null,"
      + COLUMN_FELLOWSHIP + " text not null,"
      + COLUMN_LOCATION + " text not null, "
      + COLUMN_ZIP + " text not null"
      + ");";

  public static void onDelete(SQLiteDatabase database) {

	  database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);

	  }

  
  public static void onCreate(SQLiteDatabase database) {
	  //database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);  
    database.execSQL(DATABASE_CREATE);
  }

  public static void onUpgrade(SQLiteDatabase database, int oldVersion,
      int newVersion) {
    //Log.w(TodoTable.class.getName(), "Upgrading database from version "
        //+ oldVersion + " to " + newVersion
        //+ ", which will destroy all old data");
    database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
    onCreate(database);
  }
} 