package com.funnyzhao.widget.kitcomponent.biz.ays;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.bean.AysItem;
import com.funnyzhao.widget.common.BaseActivity;

/**
 * 事件统计面板
 */
public class AysBroadActivity extends BaseActivity {
    private RecyclerView mEventRv;
    private AysEventAdapter mAdapter;

    private ImageView mDyIvBack; //返回键
    private ImageView mClear;  //清除数据
    private ImageView mSelect; //筛选
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fz_scene_ays_activity);
        mEventRv=findViewById(R.id.rv_event);
        mClear=findViewById(R.id.tv_clear);
        mDyIvBack=findViewById(R.id.dy_iv_back);
        mSelect=findViewById(R.id.tv_select);

        mAdapter=new AysEventAdapter(this, AysManager.getInstance().getEventList());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEventRv.setLayoutManager(layoutManager);
        mEventRv.setAdapter(mAdapter);

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.clearData();
                AysManager.getInstance().clearEventList();
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
            List<String> mFilterList=AysManager.getInstance().getFilterEventNameList();
            List<AysItem> totalAysItem=AysManager.getInstance().getEventList();
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
