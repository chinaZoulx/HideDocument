package com.chris.hidedocument.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.chris.hidedocument.R;

/**
 * Created by zoulx on 2016/3/30.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText password;
    private EditText passwordOnceAgain;
    private EditText inputPassword;

    private LinearLayout settingBorder, loginBorder;

    private Button btnLogin, btnSetting;

    private SharedPreferences sharedPreferences;

    private final String ACCOUNT = "account", PASSWORD = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initView();
    }

    private void init() {
        sharedPreferences = this.getSharedPreferences("config", MODE_PRIVATE);
    }

    private void initView() {
        password = (EditText) findViewById(R.id.password);
        passwordOnceAgain = (EditText) findViewById(R.id.passwordOnceAgain);
        inputPassword = (EditText) findViewById(R.id.inputPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnSetting = (Button) findViewById(R.id.btnSetting);
        settingBorder = (LinearLayout) findViewById(R.id.settingBorder);
        loginBorder = (LinearLayout) findViewById(R.id.loginBorder);

        //
        if ("".equalsIgnoreCase(sharedPreferences.getString(PASSWORD, ""))) {//未设置密码，显示设置密码的布局
            settingBorder.setVisibility(View.VISIBLE);
            loginBorder.setVisibility(View.GONE);
        }else{//同上反之
            settingBorder.setVisibility(View.GONE);
            loginBorder.setVisibility(View.VISIBLE);
        }
        //

        btnSetting.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String passwordStr;
        switch (v.getId()) {
            case R.id.btnLogin:
                passwordStr = inputPassword.getText().toString();
                if (isRight(passwordStr)) {
                    goMainActivity();
                }else{
                    Snackbar.make(v, "输入的密码不对", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnSetting:
                String pdOA = passwordOnceAgain.getText().toString();
                String pd = password.getText().toString();
                if (!pd.equalsIgnoreCase("")&&pdOA.equalsIgnoreCase(pd)) {
                    saveInfo(pd);
                    goMainActivity();
                } else {
                    Snackbar.make(v, "再次输入的密码不对", Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 跳转到主页面
     */
    private void goMainActivity(){
        finish();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoginActivity.this, ScrollingActivity.class));
            }
        }).start();

    }

    /**
     * 保存信息
     * @param password
     */
    private void saveInfo(String password) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    /**
     * 密码是否正确
     * @param password
     * @return
     */
    private boolean isRight(String password) {
        boolean flag = false;
        String localhostPassword = sharedPreferences.getString(PASSWORD, "");
        if (localhostPassword.equalsIgnoreCase(password)) {
            flag = true;
        }
        return flag;
    }

}
