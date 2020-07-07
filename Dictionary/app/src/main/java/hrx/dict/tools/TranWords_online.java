package hrx.dict.tools;

import android.util.Log;
import java.io.InputStream;
import org.json.JSONObject;
import hrx.dict.service.DataAccess;

public class TranWords_online {
    //有道在线翻译的url
    private final String url = "http://fanyi.youdao.com/openapi.do?keyfrom=android-dict&key=1038310316&type=data&doctype=json&version=1.1&q=";
    public String strTran;

    public TranWords_online(String words)
    {
        //调用查询函数
        Search(words);
    }

    public void Search(String searchWord){
        //获取InputStream对象
        InputStream is = DataAccess.getStreamByUrl(url+searchWord);
        byte[] buffer = new byte[4096];//开辟缓冲区,大小4K
        try {
            Thread.sleep(200);
            is.read(buffer);
            String json  = new String(buffer,"utf-8");
            //获取Json对象
            JSONObject obj = new JSONObject(json);
            if(obj.has("basic")) {
                strTran = replaceJson(obj.getJSONObject("basic").getString("explains"));
            }
            else {
                strTran = replaceJson(obj.getString("translation"));
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String replaceJson(String value)
    {
        value=value.replace(",", "\n");
        value=value.replace("\"", "");
        value=value.replace("[", "");
        value=value.replace("]", "");
        return value;
    }
}
