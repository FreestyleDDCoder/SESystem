package com.helloncu.sesystem.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.helloncu.sesystem.R;
import com.helloncu.sesystem.db.User;
import com.helloncu.sesystem.utils.MD5Utils;

import org.litepal.crud.DataSupport;

import java.util.Iterator;
import java.util.List;

/**
 * Created by liangzhan on 17-6-21.
 * 这是登录界面
 */

public class LoginActivity extends AppCompatActivity {

    private Button bt_login;
    private Button bt_regist;
    private EditText et_loginname;
    private EditText et_loginpassword;
    private CheckBox cb_rmpassword;
    private SharedPreferences.Editor sharedPreferences;
    private String name;
    private String password;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        String preferencesName = preferences.getString("name", null);
        String preferencesPassword = preferences.getString("password", null);
        boolean isChecked = preferences.getBoolean("isChecked", false);
        et_loginname.setText(preferencesName);
        et_loginpassword.setText(preferencesPassword);
        cb_rmpassword.setChecked(isChecked);
    }

    private void initEvent() {
        //登录按钮
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBegainActivity();
            }
        });
        //注册按钮
        bt_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegistActivity();
            }
        });
    }

    //保存密码
    private void gotoRmPassword() {
        if (!cb_rmpassword.isChecked()) {
            sharedPreferences.putBoolean("isChecked", false);
            sharedPreferences.putString("name", name);
            sharedPreferences.putString("password", null);
            sharedPreferences.apply();
        } else {
            sharedPreferences.putBoolean("isChecked", true);
            sharedPreferences.putString("name", name);
            sharedPreferences.putString("password", password);
            sharedPreferences.apply();
        }
    }

    //跳转到注册界面
    private void gotoRegistActivity() {
        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
        startActivityForResult(intent, 0);
    }

    //登录按钮处理方法
    private void gotoBegainActivity() {
        name = et_loginname.getText().toString();
        password = et_loginpassword.getText().toString();
        //健壮性检验
        if ((name.equals("")) || (password.equals(""))) {
            Toast.makeText(LoginActivity.this, "账号/密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            //保存密码的方法
            gotoRmPassword();
            List<User> users = DataSupport.where("name=?", name).find(User.class);
            Iterator<User> iterator = users.iterator();
            if (iterator.hasNext()) {
                User user = iterator.next();
                if (MD5Utils.MD5Encryption(password).equals(user.getPassword())) {
                    Intent intent = new Intent(LoginActivity.this, BeginActivity.class);
                    intent.putExtra("name", name);
                    Toast.makeText(LoginActivity.this, "密码正确！", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "账号/密码错误！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "账号/密码错误！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        Toolbar tl_login = (Toolbar) findViewById(R.id.tl_login);
        setSupportActionBar(tl_login);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        et_loginname = (EditText) findViewById(R.id.et_loginname);
        et_loginpassword = (EditText) findViewById(R.id.et_loginpassword);
        bt_login = (Button) findViewById(R.id.bt_login);
        bt_regist = (Button) findViewById(R.id.bt_regist);
        cb_rmpassword = (CheckBox) findViewById(R.id.cb_rmpassword);
        preferences = getSharedPreferences("config", MODE_PRIVATE);
        sharedPreferences = preferences.edit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                if (resultCode == 0) {
                    if (data != null) {
                        String name = data.getStringExtra("name");
                        String password = data.getStringExtra("password");
                        et_loginname.setText(name);
                        et_loginpassword.setText(password);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
