package hrx.dict.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import hrx.dict.domain.WordItem;

public class WordItemAdapter extends BaseAdapter {





	private List<WordItem> list = null;
	Context context = null;
	public WordItemAdapter(List<WordItem> list ,Context context) {
		super();
		this.list = list;
		this.context = context;
	}

	public int getCount() {

		return list.size();
	}

	public Object getItem(int position) {
		
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout item_layout = new LinearLayout(context);
		WordItem wordItem = (WordItem) this.getItem(position);
		TextView word_name_TV = new TextView(context);
		word_name_TV.setText(wordItem.getWord_name());
		word_name_TV.setTextSize(20f);
		word_name_TV.setMinWidth(250);
		word_name_TV.setMaxWidth(250);
		item_layout.addView(word_name_TV);
		return item_layout;
	}

}
