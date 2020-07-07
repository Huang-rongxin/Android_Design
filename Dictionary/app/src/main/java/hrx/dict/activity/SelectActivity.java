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
        //设置当前视图
        setContentView(R.layout.selectword);
        myApp = MyApplication.getInstance();
        initComponent();
    }

    private void initComponent() {
        //通过FindViewById实例化组件
        selectWord = (AutoCompleteTextView) findViewById(R.id.input_act);
        btn = (ImageButton) findViewById(R.id.select_btn);
        //实例化组件，查询的内容
        tv1 = (TextView) findViewById(R.id.select_word);
        //显示查询结果，输入内容的意思
        tv2 = (TextView) findViewById(R.id.select_word_mean);
        tv3 = (TextView) findViewById(R.id.show);
        imgbt = (ImageView) findViewById(R.id.add_local);
        //添加自动完成文本框的监听事件
        selectWord.addTextChangedListener(this);
        selectWord.setThreshold(1); //更改触发提示的字符长度，dafault=2
        //添加查询按钮的监听事件
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
                //必须将english字段的别名设为_id
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

    //监听事件
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

    //查询单词
    public void search() {
        cur.close();
        //初始化DBManager
        dbManager = new DBManager(this);
        if (!selectWord.getText().toString().equals("")) {
            //获取查询的内容
            String selword = selectWord.getText().toString();
            //获取查询内容的类型
            String sql = null;
            String result = "未找到该单词.";
            if (selword != null) {
                int language = LanguageAnalysisTools.getLanguage(selword);
                if (language == 0) {//查询词是中文
                    Toast.makeText(SelectActivity.this, "请输入英文单词", Toast.LENGTH_SHORT).show();
                    selectWord.setText("");
                } else if (language == 1) {//查询词是英文
                    WordItem w = new WordItem(selectWord.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));//单词、解释、用户id
                    //初始化WordService
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
            Toast.makeText(SelectActivity.this, "搜索为空，请输入单词", Toast.LENGTH_SHORT).show();
        }
        dbManager.closeDatabase();
    }

    //添加或移除单词到本地词库
    public void local() {
        //初始化WordItem
        WordItem w = new WordItem(tv1.getText().toString(), tv2.getText().toString(), String.valueOf(myApp.getUser().get_id()));//单词、解释、用户id
        //初始化WordService
        WordService ws = new WordService(SelectActivity.this);
        //判断该单词是否已存在notebook中
        if (!ws.isExist(w)) {
            //若不存在则调用WordService中的save方法添加生单词
            ws.save(w);
            ws.close();
            Toast.makeText(SelectActivity.this, "添加至本地词库成功!", Toast.LENGTH_SHORT).show();
            ws = new WordService(SelectActivity.this);
            ws.close();
            imgbt.setImageResource(R.drawable.del_note);
        } else {
            ws = new WordService(SelectActivity.this);
            //若存在则调用WordService中的delete方法删除单词
            ws.delete(w);
            ws.close();
            ws = new WordService(SelectActivity.this);
            Toast.makeText(SelectActivity.this, "从本地词库删除成功!", Toast.LENGTH_SHORT).show();
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
                mProgressDialog = ProgressDialog.show(SelectActivity.this, null, "查询中...");    //进度对话框
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
                    tv1.setText(selectWord.getText());//生词本单词选项
                    Log.d("测试","tv2:"+tran.strTran);
                    break;
            }
        }
    };

    private void dialog(){
        //先new出一个监听器，设置好监听
        DialogInterface.OnClickListener dialogOnclicListener=new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case Dialog.BUTTON_POSITIVE://是
                        if(CheckNet()) {
                            search_online();
                        }else{
                            Toast.makeText(SelectActivity.this, "网络无法连接.", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case Dialog.BUTTON_NEGATIVE://否
                        tv1.setVisibility(View.INVISIBLE);
                        tv2.setVisibility(View.INVISIBLE);
                        tv3.setVisibility(View.INVISIBLE);
                        imgbt.setVisibility(View.INVISIBLE);
                        Toast.makeText(SelectActivity.this, "本地词库未找到该单词.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        //dialog参数设置
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("提示"); //设置标题
        builder.setMessage("本地词库未找到该词，是否联网查询?"); //设置内容
        builder.setPositiveButton("是",dialogOnclicListener);
        builder.setNegativeButton("否", dialogOnclicListener);
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
