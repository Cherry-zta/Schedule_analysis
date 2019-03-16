package com.example.memo9;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.calendar.util.TimeUtil;
import com.example.library.WeekViewEvent;
import com.example.memo9.database.EditEvents;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import org.joda.time.LocalDateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *  Created by zxy 2019/3/14
 */

public class EditActivity extends AppCompatActivity implements OnDateSetListener {

    private static final String TAG = "EditActivity";
    private TimePickerDialog mDialogAll;
    private int flag;
    private static int id=0;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    private int startHour,startMinute,endHour,endMinute;
    private String startTime, endTime;
    private String name,location;
    private boolean canEdit = true;
    private EditText titleEt, locationEt;
    private TextView start_time,end_time;
    private Button edit_activity;
    private Switch all_day;
    private WeekViewEvent event = new WeekViewEvent();
    private EditEvents editEvents = new EditEvents(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        event = (WeekViewEvent)getIntent().getSerializableExtra("event");
        initViewById();
        initTimePickDialog();

        if(event!=null){
            titleEt.setText(event.getName());
            locationEt.setText(event.getLocation());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            startTime = sdf.format(event.getStartTime().getTime());
            endTime = sdf.format(event.getEndTime().getTime());
            start_time.setText(startTime);
            end_time.setText(endTime);
            canEdit = false;
            startYear = event.getStartTime().get(Calendar.YEAR);
            startMonth = event.getStartTime().get(Calendar.MONTH);
            startDay = event.getStartTime().get(Calendar.DAY_OF_MONTH);
            startHour = event.getStartTime().get(Calendar.HOUR_OF_DAY);
            startMinute =event.getStartTime().get(Calendar.MINUTE);
            endYear = event.getEndTime().get(Calendar.YEAR);
            endMonth = event.getEndTime().get(Calendar.MONTH);
            endDay = event.getEndTime().get(Calendar.DAY_OF_MONTH);
            endHour = event.getEndTime().get(Calendar.HOUR_OF_DAY);
            endMinute = event.getEndTime().get(Calendar.MINUTE);
        }

        all_day.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    initTimePickDialog2();
                }
            }
        });
        edit_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = titleEt.getText().toString().trim();
                location = locationEt.getText().toString().trim();
                try {
                    if (TextUtils.isEmpty(name)) {
                        Toast.makeText(EditActivity.this, "日程不能为空", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(location)) {
                        Toast.makeText(EditActivity.this, "地点不能为空", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(startTime)) {
                        Toast.makeText(EditActivity.this, "开始时间不能为空", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(endTime)) {
                        Toast.makeText(EditActivity.this, "结束时间不能为空", Toast.LENGTH_SHORT).show();
                    } else if (TimeUtil.compare_date(startTime, endTime)) {
                        Toast.makeText(EditActivity.this, "开始时间应该小于结束时间", Toast.LENGTH_SHORT).show();
                    } else if (canEdit) {
                        id++;
                        Calendar startTime = Calendar.getInstance();
                        startTime.set(Calendar.MINUTE, startMinute);
                        startTime.set(Calendar.HOUR_OF_DAY, startHour);
                        startTime.set(Calendar.DAY_OF_MONTH, startDay);
                        startTime.set(Calendar.MONTH, startMonth);
                        startTime.set(Calendar.YEAR, startYear);
                        Calendar endTime = (Calendar) startTime.clone();
                        endTime.set(Calendar.MINUTE, endMinute);
                        endTime.set(Calendar.HOUR_OF_DAY, endHour);
                        endTime.set(Calendar.DAY_OF_MONTH, endDay);
                        endTime.set(Calendar.MONTH, endMonth);
                        endTime.set(Calendar.YEAR, endYear);
                        WeekViewEvent event2 = new WeekViewEvent(id, name, location, startTime, endTime);

                        editEvents.addinfo(event2);
                        Intent intent = new Intent(EditActivity.this, BasicActivity.class);
                        startActivity(intent);
                    } else {

                        Calendar stime = Calendar.getInstance();
                        stime.set(Calendar.MINUTE, startMinute);
                        stime.set(Calendar.HOUR_OF_DAY, startHour);
                        stime.set(Calendar.DAY_OF_MONTH, startDay);
                        stime.set(Calendar.MONTH, startMonth);
                        stime.set(Calendar.YEAR, startYear);
                        Calendar etime = (Calendar) stime.clone();
                        etime.set(Calendar.MINUTE, endMinute);
                        etime.set(Calendar.HOUR_OF_DAY, endHour);
                        etime.set(Calendar.DAY_OF_MONTH, endDay);
                        etime.set(Calendar.MONTH, endMonth);
                        etime.set(Calendar.YEAR, endYear);

                        event.setName(name);
                        event.setLocation(location);
                        event.setStartTime(stime);
                        event.setEndTime(etime);
                        editEvents.update(event);
                        Intent intent = new Intent(EditActivity.this, BasicActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initViewById() {
        titleEt = (EditText) findViewById(R.id.title_et);
        locationEt = (EditText) findViewById(R.id.location_et);
        start_time = (TextView) findViewById(R.id.start_time);
        end_time = (TextView) findViewById(R.id.end_time);
        edit_activity = (Button) findViewById(R.id.add_activity);
        all_day = (Switch) findViewById(R.id.all_day);
    }

    private void initTimePickDialog() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(com.example.calendar.R.string.cancel))
                .setSureStringId(getString(com.example.calendar.R.string.confirm))
                .setTitleStringId("请选择")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(true)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(com.example.calendar.R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(com.example.calendar.R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(com.example.calendar.R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
    }

    private void initTimePickDialog2() {
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId(getString(com.example.calendar.R.string.cancel))
                .setSureStringId(getString(com.example.calendar.R.string.confirm))
                .setTitleStringId("请选择")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(true)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setThemeColor(getResources().getColor(com.example.calendar.R.color.timepicker_dialog_bg))
                .setType(Type.YEAR_MONTH_DAY)
                .setWheelItemTextNormalColor(getResources().getColor(com.example.calendar.R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(com.example.calendar.R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        if (flag == 0) {
            startTime = TimeUtil.StrToDate(millseconds);
            startTime = startTime.substring(0, startTime.length()-3);
            start_time.setText(startTime);
            LocalDateTime datetimeDate = new LocalDateTime(millseconds);
            startYear = datetimeDate.getYear();
            startMonth = datetimeDate.getMonthOfYear()-1;
            startDay = datetimeDate.getDayOfMonth();
            startHour = datetimeDate.getHourOfDay();
            startMinute = datetimeDate.getMinuteOfHour();
        } else {
            endTime = TimeUtil.StrToDate(millseconds);
            endTime = endTime.substring(0, endTime.length()-3);
            end_time.setText(endTime);
            LocalDateTime datetimeDate = new LocalDateTime(millseconds);
            endYear = datetimeDate.getYear();
            endMonth = datetimeDate.getMonthOfYear()-1;
            endDay = datetimeDate.getDayOfMonth();
            endHour = datetimeDate.getHourOfDay();
            endMinute = datetimeDate.getMinuteOfHour();
        }
    }

    public void startTimeClick(View view) {
        flag = 0;
        mDialogAll.show(getSupportFragmentManager(), "all");
    }

    public void endTimeClick(View view) {
        flag = 1;
        mDialogAll.show(getSupportFragmentManager(), "all");
    }
}
