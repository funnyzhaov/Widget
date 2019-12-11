package inc.dailyyoga.widget.kitcomponent.biz;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.kitcomponent.adapter.FloatingHttpChangeAdapter;
import inc.dailyyoga.widget.kitcomponent.dialog.AddNetSceneDialog;

public class NetChangeBroadActivity extends DyBaseActivity {

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
        setContentView(R.layout.dy_scene_net_change_layout);
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
                        mHttpChangeAdapter.updateData(FloatingBoxManager.getInstance().getSceneModelArray());
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
        mChannelName.setText(FloatingBoxManager.getInstance().getChannelName());
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
        mHttpChangeAdapter=new FloatingHttpChangeAdapter(this, FloatingBoxManager.getInstance().getSceneModelArray());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHttpChangeView.setLayoutManager(layoutManager);
        mHttpChangeView.setAdapter(mHttpChangeAdapter);
    }

}
