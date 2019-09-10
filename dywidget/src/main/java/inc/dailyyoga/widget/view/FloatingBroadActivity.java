package inc.dailyyoga.widget.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zk.qpm.manager.QPMManager;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.FloatingBoxManager;

public class FloatingBroadActivity extends DyBaseActivity implements View.OnClickListener {
    //关闭
    private ImageView mCloseTv;
    private ImageView mBackIv;//返回
    private AlertDialog.Builder builder;
    //功能组件
    private FrameLayout mNetArrow;//网络
    private FrameLayout mAysArrow;//统计
    private FrameLayout mPermArrow;//性能

    private TextView mChannel;//渠道

    private String mFromClassName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_view_layout);
        mCloseTv = findViewById(R.id.iv_close);
        mBackIv = findViewById(R.id.dy_iv_back);
        initListener();
        mFromClassName = getIntent().getStringExtra("className");
        if (!TextUtils.isEmpty(FloatingBoxManager.getInstance().getChannelName())) {
            mChannel.setText(FloatingBoxManager.getInstance().getChannelName());
        } else {
            mChannel.setVisibility(View.GONE);
        }
    }


    private void initListener() {
        //组件
        mNetArrow = findViewById(R.id.f1);
        mAysArrow = findViewById(R.id.f2);
        mChannel = findViewById(R.id.tv_channel);
        mPermArrow=findViewById(R.id.f3);
        mCloseTv.setOnClickListener(this);
        mNetArrow.setOnClickListener(this);
        mAysArrow.setOnClickListener(this);
        mPermArrow.setOnClickListener(this);
        mBackIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_close) {
            if (builder == null) {
                builder = new AlertDialog.Builder(this).setIcon(R.drawable.dy_scene_close_icon).setTitle("关闭提示")
                        .setMessage("关闭后，应用使用期间不再显示，下次重启显示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                                FloatingBoxManager.getInstance().hideFloatingView(mFromClassName);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
            }
            builder.create().show();
        }
        if (v.getId() == R.id.f1) {
            Intent intent = new Intent(this, NetChangeBroadActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.f2) {
            Intent intent = new Intent(this, AysBroadActivity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.dy_iv_back) {
            finish();
        }

        if (v.getId()==R.id.f3){
            hintPerformance();
        }
    }

    private void hintPerformance(){
        if (builder == null) {
            builder = new AlertDialog.Builder(this).setTitle("性能监控窗口")
                    .setMessage("开启或关闭").setPositiveButton("开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (!QPMManager.getInstance().floatViewShow()) {
                                Toast.makeText(FloatingBroadActivity.this, "请开启悬浮窗权限", Toast.LENGTH_SHORT).show();
                            }
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            QPMManager.getInstance().floatViewHide();
                            dialogInterface.dismiss();
                        }
                    });
        }
        builder.create().show();
    }
}
