package com.funnyzhao.widget.kitcomponent.biz.ays;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.bean.AysItem;
import com.funnyzhao.widget.common.BaseActivity;

public class FilterEventActivity extends BaseActivity {

    private RecyclerView mFilterRy;
    private TextView mTvFilter;
    private FilterEventNameAdapter mFilterEvAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fz_event_filter_activity);
        mFilterRy=findViewById(R.id.ry_eventName);
        mTvFilter=findViewById(R.id.tv_filter);
        mTvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AysManager.getInstance().setFilterNameList(mFilterEvAdapter.getFilterEventName());
                overridePendingTransition(0, 0);
                finish();
            }
        });

        List<AysItem> listEvent=AysManager.getInstance().getEventList();
        List<FilterEventNameBean> eventFilterList=new ArrayList<>();
        LinkedHashSet<String> eventNameSet=new LinkedHashSet<>();

        //去重
        for (AysItem item:listEvent){
            eventNameSet.add(item.getAysEventName());
        }

        //构建数据
        for (String s:eventNameSet){
            FilterEventNameBean bean=new FilterEventNameBean();
            bean.setEventName(s);
            eventFilterList.add(bean);
        }

        mFilterEvAdapter=new FilterEventNameAdapter(this, eventFilterList);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
        mFilterRy.setLayoutManager(layoutManager);
        mFilterRy.setAdapter(mFilterEvAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
