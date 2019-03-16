package com.example.memo9;

import android.util.Log;

import com.example.library.WeekViewEvent;
import com.example.memo9.database.EditEvents;

import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by zxy 2019/3/14
 */
public class BasicActivity extends BaseActivity {

    private static final String TAG = "BasicActivity";


    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events ;
        EditEvents editEvents = new EditEvents(this);
        events = editEvents.queryinfo1();
        return events;
    }



}