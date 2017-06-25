package com.helloncu.sesystem.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.helloncu.sesystem.R;
import com.helloncu.sesystem.db.Test;
import com.helloncu.sesystem.entity.Test1;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        checkpermission();
        initDate();
    }

    //检查权限
    private void checkpermission() {
        //        运行时权限
        ArrayList<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(SplashActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, 1);
        }
    }

    /**
     * 初始化的方法，在这里实现数据库的创建
     */
    private void initDate() {
        new Thread() {
            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();
                    super.run();
                    //初始化数据库
                    LitePal.getDatabase();
                    long end = System.currentTimeMillis();
                    //这里对数据库进行操作
                    File dataFile = new File(getFilesDir(), "test.json");
                    if (dataFile.exists()) {
                        Log.d("dataFile", "文件已经存在！");
                    } else {
                        FileOutputStream fos = null;
                        InputStream is = null;
                        try {
                            fos = new FileOutputStream(dataFile);
                            is = getAssets().open("test.json");
                            int len = 0;
                            byte[] by = new byte[1024];
                            while ((len = is.read(by)) != -1) {
                                fos.write(by, 0, len);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                fos.close();
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    boolean exist = DataSupport.isExist(Test.class);
                    if (!exist) {
                        Gson gson = new Gson();
                        List<Test1> test1 = null;
                        try {
                            test1 = gson.fromJson(new JsonReader(new FileReader(dataFile)), new TypeToken<List<Test1>>() {
                            }.getType());
                            Log.d("test1.size()", test1.size() + "");
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        for (Test1 t : test1) {
                            Test test = new Test();
                            test.setTitle(t.getTitle());
                            test.setAnswer0(t.getAnswer0());
                            test.setAnswer1(t.getAnswer1());
                            test.setAnswer2(t.getAnswer2());
                            test.setAnswer3(t.getAnswer3());
                            test.setTrueAnswer(t.getTrueAnswer());
                            boolean saved = test.saveIfNotExist("title=?", t.getTitle());
                            //test.save();
                            //boolean saved = test.isSaved();
                            if (saved) {
                                Log.d("test", "添加成功");
                            } else {
                                Log.d("test", "添加失败");
                            }
                        }
                    } else {
                        Log.d("test", "数据库已存在！");
                        List<Test> testList = DataSupport.findAll(Test.class);
                        for (Test t1 : testList) {
                            Log.d("test", t1.getTitle() + t1.getTrueAnswer());
                        }
                    }

                    long time = end - start;
                    if (time > 2000) {
                        handler.sendEmptyMessage(0);
                    } else {
                        sleep(2000 - time);
                        handler.sendEmptyMessage(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initView() {

    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    Boolean isgotomap = true;
                    for (int result : grantResults) {
                        //当所有权限都开启后才进行地图活动
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(SplashActivity.this, "必须同意所有权限才能使用本功能", Toast.LENGTH_SHORT).show();
                            isgotomap = false;
                            return;
                        }
                    }
                    switch (requestCode) {
                        case 1:
                            if (grantResults.length > 0) {
                                boolean a = false;
                                for (int result : grantResults) {
                                    //当所有权限都开启后才进行地图活动
                                    if (result != PackageManager.PERMISSION_GRANTED) {
                                        Toast.makeText(SplashActivity.this, "必须同意所有权限才能使用本功能", Toast.LENGTH_SHORT).show();
                                        a = false;
                                        return;
                                    } else {
                                        a = true;
                                    }
                                }
                                if (a) {
                                    initDate();
                                }
                            } else {
                                Toast.makeText(SplashActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                    }
                } else {
                    Toast.makeText(SplashActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
