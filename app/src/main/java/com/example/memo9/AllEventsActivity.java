package com.example.memo9;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;

import com.example.calendar.module.EventModel;
import com.example.calendar.util.CalendarUtils;
import com.example.library.WeekViewEvent;
import com.example.memo9.adapter.EventAdapter;
import com.example.memo9.database.EditEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by zxy 2019/3/14
 */

public class AllEventsActivity extends Activity implements View.OnClickListener, EventAdapter.OnItemClickListener{

    private static final int EVENT_CHECK = 0;
    private static final int EVENT_EDIT = 1;
    private RecyclerView recyclerView;
    private TextView mTvSelectNum;
    private Button mBtnDelete;
    private Button mBtnSyn;
    private ImageView mBtnBack;
    private TextView mSelectAll;
    private LinearLayout mLlMycollectionBottomDialog;
    private TextView mBtnEditor;
    private EventAdapter adapter;
    private LinearLayoutManager mLinearLayoutManager;
    private List<EventModel> eventModels = new ArrayList<>();
    private int mEditMode = EVENT_CHECK;
    private boolean isSelectAll = false;
    private boolean editorStatus = false;
    private int index = 0;

    private EditEvents editEvents = new EditEvents(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_schedule);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mTvSelectNum = findViewById(R.id.tv_select_num);
        mBtnDelete = findViewById(R.id.btn_delete);
        mBtnSyn = findViewById(R.id.btn_syn);
        mSelectAll = findViewById(R.id.select_all);
        mLlMycollectionBottomDialog = findViewById(R.id.ll_mycollection_bottom_dialog);
        mBtnEditor = findViewById(R.id.btn_editor);
        mBtnBack = findViewById(R.id.btn_back);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        initView();
        initData();
        initListener();
    }

    /**
     * 组装请求出的通知数据，放至日程中进行显示
     */
    private void compositeEventToMonthDay() {
        List<WeekViewEvent> events = editEvents.queryinfo1();
        eventModels = new ArrayList<>();
        for(int i =0;i<events.size();i++){
            EventModel eventModel = WeekEventToEventModel(events.get(i));
            eventModels.add(eventModel);
        }
    }

    private EventModel WeekEventToEventModel(WeekViewEvent event) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String startTime = sdf.format(event.getStartTime().getTime());
        String endTime = sdf.format(event.getEndTime().getTime());
        EventModel eventModel = new EventModel(event.getName(), startTime, endTime, event.getLocation(), event.getStartTime().get(Calendar.YEAR), event.getStartTime().get(Calendar.MONTH), event.getStartTime().get(Calendar.DAY_OF_MONTH), String.valueOf(event.getId()));
        return eventModel;
    }

    private void initView() {
    }

    private void initData() {
        adapter = new EventAdapter(this);
        mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider_main_bg_height_1));
        recyclerView.addItemDecoration(itemDecorationHeader);
        recyclerView.setAdapter(adapter);
        compositeEventToMonthDay();
        adapter.setMSchedule(eventModels);
    }


    /**
     * 根据选择的数量是否为0来判断按钮的是否可点击.
     *
     * @param size
     */
    private void setBtnBackground(int size) {
        if (size != 0) {
            mBtnDelete.setBackgroundResource(R.drawable.button_shape);
            mBtnDelete.setEnabled(true);
            mBtnDelete.setTextColor(Color.WHITE);
            mBtnSyn.setBackgroundResource(R.drawable.buttn_syn);
            mBtnSyn.setEnabled(true);
            mBtnSyn.setTextColor(Color.WHITE);
        } else {
            mBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            mBtnDelete.setEnabled(false);
            mBtnDelete.setTextColor(ContextCompat.getColor(this, R.color.color_b7b8bd));
            mBtnSyn.setBackgroundResource(R.drawable.button_noclickable_shape);
            mBtnSyn.setEnabled(false);
            mBtnSyn.setTextColor(ContextCompat.getColor(this, R.color.color_b7b8bd));
        }
    }

    private void initListener() {
        adapter.setOnItemClickListener(this);
        mBtnDelete.setOnClickListener(this);
        mBtnSyn.setOnClickListener(this);
        mSelectAll.setOnClickListener(this);
        mBtnEditor.setOnClickListener(this);
        mBtnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_delete:
                deleteVideo();
                break;
            case R.id.btn_syn:
                synEvent();
                break;
            case R.id.select_all:
                selectAllMain();
                break;
            case R.id.btn_editor:
                updataEditMode();
                break;
            case R.id.btn_back:
                backBasic();
                break;
            default:
                break;
        }
    }

    private void backBasic(){
        Intent intent = new Intent();
        intent.setClass(this,BasicActivity.class);
        startActivity(intent);
    }

    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (adapter == null) return;
        if (!isSelectAll) {
            for (int i = 0, j = adapter.getData().size(); i < j; i++) {
                adapter.getData().get(i).setSelect(true);
            }
            index =  adapter.getData().size();
            mBtnDelete.setEnabled(true);
            mSelectAll.setText("取消全选");
            isSelectAll = true;
        } else {
            for (int i = 0, j =  adapter.getData().size(); i < j; i++) {
                adapter.getData().get(i).setSelect(false);
            }
            index = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            isSelectAll = false;
        }
        adapter.notifyDataSetChanged();
        setBtnBackground(index);
        mTvSelectNum.setText(String.valueOf(index));
    }

    /**
     * 删除逻辑
     */
    private void deleteVideo() {
        if (index == 0){
            mBtnDelete.setEnabled(false);
            return;
        }
        final AlertDialog builder = new AlertDialog.Builder(this)
                .create();
        builder.show();
        if (builder.getWindow() == null) return;
        builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
        TextView msg = (TextView) builder.findViewById(R.id.tv_msg);
        Button cancle = (Button) builder.findViewById(R.id.btn_cancle);
        Button sure = (Button) builder.findViewById(R.id.btn_sure);
        if (msg == null || cancle == null || sure == null) return;

        if (index == 1) {
            msg.setText("删除后不可恢复，是否删除该条目？");
        } else {
            msg.setText("删除后不可恢复，是否删除这" + index + "个条目？");
        }
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i =  adapter.getData().size(), j =0 ; i > j; i--) {
                    EventModel eventModel = adapter.getData().get(i-1);
                    if (eventModel.isSelect()) {
                        editEvents.delete(eventModel.getId());
                        adapter.getData().remove(eventModel);
                        index--;
                    }
                }
                index = 0;
                mTvSelectNum.setText(String.valueOf(0));
                setBtnBackground(index);
                if (adapter.getData().size() == 0){
                    mLlMycollectionBottomDialog.setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                builder.dismiss();
            }
        });
    }

    private void synEvent(){
        if (CalendarUtils.requestPermission(this)) {
            String calId = "";
            Cursor userCursor = getContentResolver().query(Uri.parse(CalendarUtils.calanderURL), null,
                    null, null, null);
            if (userCursor.getCount() > 0) {
                userCursor.moveToFirst();
                calId = userCursor.getString(userCursor.getColumnIndex("_id"));
            }
            ContentValues event = new ContentValues();
            for(int i = 0;i<eventModels.size();i++){
                if(eventModels.get(i).isSelect()){
                    event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
                    WeekViewEvent weekViewEvent = editEvents.queryById(eventModels.get(i).getId());
                    event.put("title", weekViewEvent.getName());
                    event.put("description", weekViewEvent.getLocation());
                    //插入账户
                    event.put("calendar_id", calId);

                    Calendar mCalendar = weekViewEvent.getStartTime();
                    mCalendar.set(Calendar.MONTH,weekViewEvent.getStartTime().get(Calendar.MONTH)+1);
                    long start = mCalendar.getTime().getTime();
                    mCalendar = weekViewEvent.getEndTime();
                    mCalendar.set(Calendar.MONTH,weekViewEvent.getEndTime().get(Calendar.MONTH)+1);
                    long end = mCalendar.getTime().getTime();

                    event.put("dtstart", start);
                    event.put("dtend", end);
                    event.put("hasAlarm", 1);

                    Uri newEvent = getContentResolver().insert(Uri.parse(CalendarUtils.calanderEventURL), event);
                    long id = Long.parseLong(newEvent.getLastPathSegment());
                    ContentValues values = new ContentValues();
                    values.put("event_id", id);
                    //提前10分钟有提醒
                    values.put("minutes", 10);
                    getContentResolver().insert(Uri.parse(CalendarUtils.calanderRemiderURL), values);
                }
            }
            Toast.makeText(this,"导入成功",Toast.LENGTH_SHORT).show();
        }
    }



    private void updataEditMode() {
        mEditMode = mEditMode == EVENT_CHECK ? EVENT_EDIT : EVENT_CHECK;
        if (mEditMode == EVENT_EDIT) {
            mBtnEditor.setText("取消");
            for (int i = 0, j =  adapter.getData().size(); i < j; i++) {
                adapter.getData().get(i).setSelect(false);
            }
            index = 0;
            mBtnDelete.setEnabled(false);
            mSelectAll.setText("全选");
            isSelectAll = false;
            mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
            editorStatus = true;
        } else {
            mBtnEditor.setText("编辑");
            mLlMycollectionBottomDialog.setVisibility(View.GONE);
            editorStatus = false;
            clearAll();
        }
        adapter.setEditMode(mEditMode);
    }


    private void clearAll() {
        mTvSelectNum.setText(String.valueOf(0));
        isSelectAll = false;
        mSelectAll.setText("全选");
        setBtnBackground(0);
    }

    @Override
    public void onItemClickListener(int pos, List<EventModel> eventModels) {
        if (editorStatus) {
            EventModel eventModel = eventModels.get(pos);
            boolean isSelect = eventModel.isSelect();
            if (!isSelect) {
                index++;
                eventModel.setSelect(true);
                if (index == eventModels.size()) {
                    isSelectAll = true;
                    mSelectAll.setText("取消全选");
                }

            } else {
                eventModel.setSelect(false);
                index--;
                isSelectAll = false;
                mSelectAll.setText("全选");
            }
            setBtnBackground(index);
            mTvSelectNum.setText(String.valueOf(index));
            adapter.notifyDataSetChanged();
        }else {
            EventModel eventModel = eventModels.get(pos);
            WeekViewEvent event = editEvents.queryById(eventModel.getId());
            event.getStartTime().set(Calendar.MONTH,event.getStartTime().get(Calendar.MONTH)+1);
            event.getEndTime().set(Calendar.MONTH,event.getEndTime().get(Calendar.MONTH)+1);
            Intent intent = new Intent();
            intent.setClass(this, EditActivity.class);
            intent.putExtra("event",event);
            startActivity(intent);
        }
    }
}

