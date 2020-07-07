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
		TabHost th = getTabHost();   //TabHost����һ��������������Դ�Ŷ��Tab
		myApp = MyApplication.getInstance(); // ����Զ����Ӧ�ó���MyApp
		LayoutInflater.from(this).inflate(R.layout.dict, th.getTabContentView(),true);
		//���ڽ�tab��ӵ�tabHost
		th.addTab(th.newTabSpec("dictionary")
				.setIndicator("��ѯ����",getResources().getDrawable(R.drawable.dictab))
				.setContent(new Intent(MainActivity.this,SelectActivity.class)));
		th.addTab(th.newTabSpec("translate")
				.setIndicator("���ӷ���",getResources().getDrawable(R.drawable.transtab))
				.setContent(new Intent(MainActivity.this,TranslateActivity.class)));
		//��ӱ�ǩ�л��¼�����
		SQLiteStudioService.instance().start(this);
	}

	@Override
	protected void onDestroy() {
		SQLiteStudioService.instance().stop();
		super.onDestroy();
	}
}