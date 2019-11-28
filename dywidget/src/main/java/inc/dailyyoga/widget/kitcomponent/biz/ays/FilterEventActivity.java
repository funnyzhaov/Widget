package inc.dailyyoga.widget.kitcomponent.biz.ays;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.bean.AysItem;
import inc.dailyyoga.widget.kitcomponent.biz.DyBaseActivity;

public class FilterEventActivity extends DyBaseActivity {

    private RecyclerView mFilterRy;
    private TextView mTvFilter;
    private FilterEventNameAdapter mFilterEvAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_event_filter_activity);
        mFilterRy=findViewById(R.id.ry_eventName);
        mTvFilter=findViewById(R.id.tv_filter);
        mTvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingBoxManager.getInstance().setFilterNameList(mFilterEvAdapter.getFilterEventName());
                overridePendingTransition(0, 0);
                finish();
            }
        });

        List<AysItem> listEvent=FloatingBoxManager.getInstance().getEventList();
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
