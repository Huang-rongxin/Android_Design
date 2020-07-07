package hrx.dict.activity;


import hrx.dict.tools.LanguageAnalysisTools;
import hrx.dict.tools.TranWords;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;

public class TranslateActivity extends Activity implements OnClickListener {
	private static final int MES_1 = 1; 	
	private TranWords tran;
	private Spinner spinner_type;
	private Button btn_tran;
	private Button btn_clear;
	private EditText et_value;
	private EditText et_result;
	private ProgressDialog mProgressDialog  = null;
	private LanguageAnalysisTools languageAnalysisTools;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate);
		//SelectActivity.dbManager.open();
		initComponent();
	}
	private void initComponent()
	{
		//通过findViewById实例化组件
		btn_tran=(Button)this.findViewById(R.id.btn_tran);
		btn_clear=(Button)this.findViewById(R.id.btn_clear);
		et_value=(EditText)this.findViewById(R.id.et_value);
		et_result=(EditText)this.findViewById(R.id.et_result);
		spinner_type=(Spinner)this.findViewById(R.id.spinner_type);
		//为按钮添加事件监听器
		btn_tran.setOnClickListener(this);
		btn_clear.setOnClickListener(this);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("英译汉");
		spinner_type.setAdapter(adapter);		
	}
	 /**
     * 查询网络是否连接
     * @return
     */
    private Boolean CheckNet()
    {
    	ConnectivityManager manager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo info=manager.getActiveNetworkInfo();
    	if(info!=null&&info.isAvailable())
    	{
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_tran:			
			if(CheckNet())
			{
				search();	
			}
			else
			{
				Toast.makeText(this, "网络无法连接", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.btn_clear:
			clear();
			break;
		default:
			break;
		}		
	}

	 public void search()
    {   	 	  	
    	try
    	{
	    	final String words=et_value.getText().toString();
			languageAnalysisTools=new LanguageAnalysisTools();
			if(languageAnalysisTools.getLanguage(words)==1) {
				if(words!=null&&!words.equals(""))
				{
					mProgressDialog = ProgressDialog.show(TranslateActivity.this, null, "Loading...");    //进度对话框
					new Thread(){
						@Override
						public void run() {
							tran=new TranWords(words);
							Message message=new Message();
							message.what=MES_1;
							handler.sendMessage(message);
						}
					}.start();
				}
			}else{
				Toast.makeText(TranslateActivity.this, "请输入英文!", Toast.LENGTH_SHORT).show();
				clear();
			}
    	}
    	catch (Exception e) {
		}
	}

	private void clear()
	{
		et_value.setText("");
		et_result.setText("");
	}

	 private Handler handler=new Handler(){
	    	public void handleMessage(Message message)
	    	{
	    		switch (message.what) 
	    		{
					case MES_1:
						mProgressDialog.dismiss();
						et_result.setText(tran.strTran);
						break;
				}
	    	}
	    };
}
