package com.helloncu.sesystem.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.helloncu.sesystem.R;
import com.helloncu.sesystem.db.Test;
import com.helloncu.sesystem.db.User;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangzhan on 17-6-23.
 * 全真模拟界面
 */

public class SelectTestActivity extends AppCompatActivity {

    private ListView lv_selecttest;
    private LinearLayout ll_testprogress;
    //开始分数为0
    private int mark = 0;
    private FloatingActionButton float_commit;
    private String name;
    private boolean[] oneMark;
    private int testnumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecttest);
        initView();
        initData();
        initEvent();
    }

    private void initData() {
        //获取上一个活动的信息
        SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
        name = config.getString("name", null);
        Intent intent = getIntent();
        testnumber = intent.getIntExtra("testnumber", 20);
        //用于记录每个选项的正确情况
        oneMark = new boolean[testnumber];
        new Thread() {
            @Override
            public void run() {
                super.run();
                //这里用子线程对数据进行查询
                List<Test> testList = DataSupport.findAll(Test.class);
                ArrayList<Test> newTests = new ArrayList<>();
                int temp = 100;
                if (testnumber > testList.size()) {
                    testnumber = testList.size();
                }
                //用于每次随机产生的20个题目
                for (int i = 0; i < testnumber; i++) {
                    int testNumber = (int) (Math.random() * testnumber);
                    if (temp == testNumber) {
                        i--;
                    } else {
                        Test test = testList.get(testNumber);
                        Test test1 = new Test();
                        int answerNumber = (int) (Math.random() * 4);
                        if (answerNumber == 0) {
                            test1.setTitle(test.getTitle());
                            test1.setAnswer0(test.getAnswer0());
                            test1.setAnswer1(test.getAnswer1());
                            test1.setAnswer2(test.getAnswer2());
                            test1.setAnswer3(test.getAnswer3());
                            test1.setTrueAnswer(test.getTrueAnswer());
                        } else if (answerNumber == 1) {
                            test1.setTitle(test.getTitle());
                            test1.setAnswer0(test.getAnswer1());
                            test1.setAnswer1(test.getAnswer0());
                            test1.setAnswer2(test.getAnswer3());
                            test1.setAnswer3(test.getAnswer2());
                            test1.setTrueAnswer(test.getTrueAnswer());
                        } else if (answerNumber == 2) {
                            test1.setTitle(test.getTitle());
                            test1.setAnswer0(test.getAnswer2());
                            test1.setAnswer1(test.getAnswer3());
                            test1.setAnswer2(test.getAnswer0());
                            test1.setAnswer3(test.getAnswer1());
                            test1.setTrueAnswer(test.getTrueAnswer());
                        } else {
                            test1.setTitle(test.getTitle());
                            test1.setAnswer0(test.getAnswer3());
                            test1.setAnswer1(test.getAnswer2());
                            test1.setAnswer2(test.getAnswer1());
                            test1.setAnswer3(test.getAnswer0());
                            test1.setTrueAnswer(test.getTrueAnswer());
                        }
                        newTests.add(test1);
                        temp = testNumber;
                    }
                }
                Log.d("查询的条数", testList.size() + "");
                Log.d("查询的条数", newTests.size() + "");
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putSerializable("newTests", (Serializable) newTests);
                message.setData(bundle);
                message.what = 0;
                handler.sendMessage(message);
            }
        }.start();
    }

    private void initEvent() {
        //提交答案监听时间
        float_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SelectTestActivity.this);
                builder.setTitle("提示！");
                builder.setMessage("确定提交这次测试？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gotoGetMark();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });
    }

    //计算分数的方法
    private void gotoGetMark() {
        //根据记录计算结果
        float evevryTestMark = (float) (100.0 / testnumber);
        for (int i = 0; i < testnumber; i++) {
            if (oneMark[i])
                mark += evevryTestMark;
        }

        User user = new User();
        user.setMark(mark);
        user.updateAll("name=?", name);

        //以弹窗的方式显示分数
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectTestActivity.this);
        builder.setTitle("最终得分：");
        builder.setMessage("" + mark + "分");
        builder.setPositiveButton("返回主页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
    }

    private void initView() {
        lv_selecttest = (ListView) findViewById(R.id.lv_selecttest);
        ll_testprogress = (LinearLayout) findViewById(R.id.ll_testprogress);
        float_commit = (FloatingActionButton) findViewById(R.id.float_commit);
        Toolbar tl_selecttest = (Toolbar) findViewById(R.id.tl_selecttest);
        setSupportActionBar(tl_selecttest);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        ll_testprogress.setVisibility(View.VISIBLE);
    }

    public class selectTestAdapter extends BaseAdapter {

        private final List<Test> mList;

        public selectTestAdapter(List<Test> list) {
            mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final Test test = mList.get(position);
            ViewHolder viewHolder = new ViewHolder();
            if (convertView != null) {
                //由于复用导致选择某项时导致其他项被选上，所以这里不复用，所以代码这么写...
                //viewHolder = (ViewHolder) convertView.getTag();
                convertView = View.inflate(parent.getContext(), R.layout.item_selecttest, null);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.rb_answer0 = (RadioButton) convertView.findViewById(R.id.rb_answer0);
                viewHolder.rb_answer1 = (RadioButton) convertView.findViewById(R.id.rb_answer1);
                viewHolder.rb_answer2 = (RadioButton) convertView.findViewById(R.id.rb_answer2);
                viewHolder.rb_answer3 = (RadioButton) convertView.findViewById(R.id.rb_answer3);
                viewHolder.rg_selecetest = (RadioGroup) convertView.findViewById(R.id.rg_selecetest);
                convertView.setTag(viewHolder);
            } else {
                convertView = View.inflate(parent.getContext(), R.layout.item_selecttest, null);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.rb_answer0 = (RadioButton) convertView.findViewById(R.id.rb_answer0);
                viewHolder.rb_answer1 = (RadioButton) convertView.findViewById(R.id.rb_answer1);
                viewHolder.rb_answer2 = (RadioButton) convertView.findViewById(R.id.rb_answer2);
                viewHolder.rb_answer3 = (RadioButton) convertView.findViewById(R.id.rb_answer3);
                viewHolder.rg_selecetest = (RadioGroup) convertView.findViewById(R.id.rg_selecetest);
                convertView.setTag(viewHolder);
            }
            viewHolder.tv_title.setText(position + 1 + "." + test.getTitle());
            viewHolder.rb_answer0.setText("A." + test.getAnswer0());
            viewHolder.rb_answer1.setText("B." + test.getAnswer1());
            viewHolder.rb_answer2.setText("C." + test.getAnswer2());
            viewHolder.rb_answer3.setText("D." + test.getAnswer3());
            Log.d("TrueAnswer", test.getTrueAnswer());
            ll_testprogress.setVisibility(View.INVISIBLE);
            viewHolder.rg_selecetest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                    switch (checkedId) {
                        case R.id.rb_answer0:
                            //答案正确和错误时候的方法
                            if (test.getAnswer0().equals(test.getTrueAnswer())) {
                                oneMark[position] = true;
                            } else {
                                oneMark[position] = false;
                            }
                            break;
                        case R.id.rb_answer1:
                            if (test.getAnswer1().equals(test.getTrueAnswer())) {
                                oneMark[position] = true;
                            } else {
                                oneMark[position] = false;
                            }
                            break;
                        case R.id.rb_answer2:
                            if (test.getAnswer2().equals(test.getTrueAnswer())) {
                                oneMark[position] = true;
                            } else {
                                oneMark[position] = false;
                            }
                            break;
                        case R.id.rb_answer3:
                            if (test.getAnswer3().equals(test.getTrueAnswer())) {
                                oneMark[position] = true;
                            } else {
                                oneMark[position] = false;
                            }
                            break;
                        default:
                            oneMark[position] = false;
                    }
                }
            });
            return convertView;
        }
    }

    class ViewHolder {
        private TextView tv_title;
        private RadioGroup rg_selecetest;
        private RadioButton rb_answer0;
        private RadioButton rb_answer1;
        private RadioButton rb_answer2;
        private RadioButton rb_answer3;
    }

    //处理子线程消息
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Bundle data = msg.getData();
                    List<Test> newTests = (List<Test>) data.getSerializable("newTests");
                    lv_selecttest.setAdapter(new selectTestAdapter(newTests));
                    break;
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                gotoShowDialog();
                break;
        }
        return true;
    }

    //展示弹窗提示
    private void gotoShowDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SelectTestActivity.this);
        builder.setTitle("提示！");
        builder.setMessage("确定放弃这次测试？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    //当按下返回键时的方法
    @Override
    public void onBackPressed() {
        gotoShowDialog();
    }
}
