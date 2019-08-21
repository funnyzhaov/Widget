package inc.dailyyoga.widget.view;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.util.ActivityStack;

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
    private FrameLayout mRootView;
    private int mGravity=0;

    private View mFloatingLayout;
    private Activity mContext;

    //组件
    private Button mFloatingButton;

    public Activity getMActivity(){
        return mContext;
    }

    public FloatingViewManager(Activity context) {
        mContext = context;
    }

    private void initFloatingButton(){
        //获取系统View
        ViewGroup content = mContext.findViewById(android.R.id.content);

        mFloatingLayout = LayoutInflater.from(mContext).inflate(R.layout.dy_scene_foating_box_hint_layout,null,false);
        FrameLayout.LayoutParams layoutParams=  new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= mGravity;
        mFloatingLayout.setLayoutParams(layoutParams);
        //线性布局
        mRootView = new FrameLayout(mContext);
        //添加到根布局
        mRootView.addView(mFloatingLayout);
        //添加View
        content.addView(mRootView);

        initListener();
    }

    private void initListener(){
        mFloatingButton=mFloatingLayout.findViewById(R.id.bt_floating);
        mFloatingButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.bt_floating){
            switchBroad();
        }
    }

    private void switchBroad() {
        if (ActivityStack.hasActivity(FloatingBroadActivity.class.getName())){
            ActivityStack.finishActivity(FloatingBroadActivity.class.getName());
        }else {
        Intent intent=new Intent(mContext,FloatingBroadActivity.class);
        intent.putExtra("className",mContext.getClass().getName());
            mContext.startActivity(intent);
        }
    }

    private void closeFloatingBroad() {
        mFloatingLayout.setVisibility(View.GONE);
    }

    /**
     * 设置浮窗位置
     * @return
     */
    public void setFloatingGravity(int location){

        switch (location){
            case 0:
                mGravity=Gravity.CENTER_HORIZONTAL|Gravity.TOP;
                break;
            case 1:
                mGravity=Gravity.BOTTOM|Gravity.CENTER;
                break;
        }
        initFloatingButton();
    }

    public void showFloating() {
        mFloatingButton.setVisibility(View.VISIBLE);
    }

    public void hideFloating(){
        mFloatingButton.setVisibility(View.GONE);
    }
}
