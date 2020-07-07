package hrx.dict.activity;


import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TabHost;
import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;


public class MainActivity extends TabActivity {

	MyApplication myApp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TabHost th = getTabHost();   //TabHost就像一个容器，里面可以存放多个Tab
		myApp = MyApplication.getInstance(); // 获得自定义的应用程序MyApp
		LayoutInflater.from(this).inflate(R.layout.dict, th.getTabContentView(),true);
		//用于将tab添加到tabHost
		th.addTab(th.newTabSpec("dictionary")
				.setIndicator("查询单词",getResources().getDrawable(R.drawable.dictab))
				.setContent(new Intent(MainActivity.this,SelectActivity.class)));
		th.addTab(th.newTabSpec("translate")
				.setIndicator("句子翻译",getResources().getDrawable(R.drawable.transtab))
				.setContent(new Intent(MainActivity.this,TranslateActivity.class)));
		//添加标签切换事件监听
		SQLiteStudioService.instance().start(this);
	}

	@Override
	protected void onDestroy() {
		SQLiteStudioService.instance().stop();
		super.onDestroy();
	}
}