package hrx.dict.service;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;


import hrx.dict.domain.WordItem;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class WordService{
	private DBOpenHelper dbOpenHelper;
    public WordService(Context context) {
    	this.dbOpenHelper = new DBOpenHelper(context);
	}

	public void save(WordItem wordItem){
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into words (english,chinese) values(?,?)",
				new Object[]{wordItem.getWord_name(),wordItem.getWord_mean()});
		db.close();
	}
	
	public void delete(WordItem wordItem){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from words where english=?", new String[]{wordItem.getWord_name()});
		db.close();
	}

	/**
	 * 判断单词是否存在
	 * @param wordItem
	 * @return
	 */
	public boolean isExist(WordItem wordItem){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from words where  english = ?", new String[]{wordItem.getWord_name()});
		boolean result = cursor.getCount() > 0? true : false ;
		cursor.close();
		db.close();
		return result ;
	}

	public String isExist2(WordItem wordItem){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from words where  english = ?", new String[]{wordItem.getWord_name()});
		cursor.moveToFirst();
		String result = cursor.getString(cursor.getColumnIndex("chinese"));
		cursor.close();
		db.close();
		return result ;
	}

	public List<WordItem> getword(String userid){
		List<WordItem> word_items = new ArrayList<WordItem>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("notebook", null, "user_id="+userid,null,  null, null, null);
		while(cursor.moveToNext()){
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String word_name = cursor.getString(cursor.getColumnIndex("word_name"));
			String word_mean = cursor.getString(cursor.getColumnIndex("word_mean"));
			long word_time = cursor.getLong(cursor.getColumnIndex("word_time"));
			String user_id = cursor.getString(cursor.getColumnIndex("user_id"));
			//System.out.println(word_name);
			WordItem wordItem = new WordItem(_id ,word_name ,word_mean ,word_time,user_id);
			word_items.add(wordItem);
		}
		cursor.close();
		db.close();
		return word_items;
	}
	/**
	 * 
	 * @return 数据库总记录 
	 */
	
	public long getCount() {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from notebook", null);
		cursor.moveToFirst();
		long length = cursor.getLong(0);
		cursor.close();
		db.close();
		return length;
	}
	
	public void close(){
		dbOpenHelper.close();
	}

}
