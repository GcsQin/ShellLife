package com.ustb.shellbox.shelllife.databean;

/**
 * Created by 37266 on 2017/3/25.
 */

public class LibDeptInfo {
    String tiaoma;
    String bookName;
    String author;
    String borrowTime;
    String returnTime;
    String shouldPay;
    String realPay;
    String state;

    public LibDeptInfo(String tiaoma, String bookName, String author, String borrowTime, String returnTime, String shouldPay, String realPay, String state) {
        this.tiaoma = tiaoma;
        this.bookName = bookName;
        this.author = author;
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
        this.shouldPay = shouldPay;
        this.realPay = realPay;
        this.state = state;
    }

    public String getTiaoma() {
        return tiaoma;
    }

    public void setTiaoma(String tiaoma) {
        this.tiaoma = tiaoma;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBorrowTime() {
        return borrowTime;
    }

    public void setBorrowTime(String borrowTime) {
        this.borrowTime = borrowTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getShouldPay() {
        return shouldPay;
    }

    public void setShouldPay(String shouldPay) {
        this.shouldPay = shouldPay;
    }

    public String getRealPay() {
        return realPay;
    }

    public void setRealPay(String realPay) {
        this.realPay = realPay;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
