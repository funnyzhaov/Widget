package inc.dailyyoga.widget.kitcomponent.biz.ays;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.bean.AysItem;
import inc.dailyyoga.widget.kitcomponent.biz.DyBaseActivity;

/**
 * 事件统计面板
 */
public class AysBroadActivity extends DyBaseActivity {
    private RecyclerView mEventRv;
    private AysEventAdapter mAdapter;

    private ImageView mDyIvBack; //返回键
    private ImageView mClear;  //清除数据
    private ImageView mSelect; //筛选
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_scene_ays_activity);
        mEventRv=findViewById(R.id.rv_event);
        mClear=findViewById(R.id.tv_clear);
        mDyIvBack=findViewById(R.id.dy_iv_back);
        mSelect=findViewById(R.id.tv_select);

        mAdapter=new AysEventAdapter(this, FloatingBoxManager.getInstance().getEventList());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEventRv.setLayoutManager(layoutManager);
        mEventRv.setAdapter(mAdapter);

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clearData();
                FloatingBoxManager.getInstance().clearEventList();
            }
        });
        mDyIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(0, 0);
                Intent intent=new Intent(AysBroadActivity.this,FilterEventActivity.class);
                startActivityForResult(intent,111);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==111){
            List<String> mFilterList=FloatingBoxManager.getInstance().getFilterEventNameList();
            List<AysItem> totalAysItem=FloatingBoxManager.getInstance().getEventList();
            List<AysItem> mAysItem = new ArrayList<>(totalAysItem);
            List<AysItem> mAysItemUpdate = new ArrayList<>();
            for (AysItem syItem:mAysItem){
                for (String s:mFilterList){
                    if (syItem.getAysEventName().equals(s)){
                        mAysItemUpdate.add(syItem);
                    }
                }
            }


            mAdapter.updateData(mAysItemUpdate);

        }
    }
}
