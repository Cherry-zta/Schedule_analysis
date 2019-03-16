package com.example.memo9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.calendar.module.EventModel;
import com.example.memo9.R;
import com.example.memo9.database.EditEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zxy 2019/3/14
 */

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EVENT_CHECK = 0;
    int mEditMode = EVENT_CHECK;

    private Context mContext;
    private List<EventModel> mSchedules;
    private OnItemClickListener mOnItemClickListener;
    private EditEvents editEvents = new EditEvents(mContext);

    public EventAdapter(Context context) {
        mContext = context;
        mSchedules = new ArrayList<>();
    }

    public void setMSchedule(List<EventModel> events) {
        mSchedules = events;
        if (mSchedules == null || mSchedules.size() == 0) {
            mSchedules = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public List<EventModel> getData() {
        return mSchedules;
    }

    public void clear() {
        mSchedules = null;
        mSchedules = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       return new ScheduleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final EventModel schedule = mSchedules.get(holder.getAdapterPosition());
        final ScheduleViewHolder viewHolder = (ScheduleViewHolder) holder;
        viewHolder.tvScheduleTitle.setText(schedule.getName());
        viewHolder.tvScheduleState.setText(schedule.getF1());
        viewHolder.tvScheduleTime.setText(schedule.getStartTime() + "~" + schedule.getEndTime());
        if (mEditMode == EVENT_CHECK) {
            viewHolder.mCheckBox.setVisibility(View.GONE);
        } else {
            viewHolder.mCheckBox.setVisibility(View.VISIBLE);

            if (schedule.isSelect()) {
                viewHolder.mCheckBox.setImageResource(R.mipmap.ic_checked);
            } else {
                viewHolder.mCheckBox.setImageResource(R.mipmap.ic_uncheck);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), mSchedules);
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
    public interface OnItemClickListener {
        void onItemClickListener(int pos,List<EventModel> mSchedules);
    }
    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mSchedules == null ? 0 : mSchedules.size();
    }

    protected class ScheduleViewHolder extends RecyclerView.ViewHolder {

        protected View vScheduleHintBlock;
        protected TextView tvScheduleState;
        protected TextView tvScheduleTitle;
        protected TextView tvScheduleTime;
        protected ImageView mCheckBox;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            vScheduleHintBlock = itemView.findViewById(R.id.vScheduleHintBlock);
            tvScheduleState = (TextView) itemView.findViewById(R.id.tvScheduleState);
            tvScheduleTitle = (TextView) itemView.findViewById(R.id.tvScheduleTitle);
            tvScheduleTime = (TextView) itemView.findViewById(R.id.tvScheduleTime);
            mCheckBox = (ImageView)itemView.findViewById(R.id.check_box);
        }
    }
}

