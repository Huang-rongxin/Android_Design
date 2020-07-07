package hrx.dict.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import hrx.dict.activity.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


//�����һ�����е����ݿ�
public class DBManager {
	private final int BUFFER_SIZE = 400000;    
	public static final String DB_NAME = "dictionary.db"; //��������ݿ��ļ���
	public static final String PACKAGE_NAME = "hrx.dict.activity";
	public static final String DB_PATH = "/data"+ Environment.getDataDirectory().getAbsolutePath() + "/"+ PACKAGE_NAME;  //���ֻ��������ݿ��λ��   
	private Context context; 
	private SQLiteDatabase database;
    public DBManager(Context context) {        
    	this.context = context;
    	open();
    }

    public void open() {        
    	try
		{
			// ���dictionary.db�ļ��ľ���·��
  			String databaseFilename = DB_PATH + "/" + DB_NAME;
			File dir = new File(DB_PATH);
			// ���/sdcard/dictionaryĿ¼�д��ڣ��������Ŀ¼
			if (!dir.exists())
				dir.mkdir();
			// �����/sdcard/dictionaryĿ¼�в����� dictionary.db�ļ������res\rawĿ¼�и�������ļ��� SD����Ŀ¼��/sdcard/dictionary��
			if (!(new File(databaseFilename)).exists())
			{
				// ��÷�װdictionary.db�ļ���InputStream����
				InputStream is = this.context.getResources().openRawResource(
						R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				// ��ʼ����dictionary.db�ļ�
				while ((count = is.read(buffer)) > 0)
				{
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			// ��/sdcard/dictionaryĿ¼�е�dictionary.db�ļ�
			database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
		}
		catch (Exception e)
		{
		}
    }     
    //��ѯ
    public Cursor select(String sql,String[] condition){
		return database.rawQuery(sql, condition);
    }
    //ģ����ѯ
    public Cursor fuzzySelect(String sql,String[] condition){
    	return database.rawQuery(sql, condition);
    }
    public void closeDatabase() {        
    	database.close();    
    }
}
