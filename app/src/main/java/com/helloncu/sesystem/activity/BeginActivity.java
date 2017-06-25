package com.helloncu.sesystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.helloncu.sesystem.R;
import com.helloncu.sesystem.db.User;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by liangzhan on 17-6-22.
 * 登录成功后的界面
 */

public class BeginActivity extends AppCompatActivity {

    private NavigationView nav_view;
    private Button bt_unitTest;
    private Button bt_selectTest;
    private DrawerLayout drawer_layout;
    private TextView tv_navname;
    private TextView tv_navsex;
    private EditText et_testnumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_begin);
        initView();
        initEvent();
    }

    private void initEvent() {
        //侧滑栏选项监听
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    //分数排行榜
                    case R.id.nav_mark:

                        break;
                    //个人资料
                    case R.id.nav_setting:
                        break;
                }
                return true;
            }
        });
        //全真模拟测试
        bt_selectTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSelectTest();
            }
        });
        //单元测试按钮
        bt_unitTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoUnitTest();
            }
        });
    }

    //单元测试处理方法
    private void gotoUnitTest() {

    }

    //全真模拟处理方法
    private void gotoSelectTest() {
        Intent intent = new Intent(BeginActivity.this, SelectTestActivity.class);
        et_testnumber = (EditText) findViewById(R.id.et_testnumber);
        String s = et_testnumber.getText().toString();
        if (s.equals("")) {
            Toast.makeText(BeginActivity.this, "题量不能为空！", Toast.LENGTH_SHORT).show();
        } else if (s.substring(0, 1).equals("0")) {
            Toast.makeText(BeginActivity.this, "题量不能以0开头！", Toast.LENGTH_SHORT).show();
        } else {
            int number = Integer.valueOf(s);
            intent.putExtra("testnumber", number);
            startActivity(intent);
        }
    }

    private void initView() {
        bt_selectTest = (Button) findViewById(R.id.bt_selectTest);
        bt_unitTest = (Button) findViewById(R.id.bt_unitTest);
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        //侧滑栏显示
        View headerView = nav_view.getHeaderView(0);
        tv_navname = (TextView) headerView.findViewById(R.id.tv_navname);
        tv_navsex = (TextView) headerView.findViewById(R.id.tv_navsex);
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        String name = config.getString("name", null);
        List<User> users = DataSupport.where("name=?", name).find(User.class);
        User user = users.get(0);
        tv_navname.setText(user.getName());
        if (user.getSex() == 1) {
            tv_navsex.setText("男");
        } else {
            tv_navsex.setText("女");
        }

        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toolbar tl_begin = (Toolbar) findViewById(R.id.tl_begin);
        setSupportActionBar(tl_begin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer_layout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BeginActivity.this);
        builder.setTitle("退出程序");
        builder.setMessage("确认退出吗？");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }
}