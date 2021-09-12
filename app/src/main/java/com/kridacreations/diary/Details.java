package com.kridacreations.diary;

public class Details {
    private int mDate;
    private int mMonth;
    private int mYear;
    private String mFeeling;
    private String mDesc;

    public Details(int mDate, int mMonth, int mYear, String mFeeling, String mDesc) {
        this.mDate = mDate;
        this.mMonth = mMonth;
        this.mYear = mYear;
        this.mFeeling = mFeeling;
        this.mDesc = mDesc;
    }

    public int getDate() {
        return mDate;
    }

    public int getMonth() {
        return mMonth;
    }

    public int getYear() {
        return mYear;
    }

    public String getFeeling() {
        return mFeeling;
    }

    public String getDesc() {
        return mDesc;
    }
}
