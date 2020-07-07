package hrx.dict.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import hrx.dict.domain.User;

public class UserService {
	private DBOpenHelper dbOpenHelper;
    public UserService(Context context) {
			this.dbOpenHelper = new DBOpenHelper(context);
		}

	public void save(User user){
		//如果要对数据进行更改，就调用此方法得到用于操作数据库的实例,该方法以读和写方式打开数据库
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into user (loginname,loginpsw) values(?,?)",
				new Object[]{user.getLoginname(),user.getLoginpsw()});
		db.close();
	}
	
	public void delete(User user){
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		db.execSQL("delete from user where loginname=?", new String[]{user.getLoginname()});
		db.close();
	}
	/**
	 * 判断用户名是否存在
	 */
	public boolean isExist(User user){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from user where loginname = ?", new String[]{user.getLoginname()});
		boolean result = cursor.getCount() > 0? true : false ;
		cursor.close();
		db.close();
		return result ;
	}

	public boolean exist(String loginname){
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from user where loginname = ?", new String[]{loginname});
		boolean result = cursor.getCount() > 0? true : false ;
		cursor.close();
		db.close();
		return result;
		
	}
	public List<User> getUsers(){
		List<User> users = new ArrayList<User>();
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query("users", null, null, null, null, null, null);
		while(cursor.moveToNext()){
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String loginname = cursor.getString(cursor.getColumnIndex("loginname"));
			String loginpsw = cursor.getString(cursor.getColumnIndex("loginpsw"));
			String tel = cursor.getString(cursor.getColumnIndex("tel"));
			String email = cursor.getString(cursor.getColumnIndex("email"));
			//System.out.println(word_name);
			User user = new User();
			user.setLoginname(loginname);
			user.setLoginpsw(loginpsw);
			user.setTel(tel);
			user.setEmail(email);
			users.add(user);
		}
		cursor.close();
		db.close();
		return users;
	}

	//判断用户是否存在
	public User getUserByLoginNameAndPsw(String loginname,String loginpsw){
		List<User> users = new ArrayList<User>();

		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

		Cursor cursor = db.rawQuery("select * from user where loginname = ? and loginpsw = ?", new String[]{loginname,loginpsw});

		User user=null;

		while(cursor.moveToNext()){
			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String loginname1 = cursor.getString(cursor.getColumnIndex("loginname"));
			String loginpsw1 = cursor.getString(cursor.getColumnIndex("loginpsw"));
			user = new User();
			user.set_id(_id);
			user.setLoginname(loginname);
			user.setLoginpsw(loginpsw);
            break;
		}
		cursor.close();
		db.close();
		return user;
	}
	
	public void close(){
		dbOpenHelper.close();
	}

}
