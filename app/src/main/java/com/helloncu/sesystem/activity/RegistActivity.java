package com.helloncu.sesystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.helloncu.sesystem.R;
import com.helloncu.sesystem.db.User;
import com.helloncu.sesystem.utils.MD5Utils;

/**
 * Created by liangzhan on 17-6-21.
 * 这是注册界面
 */

public class RegistActivity extends AppCompatActivity {

    private EditText et_registname;
    private EditText et_registpassword;
    private EditText et_passwordconfirm;
    private Button bt_regist1;
    private RadioButton rb_boy;
    private RadioButton rb_girl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        initEvent();
    }

    private void initEvent() {
        //注册按钮
        bt_regist1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegist();
            }
        });
    }

    //进行数据库注册的方法
    private void gotoRegist() {
        String name = et_registname.getText().toString();
        String password = et_registpassword.getText().toString();
        String passwordconfirm = et_passwordconfirm.getText().toString();
        //健壮性检验
        if (name.equals("")) {
            Toast.makeText(RegistActivity.this, "账号不能为空！", Toast.LENGTH_SHORT).show();
        } else if (password.equals("")) {
            Toast.makeText(RegistActivity.this, "密码不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            if (password.equals(passwordconfirm)) {
                User user = new User();
                user.setName(name);
                user.setPassword(MD5Utils.MD5Encryption(password));
                if (rb_boy.isChecked()) {
                    user.setSex(1);
                } else if (rb_girl.isChecked()) {
                    user.setSex(0);
                }
                boolean save = user.saveIfNotExist("name=?", name);
                //boolean save = user.save();
                //如果注册成功
                if (save) {
                    Intent intent = new Intent();
                    intent.putExtra("name", name);
                    intent.putExtra("password", password);
                    setResult(0, intent);
                    Toast.makeText(RegistActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(RegistActivity.this, "该账号已经注册！", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegistActivity.this, "两次密码不一致！请重试！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initView() {
        et_registname = (EditText) findViewById(R.id.et_registname);
        et_registpassword = (EditText) findViewById(R.id.et_registpassword);
        et_passwordconfirm = (EditText) findViewById(R.id.et_passwordconfirm);
        bt_regist1 = (Button) findViewById(R.id.bt_regist1);
        rb_boy = (RadioButton) findViewById(R.id.rb_boy);
        rb_girl = (RadioButton) findViewById(R.id.rb_girl);
        Toolbar tl_regist = (Toolbar) findViewById(R.id.tl_regist);
        setSupportActionBar(tl_regist);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
