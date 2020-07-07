package hrx.dict.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import hrx.dict.activity.R;

//AutoCompleteTextView所使用的Adapter
public class DictionaryAdapter extends CursorAdapter
{
	private LayoutInflater layoutInflater;
	@Override
	//将cursor转换成CharSequence,返回一个有序的字符集合
	public CharSequence convertToString(Cursor cursor)
	{
		//返回指定的列
		//在Android 查询数据是通过Cursor 类来实现的。当我们使用 SQLiteDatabase.query()方法时，就会得到Cursor对象， Cursor所指向的就是每一条数据
		return cursor == null ? "" : cursor.getString(cursor.getColumnIndex("_id"));
	}
	private void setView(View view, Cursor cursor)
	{
		TextView tvWordItem = (TextView) view;
		tvWordItem.setText(cursor.getString(cursor.getColumnIndex("_id")));
	}
    //重用一个已有的view，使其显示当前cursor所指向的数据。将当前cursor绑定为view的Text内容 
	@Override
	public void bindView(View view, Context context, Cursor cursor)     // view是已存在的视图, 返回之前newView方法创建的视图。
	{
		setView(view, cursor);
	}
    
    //新建一个视图来保存cursor指向的数据
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent)    //Context是应用程序全局信息接口
	{
		View view = layoutInflater.inflate(R.layout.word_list_item, null);
		setView(view, cursor);
		return view;
	}
	public DictionaryAdapter(Context context, Cursor c, boolean autoRequery)
	{
		super(context, c, autoRequery);
		layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);    //取得xml里定义的view
	}
}