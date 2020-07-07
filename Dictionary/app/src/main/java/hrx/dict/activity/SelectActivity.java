package hrx.dict.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import hrx.dict.adapter.DictionaryAdapter;
import hrx.dict.domain.WordItem;
import hrx.dict.service.DBManager;
import hrx.dict.service.WordService;
import hrx.dict.tools.LanguageAnalysisTools;
import hrx.dict.tools.TranWords_online;

public class SelectActivity extends Activity implements OnClickListener, TextWatcher {
    private AutoCompleteTextView selectWord;
    private ImageButton btn;
    static ImageView imgbt;
    static DBManager dbManager;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    static Cursor cur;
    MyApplication myApp;
    private ProgressDialog mProgressDialog  = null;
    private TranWords_online tran;
    private static final int MES_1 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //���õ�ǰ��ͼ
        setContentView(R.layout.selectword);
        myApp = MyApplication.getInstance();
        initComponent();
    }

    private void initComponent() {
        //ͨ��FindViewByIdʵ�������
        selectWord = (AutoCompleteTextView) findViewById(R.id.input_act);
        btn = (ImageButton) findViewById(R.id.select_btn);
        //ʵ�����������ѯ������
        tv1 = (TextView) findViewById(R.id.select_word);
        //��ʾ��ѯ������������ݵ���˼
        tv2 = (TextView) findViewById(R.id.select_word_mean);
        tv3 = (TextView) findViewById(R.id.show);
        imgbt = (ImageView) findViewById(R.id.add_local);
        //����Զ�����ı���ļ����¼�
        selectWord.addTextChangedListener(this);
        selectWord.setThreshold(1); //���Ĵ�����ʾ���ַ����ȣ�dafault=2
        //��Ӳ�ѯ��ť�ļ����¼�
        btn.setOnClickListener(this);
        imgbt.setOnClickListener(this);
    }


    @Override
    public void afterTextChanged(Editable s) {
        dbManager = new DBManager(this);
        tv1.setVisibility(View.INVISIBLE);
        tv2.setVisibility(View.INVISIBLE);
        tv3.setVisibility(View.INVISIBLE);
        imgbt.setVisibility(View.INVISIBLE);
        if (s.length() > 0) {
            int language = LanguageAnalysisTools.getLanguage(s.toString());
            if (language == 0) {
                String sql = "select chinese as _id from t_words where chinese like ?";
                cur = dbManager.fuzzySelect(sql, new String[]{s.toString() + "%"});
                if (cur.getCount() > 0) {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            cur, true);
                    selectWord.setAdapter(dictionaryAdapter);
                } else {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            null, true);
                    selectWord.setAdapter(dictionaryAdapter);
                }
            } else if (language == 1) {
                //���뽫english�ֶεı�����Ϊ_id
                String sql = "select english as _id from t_words where english like ?";
                cur = dbManager.fuzzySelect(sql, new String[]{s.toString() + "%"});
                if (cur.getCount() > 0) {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            cur, true);
                    selectWord.setAdapter(dictionaryAdapter);
                } else {
                    DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                            null, true);
                    selectWord.setAdapter(dictionaryAdapter);
                }
            }
        } else {
            DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                    null, true);
            selectWord.setAdapter(dictionaryAdapter);
        }
        dbManager.closeDatabase();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // TODO Auto-generated method stub
    }

    //�����¼�
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_local:
                local();
                break;
            case R.id.select_btn:
                search();
                break;
            default:
                break;
        }

    }

    //��ѯ����
    public void search() {
        cur.close();
        //��ʼ��DBManager
        dbManager = new DBManager(this);
        if (!selectWord.getText().toString().equals("")) {
            //��ȡ��ѯ������
            String selword = selectWord.getText().toString();
            //��ȡ��ѯ���ݵ�����
            String sql = null;
            String result = "δ�ҵ��õ���.";
            if (selword != null) {
                int language = LanguageAnalysisTools.getLanguage(selword);
                if (language == 0) {//��ѯ��������
                    Toast.makeText(SelectActivity.this, "������Ӣ�ĵ���", Toast.LENGTH_SHORT).show();
                    selectWord.setText("");
                } else if (language == 1) {//��ѯ����Ӣ��
                    WordItem w = new WordItem(selectWord.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));//���ʡ����͡��û�id
                    //��ʼ��WordService
                    WordService ws = new WordService(SelectActivity.this);
                    Log.d("test","  "+selectWord.getText().toString());
                    if(ws.isExist(w)){
                        tv1.setVisibility(View.VISIBLE);
                        tv2.setVisibility(View.VISIBLE);
                        tv3.setVisibility(View.VISIBLE);
                        imgbt.setVisibility(View.VISIBLE);
                        tv2.setText(ws.isExist2(w));
                        tv1.setText(selectWord.getText());
                    }else{
                        dialog();
                    }
                }
            }
        } else {
            DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(this,
                    null, true);
            selectWord.setAdapter(dictionaryAdapter);
            Toast.makeText(SelectActivity.this, "����Ϊ�գ������뵥��", Toast.LENGTH_SHORT).show();
        }
        dbManager.closeDatabase();
    }

    //��ӻ��Ƴ����ʵ����شʿ�
    public void local() {
        //��ʼ��WordItem
        WordItem w = new WordItem(tv1.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));//���ʡ����͡��û�id
        //��ʼ��WordService
        WordService ws = new WordService(SelectActivity.this);
        //�жϸõ����Ƿ��Ѵ���notebook��
        if (!ws.isExist(w)) {
            //�������������WordService�е�save�������������
            ws.save(w);
            ws.close();
            Toast.makeText(SelectActivity.this, "��������شʿ�ɹ�!", Toast.LENGTH_SHORT).show();
            ws = new WordService(SelectActivity.this);
            ws.close();
            imgbt.setImageResource(R.drawable.del_note);
        } else {
            ws = new WordService(SelectActivity.this);
            //�����������WordService�е�delete����ɾ������
            ws.delete(w);
            ws.close();
            ws = new WordService(SelectActivity.this);
            Toast.makeText(SelectActivity.this, "�ӱ��شʿ�ɾ���ɹ�!", Toast.LENGTH_SHORT).show();
            ws.close();
            imgbt.setImageResource(R.drawable.add_note);
        }
    }

    public void search_online()
    {
        tv1.setVisibility(View.VISIBLE);
        tv2.setVisibility(View.VISIBLE);
        tv3.setVisibility(View.VISIBLE);
        imgbt.setVisibility(View.VISIBLE);
        try
        {
            final String words=selectWord.getText().toString();
            if(words!=null&&!words.equals(""))
            {
                mProgressDialog = ProgressDialog.show(SelectActivity.this, null, "��ѯ��...");    //���ȶԻ���
                new Thread(){
                    @Override
                    public void run() {
                        tran=new TranWords_online(words);
                        Message message=new Message();
                        message.what=MES_1;
                        handler.sendMessage(message);
                    }
                }.start();
            }
        }
        catch (Exception e) {
        }
    }

    private Handler handler=new Handler(){
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case MES_1:
                    mProgressDialog.dismiss();
                    tv2.setText(tran.strTran);
                    tv1.setText(selectWord.getText());//���ʱ�����ѡ��
                    Log.d("����","tv2:"+tran.strTran);
                    break;
            }
        }
    };

    private void dialog(){
        //��new��һ�������������úü���
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE://��
                        if(CheckNet()) {
                            search_online();
                        }else{
                            Toast.makeText(SelectActivity.this, "�����޷�����.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Dialog.BUTTON_NEGATIVE://��
                        tv1.setVisibility(View.INVISIBLE);
                        tv2.setVisibility(View.INVISIBLE);
                        tv3.setVisibility(View.INVISIBLE);
                        imgbt.setVisibility(View.INVISIBLE);
                        Toast.makeText(SelectActivity.this, "���شʿ�δ�ҵ��õ���.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //dialog��������
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������
        builder.setTitle("��ʾ"); //���ñ���
        builder.setMessage("���شʿ�δ�ҵ��ôʣ��Ƿ�������ѯ?"); //��������
        builder.setPositiveButton("��",dialogOnclicListener);
        builder.setNegativeButton("��", dialogOnclicListener);
        builder.create().show();
    }

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

}
