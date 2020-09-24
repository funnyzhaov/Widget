package com.funnyzhao.widget.kitcomponent.biz.net;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.kitcomponent.biz.net.adapter.FloatingHttpChangeAdapter;
import com.funnyzhao.widget.common.BaseActivity;
import com.funnyzhao.widget.kitcomponent.dialog.AddNetSceneDialog;

public class NetChangeBroadActivity extends BaseActivity {

    //网络组件
    private RecyclerView mHttpChangeView;
    private ImageView mPageBack;
    private FloatingHttpChangeAdapter mHttpChangeAdapter;
    //添加网络场景按钮
    private ImageView mAddSceneIv;
    private AddNetSceneDialog mAddNetSceneDialog;

    //渠道信息
    private TextView mChannelName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fz_scene_net_change_layout);
        initUIXml();
        initHttpAdapter();
        initHelp();
        initEvent();

    }

    /**
     * 初始化辅助的组件
     */
    private void initHelp(){
        mAddNetSceneDialog=new AddNetSceneDialog(this);
        mAddNetSceneDialog.setAddNetSceneListener(new AddNetSceneDialog.AddNetSceneListener() {
            @Override
            public void onAddSuccess() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mHttpChangeAdapter.updateData(NetworkSceneManager.getInstance().getSceneModelArray());
                    }
                },200);
            }
        });
    }

    /**
     * 初始化UI组件
     */
    private void initUIXml(){
        mHttpChangeView=findViewById(R.id.change_http_rv);
        mPageBack=findViewById(R.id.dy_iv_back);
        mAddSceneIv=findViewById(R.id.iv_add_scene);
        mChannelName=findViewById(R.id.iv_channel_name);
        mChannelName.setText(NetworkSceneManager.getInstance().getChannelName());
    }

    /**
     * 初始化事件
     */
    private void initEvent(){
        mPageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAddSceneIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddNetSceneDialog.showVideoDialog();
            }
        });
    }

    private void initHttpAdapter(){
        mHttpChangeAdapter=new FloatingHttpChangeAdapter(this, NetworkSceneManager.getInstance().getSceneModelArray());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHttpChangeView.setLayoutManager(layoutManager);
        mHttpChangeView.setAdapter(mHttpChangeAdapter);
    }

}
