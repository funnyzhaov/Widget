package com.funnyzhao.widget.kitcomponent.biz.ays;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.funnyzhao.widget.R;

public class FilterEventNameAdapter extends RecyclerView.Adapter<FilterEventNameAdapter.Holder> {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<FilterEventNameBean> mEventNameList=new ArrayList<>();
    private List<String> mEventNameFilterList=new ArrayList<>();
    public FilterEventNameAdapter(Context context, List<FilterEventNameBean> list){
        mEventNameList.clear();
        mEventNameList.addAll(list);
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.fz_event_name_filter_item,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        if (mEventNameList.get(i).getChoose()==0){
            holder.mEventName.setBackground(mContext.getResources().getDrawable(R.drawable.fz_event_name_normal_bg));
        }else {
            holder.mEventName.setBackground(mContext.getResources().getDrawable(R.drawable.fz_event_name_select_item_bg));
        }
        holder.mEventName.setText(mEventNameList.get(i).getEventName());
        holder.mEventName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEventNameList.get(i).getChoose()==0){
                    holder.mEventName.setBackground(mContext.getResources().getDrawable(R.drawable.fz_event_name_select_item_bg));
                    mEventNameFilterList.add(mEventNameList.get(i).getEventName());
                    mEventNameList.get(i).setChoose(1);
                }else {
                    holder.mEventName.setBackground(mContext.getResources().getDrawable(R.drawable.fz_event_name_normal_bg));
                    mEventNameFilterList.remove(mEventNameList.get(i).getEventName());
                    mEventNameList.get(i).setChoose(0);
                }
                notifyDataSetChanged();
            }
        });
    }

    public List<String> getFilterEventName(){
        return mEventNameFilterList;
    }

    @Override
    public int getItemCount() {
        return mEventNameList.size();
    }

    static class Holder extends RecyclerView.ViewHolder{
        TextView mEventName;
        public Holder(@NonNull View itemView) {
            super(itemView);
            mEventName=itemView.findViewById(R.id.tv_event_name);
        }
    }
}
