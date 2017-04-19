package com.ustb.shellbox.shelllife.databean;

import java.io.Serializable;

/**
 * Created by 37266 on 2017/3/15.
 */
public class EportalCardBean implements Serializable{
    private String time;
    private String where;
    private String consumer;
    private String balance;

    public EportalCardBean(String time, String where, String consumer, String balance) {
        this.time = time;
        this.where = where;
        this.consumer = consumer;
        this.balance = balance;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
