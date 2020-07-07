package hrx.dict.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import hrx.dict.domain.User;
import hrx.dict.service.UserService;


public class LoginActivity extends Activity {

    private Button loginBtn;
    private Button registbutton;
    private EditText etUsername;
    private EditText etPwd;
    UserService us;
    MyApplication myApp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login);

        myApp = MyApplication.getInstance(); // ����Զ����Ӧ�ó���MyApp
        us = new UserService(LoginActivity.this);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPwd = (EditText) findViewById(R.id.etPwd);
        loginBtn = (Button) findViewById(R.id.btLogin);
        //����button�¼�
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etUsername.getText().toString().length() < 1) {
                    Toast.makeText(LoginActivity.this, "��¼�˺Ų���Ϊ��!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (etPwd.getText().toString().length() < 1) {
                    Toast.makeText(LoginActivity.this, "��¼���벻��Ϊ��!", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                User user = us.getUserByLoginNameAndPsw(etUsername.getText().toString(), etPwd.getText().toString());

                if (user != null) {

                    myApp.setUser(user);

                    Intent intent = new Intent();

                    intent.setClass(LoginActivity.this, MainActivity.class);

                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "��¼ʧ��,�����¼�˺ź������Ƿ���ȷ!",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        registbutton = (Button) findViewById(R.id.btRegist);
        //����button�¼�
        registbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();

                intent.setClass(LoginActivity.this, RegisterActivity.class);

                startActivity(intent);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.activity_my_final, menu);
        return true;
    }

}
