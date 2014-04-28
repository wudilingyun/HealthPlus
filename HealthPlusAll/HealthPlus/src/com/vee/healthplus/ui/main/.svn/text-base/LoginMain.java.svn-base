package com.vee.healthplus.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
 import com.vee.healthplus.R;
import com.vee.healthplus.ui.user.HealthPlusLoginActivity;
import com.vee.healthplus.util.user.HP_User;
import com.vee.myhealth.util.DBManager;

/**
 * Created by xujizhe on 14-1-21.
 */
public class LoginMain extends Activity {
    private TextView loginText;
    private  DBManager dbHelper;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                	if (HP_User.getOnLineUserId(LoginMain.this) == 0) {
                        Intent intent = new Intent();
                        intent.setClass(LoginMain.this,HealthPlusLoginActivity.class);
                        startActivity(intent);
                	}else{
                		Intent intent = new Intent();
                        intent.setClass(LoginMain.this,MainPage.class);
                        startActivity(intent);
                	}
                	finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        loginText = (TextView) findViewById(R.id.login_text);
        addAskWeeknessDB();
    }
    
    void addAskWeeknessDB() {
		dbHelper = new DBManager(this);
		dbHelper.openDatabase();
		dbHelper.closeDatabase();
	}

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(5000);
                    handler.sendEmptyMessage(1);
                } catch (InterruptedException e) {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }
    
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	handler.removeMessages(1);
    }
}
