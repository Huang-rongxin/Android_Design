package hrx.dict.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import hrx.dict.activity.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


//管理打开一个已有的数据库
public class DBManager {
	private final int BUFFER_SIZE = 400000;    
	public static final String DB_NAME = "dictionary.db"; //保存的数据库文件名
	public static final String PACKAGE_NAME = "hrx.dict.activity";
	public static final String DB_PATH = "/data"+ Environment.getDataDirectory().getAbsolutePath() + "/"+ PACKAGE_NAME;  //在手机里存放数据库的位置   
	private Context context; 
	private SQLiteDatabase database;
    public DBManager(Context context) {        
    	this.context = context;
    	open();
    }

    public void open() {        
    	try
		{
			// 获得dictionary.db文件的绝对路径
  			String databaseFilename = DB_PATH + "/" + DB_NAME;
			File dir = new File(DB_PATH);
			// 如果/sdcard/dictionary目录中存在，创建这个目录
			if (!dir.exists())
				dir.mkdir();
			// 如果在/sdcard/dictionary目录中不存在 dictionary.db文件，则从res\raw目录中复制这个文件到 SD卡的目录（/sdcard/dictionary）
			if (!(new File(databaseFilename)).exists())
			{
				// 获得封装dictionary.db文件的InputStream对象
				InputStream is = this.context.getResources().openRawResource(
						R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				// 开始复制dictionary.db文件
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			// 打开/sdcard/dictionary目录中的dictionary.db文件
			database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
		}
		catch (Exception e)
		{
		}
    }     
    //查询
    public Cursor select(String sql,String[] condition){
		return database.rawQuery(sql, condition);
    }
    //模糊查询
    public Cursor fuzzySelect(String sql,String[] condition){
    	return database.rawQuery(sql, condition);
    }
    public void closeDatabase() {        
    	database.close();    
    }
}
