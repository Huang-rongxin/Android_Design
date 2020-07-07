package hrx.dict.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
	private static final String DATABASENAME = "note.db"; //数据库名称
	private static final int DATABASEVERSION = 1;//数据库版本

	public DBOpenHelper(Context context) {
		super(context, DATABASENAME, null, DATABASEVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE user (_id integer primary key autoincrement, loginname varchar(100), loginpsw varchar(100))");//执行有更改的sql语句
		db.execSQL("CREATE TABLE words (_id integer primary key autoincrement, english varchar(40), chinese varchar(100))");
	}
    
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS user");
		db.execSQL("DROP TABLE IF EXISTS words");
		onCreate(db);
	}

}
