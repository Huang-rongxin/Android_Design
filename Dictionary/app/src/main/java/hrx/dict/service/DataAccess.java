package hrx.dict.service;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DataAccess {	
	
	public static InputStream getStreamByUrl(String _url)
	{
		//空格转换
		_url = _url.replace(" ", "%20");
		URL url;
		InputStream inputStream=null;
		try {
			url = new URL(_url);
		    URLConnection connection = url.openConnection();
		    HttpURLConnection httpConnection = (HttpURLConnection)connection; //发送网络请求服务
		    int responseCode = httpConnection.getResponseCode();
		    if (responseCode == HttpURLConnection.HTTP_OK)
		    {
		    	inputStream = httpConnection.getInputStream();
		    }
		} catch (Exception e) {
			return null;
		}
		return inputStream;
	}
}
