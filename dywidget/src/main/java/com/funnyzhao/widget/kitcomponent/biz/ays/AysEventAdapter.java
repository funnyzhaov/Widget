package com.funnyzhao.widget.kitcomponent.biz.ays;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.bean.AysItem;
import com.funnyzhao.widget.util.JsonFormat;

public class AysEventAdapter extends RecyclerView.Adapter<AysEventAdapter.EventAdapter> {
    private Context mContext;
    private LayoutInflater mInflater;

    private List<AysItem> mList=new ArrayList<>();

    public AysEventAdapter(Context context,List<AysItem> list){
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mList.clear();
        mList.addAll(list);
    }

    public void updateData(List<AysItem> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public EventAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v=mInflater.inflate(R.layout.fz_scene_event_item,viewGroup,false);
        EventAdapter eventAdapter=new EventAdapter(v);
        return eventAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter eventAdapter, int i) {
        eventAdapter.mAysTime.setText("统计时间: "+mList.get(getItemCount()-1-i).getAysTime());
        eventAdapter.mAysEventName.setText("事件名称: "+mList.get(getItemCount()-1-i).getAysEventName());
        eventAdapter.mAysEventInfo.setText(JsonFormat.formatJson(mList.get(getItemCount()-1-i).getAysEventInfo()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class EventAdapter extends RecyclerView.ViewHolder{
        TextView mAysTime;
        TextView mAysEventName;
        TextView mAysEventInfo;

        public EventAdapter(@NonNull View itemView) {
            super(itemView);
            mAysTime=itemView.findViewById(R.id.tv_ays_time);
            mAysEventName=itemView.findViewById(R.id.tv_ays_event_name);
            mAysEventInfo=itemView.findViewById(R.id.tv_ays_event_info);
        }
    }

    public void clearData(){
        mList.clear();
        notifyDataSetChanged();
    }
}
