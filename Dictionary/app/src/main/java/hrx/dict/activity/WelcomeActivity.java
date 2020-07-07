package hrx.dict.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity{
	  public void onCreate(Bundle savedInstancetate){
		  super.onCreate(savedInstancetate);
		  this.setContentView(R.layout.welcome);
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
                  startActivity(intent);
                  finish();
              }
          },3000);
      }

}
	
	