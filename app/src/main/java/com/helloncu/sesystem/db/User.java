package com.helloncu.sesystem.db;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by liangzhan on 17-6-21.
 * 这是用户表的实体
 */

public class User extends DataSupport implements Serializable {
    private String name;
    private String password;
    private int sex;
    private int mark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
