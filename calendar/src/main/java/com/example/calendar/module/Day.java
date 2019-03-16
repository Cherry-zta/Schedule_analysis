package com.example.calendar.module;

import android.support.annotation.NonNull;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Blaz Solar on 24/02/14.
 */
public class Day {

    private static final DateTimeFormatter mFormatter = DateTimeFormat.forPattern("d");

    @NonNull
    private final LocalDate mDate;
    private boolean mToday;          //是否今天
    private boolean mSelected;       //是否选择
    private boolean mEnabled;        //是否有事件
    private boolean mCurrent;        //是否为现在事件

    public Day(@NonNull LocalDate date, boolean today) {
        mDate = date;
        mToday = today;
        mEnabled = true;
        mCurrent = true;
    }

    @NonNull
    public LocalDate getDate() {
        return mDate;
    }

    public boolean isToday() {
        return mToday;
    }

     public boolean isSelected() {
        return mSelected;
    }

    public boolean isEnabled() {
        return mEnabled;
    }

     public boolean isCurrent() {
        return mCurrent;
    }

     public void setSelected(boolean selected) {
        mSelected = selected;
    }

    public void setEnabled(boolean enabled) {
        mEnabled = enabled;
    }

    public void setCurrent(boolean current) {
        mCurrent = current;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (o == null || getClass() != o.getClass()) 
            return false;

        Day day = (Day) o;

        if (mEnabled != day.mEnabled) return false;
        if (mSelected != day.mSelected) return false;
        if (mToday != day.mToday) return false;

        return mDate.isEqual(day.mDate);

    }

    @Override
    public int hashCode() {       //获取日期的哈希码
        int result = mDate.hashCode();
        result = 31 * result + (mToday ? 1 : 0);
        result = 31 * result + (mSelected ? 1 : 0);
        result = 31 * result + (mEnabled ? 1 : 0);
        return result;
    }

    @NonNull
    public String getText() {
        return mDate.toString(mFormatter);
    }
}
