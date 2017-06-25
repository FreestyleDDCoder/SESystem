package com.helloncu.sesystem.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by liangzhan on 17-6-21.
 * 这是测试题目的数据库
 */

public class Test extends DataSupport implements Serializable {
    private String title;
    private String answer0;
    private String answer1;
    private String answer2;
    private String answer3;
    private String trueAnswer;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAnswer0() {
        return answer0;
    }

    public void setAnswer0(String answer0) {
        this.answer0 = answer0;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getTrueAnswer() {
        return trueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        this.trueAnswer = trueAnswer;
    }
}
