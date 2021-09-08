package com.example.akujobijoshua.StudentBuddy.Timetable;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	public static final int DB_VERSION = 3;
	public static final String DB_NAME = "cs.db";

	public DBHelper(Context ctx) {
		super(ctx, DB_NAME, null, DB_VERSION);
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}

	public void createTables(SQLiteDatabase database) {
		String batches_table_sql = "create table " + Database.BATCHES_TABLE_NAME + " ( " +
				Database.BATCHES_ID 	+ " integer  primary key autoincrement," +
				Database.BATCHES_BATCHCODE + " TEXT," +
				Database.BATCHES_COURSE + " TEXT," +
				Database.BATCHES_STARTDATE  + " TEXT," +
				Database.BATCHES_REMARKS + " TEXT,"+
				Database.BATCHES_ENDDATE + " TEXT)" ;



		String classes_table_sql = "create table " + Database.CLASSES_TABLE_NAME + " ( " +
				Database.CLASSES_CLASSES_ID 	+ " integer  primary key autoincrement," +
				Database.CLASSES_BATCHCODE + " TEXT," +
				Database.CLASSES_CLASSDATE + " TEXT," +
				Database.CLASSES_CLASSTIME + " TEXT," +
				Database.CLASSES_CLASSPERIOD + " integer," +
				Database.CLASSES_TOPICS+ " TEXT," +
				Database.CLASSES_REMARKS + " TEXT,"+
				Database.CLASSES_LECTURER + " TEXT,"+
				Database.CLASSES_TYPE + " TEXT,"+
				Database.CLASSES_ROOM + " TEXT)";

		try {
			database.execSQL(batches_table_sql);
			database.execSQL("insert into batches (batchcode,course, startdate,remarks,enddate)"
					+   "values ('HB2404','Hibernate','2017-04-24','Short course','2017-04-24')");

			database.execSQL(classes_table_sql);

			database.execSQL("insert into classes (batchcode,classdate,classtime,period,topics,remarks,lecturer,Type,room)"
					+   "values ('HB2404','2017-04-24','19:00',90,null,null,null,null,null)");
			database.execSQL("insert into classes (batchcode,classdate,classtime,period,topics,remarks,lecturer,Type,room)"
					+   "values ('HB2404','2017-04-24','19:00',90,null,null,null,null,null)");
			database.execSQL("insert into classes (batchcode,classdate,classtime,period,topics,remarks,lecturer,Type,room)"
					+   "values ('HB2404','2017-04-24','19:00',90,null,null,null,null,null)");

			Log.d("CS","Tables created!");

		}
		catch(Exception ex) {
			Log.d("CS", "Error in DBHelper.onCreate() : " + ex.getMessage());
		}
	}

}
