package inc.dailyyoga.widget.view;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.SceneManager;
import inc.dailyyoga.widget.view.adapter.FloatingHttpChangeAdapter;

/*

 * -----------------------------------------------------------------

 * Copyright (C) 2019,dailyyoga.inc

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/13 0013 16:59

 * targetVersion:

 * Desc:浮窗View管理

 * -----------------------------------------------------------------

 */
public class FloatingViewManager implements View.OnClickListener {

    private View mFloatingLayout;
    private Activity mContext;

    //组件
    private Button mFloatingButton;
    //面板
    private LinearLayout mBox;
    //关闭
    private ImageView mCloseTv;

    //功能组件
    private ImageView mNetArrow;//网络
    private ImageView mAysArrow;//统计

    //网络面板
    private FrameLayout mNetBroad;
    //网络组件
    private RecyclerView mHttpChangeView;
    private FloatingHttpChangeAdapter mHttpChangeAdapter;


    public FloatingViewManager(Activity context) {
        mContext = context;
        initFloatingButton();
    }

    private void initFloatingButton(){
        //获取系统View
        ViewGroup content = mContext.findViewById(android.R.id.content);

        mFloatingLayout = LayoutInflater.from(mContext).inflate(R.layout.floating_view_layout,null,false);
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER_HORIZONTAL|Gravity.TOP;
        mFloatingLayout.setLayoutParams(layoutParams);

        //线性布局
        LinearLayout rootView = new LinearLayout(mContext);
        rootView.setOrientation(LinearLayout.VERTICAL);

        //添加到根布局
        rootView.addView(mFloatingLayout);
        //添加View
        content.addView(rootView);
        initListener();

        List<String> keyList= new ArrayList<>(SceneManager.getInstance().getHttpMap().keySet());
        mHttpChangeAdapter=new FloatingHttpChangeAdapter(mContext,keyList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHttpChangeView.setLayoutManager(layoutManager);
        mHttpChangeView.setAdapter(mHttpChangeAdapter);
    }

    private void initListener(){
        mFloatingButton=mFloatingLayout.findViewById(R.id.bt_floating);
        mBox=mFloatingLayout.findViewById(R.id.ll_box);
        mCloseTv=mFloatingLayout.findViewById(R.id.iv_close);

        //组件
        mNetArrow=mFloatingLayout.findViewById(R.id.iv_net);
        mAysArrow=mFloatingLayout.findViewById(R.id.iv_ays);

        mFloatingButton.setOnClickListener(this);
        mCloseTv.setOnClickListener(this);
        mNetArrow.setOnClickListener(this);
        mAysArrow.setOnClickListener(this);


        mNetBroad=mFloatingLayout.findViewById(R.id.net_change);
        mHttpChangeView=mNetBroad.findViewById(R.id.change_http_rv);
        mNetBroad.setVisibility(View.GONE);
        mBox.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bt_floating){
            switchBroad();
        }
        if (v.getId()==R.id.iv_close){
            closeFloatingBroad();
        }
        if (v.getId()==R.id.iv_net){
            openNetBroad();
        }
    }

    private void openNetBroad() {
        if (mNetBroad.getVisibility()==View.GONE){
            mNetBroad.setVisibility(View.VISIBLE);
        }else {
            mNetBroad.setVisibility(View.GONE);
        }
    }

    private void closeFloatingBroad() {
        mFloatingLayout.setVisibility(View.GONE);
    }

    /**
     * 设置浮窗位置
     * @param location 0 1 2 3 4  0上中   1左上  2 右上  3下左 4 下右
     * @return
     */
    public void setFloatingGravity(int location){
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        int gravity=0;
        switch (location){
            case 0:
                gravity=Gravity.CENTER_HORIZONTAL|Gravity.TOP;
                break;
            case 1:
                gravity=Gravity.LEFT|Gravity.TOP;
                break;
            case 2:
                gravity=Gravity.RIGHT|Gravity.TOP;
                break;
            case 3:
                gravity=Gravity.LEFT|Gravity.BOTTOM;
                break;
            case 4:
                gravity=Gravity.RIGHT|Gravity.BOTTOM;
                break;
        }
        layoutParams.gravity=gravity;
        mFloatingLayout.setLayoutParams(layoutParams);
    }

    private void switchBroad(){
        if (mBox.getVisibility()==View.GONE){
            mBox.setVisibility(View.VISIBLE);
        }else {
            mBox.setVisibility(View.GONE);
        }
    }



    public void showFloating() {
        mFloatingButton.setVisibility(View.VISIBLE);
    }

    public void hideFloating(){
        mFloatingButton.setVisibility(View.GONE);
    }
}
