package hrx.dict.activity;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import hrx.dict.domain.User;
import hrx.dict.service.UserService;

public class RegisterActivity extends Activity {


    private Button registbutton;

    private EditText loginname;

    private EditText loginpsw;


    UserService us;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);

        us = new UserService(RegisterActivity.this);

        loginname = (EditText) findViewById(R.id.loginname);

        loginpsw = (EditText) findViewById(R.id.loginpsw);


        registbutton = (Button) findViewById(R.id.register_btn);

        //����button�¼�
        registbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginname.getText().toString().length() < 1) {
                    Toast.makeText(RegisterActivity.this, "��½�˺Ų���Ϊ��!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (loginpsw.getText().toString().length() < 1) {
                    Toast.makeText(RegisterActivity.this, "��½���벻��Ϊ��!", Toast.LENGTH_LONG).show();
                    return;
                }

                User user = new User();
                user.setLoginname(loginname.getText().toString());
                user.setLoginpsw(loginpsw.getText().toString());


                if (us.isExist(user)) {
                    Toast.makeText(RegisterActivity.this, "���˺��Ѵ��ڣ�����������!",
                            Toast.LENGTH_LONG).show();
                    return;
                } else {
                    us.save(user);

                    Toast.makeText(RegisterActivity.this, "ע��ɹ�!",
                            Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });

    }

}
