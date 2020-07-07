package hrx.dict.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class WordItem implements Parcelable {
	private int _id;
	private String word_name = null;
	private String word_mean = null;
	private Long word_time = null;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	private String  user_id = null;
	//构建WordItem
	public WordItem(String word_name, String word_mean,String user_id) {
		super();
		this.word_name = word_name;
		this.word_mean = word_mean;
		this.word_time = System.currentTimeMillis();
		this.user_id=user_id;
	}

	//获取WordItem
	public WordItem(int _id ,String word_name, String word_mean,long word_time,String user_id) {
		super();
		this._id = _id ;
		this.word_name = word_name;
		this.word_mean = word_mean;
		this.word_time = word_time;
		this.user_id=user_id;
	}

	public WordItem() {
		// TODO Auto-generated constructor stub
	}
	public int get_id() {
		return _id;
	}
	public String getWord_name() {
		return word_name;
	}
	public String getWord_mean() {
		return word_mean;
	}


	public static final Parcelable.Creator<WordItem> CREATOR = new Creator<WordItem>() {    
		public WordItem createFromParcel(Parcel source) {    
			WordItem mWordItem = new WordItem();    
			mWordItem._id = source.readInt();
			mWordItem.word_name = source.readString();    
			mWordItem.word_mean = source.readString();    
			mWordItem.word_time = source.readLong();
			mWordItem.user_id=source.readString();
			return mWordItem;    
		}    
		public WordItem[] newArray(int size) {    
			return new WordItem[size];    
		}    
	};    

	@Override  
	public int describeContents() {  
		// TODO Auto-generated method stub   
		return 0;  
	}  
	@Override  
	//通过wrireToParcel()方法将序列化的值写入parcel对象。
	public void writeToParcel(Parcel parcel, int arg1) {  
		//将序列化的值写入parcel对象
		parcel.writeInt(_id);  
		parcel.writeString(word_name);  
		parcel.writeString(word_mean);  
		parcel.writeLong(word_time);
		parcel.writeString(user_id);
	}  
}
