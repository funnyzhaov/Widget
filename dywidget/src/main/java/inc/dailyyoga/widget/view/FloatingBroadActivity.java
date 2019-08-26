package inc.dailyyoga.widget.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.FloatingBoxManager;

public class FloatingBroadActivity extends DyBaseActivity implements View.OnClickListener {
    //关闭
    private ImageView mCloseTv;

    //功能组件
    private FrameLayout mNetArrow;//网络
    private FrameLayout mAysArrow;//统计

    private String mFromClassName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_view_layout);
        mCloseTv=findViewById(R.id.iv_close);
        initListener();
        mFromClassName=getIntent().getStringExtra("className");
    }


    private void initListener(){
        //组件
        mNetArrow=findViewById(R.id.f1);
        mAysArrow=findViewById(R.id.f2);
        mCloseTv.setOnClickListener(this);
        mNetArrow.setOnClickListener(this);
        mAysArrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.iv_close){
            finish();
            FloatingBoxManager.getInstance().hideFloatingView(mFromClassName);
        }
        if (v.getId()==R.id.f1){
            Intent intent=new Intent(this,NetChangeBroadActivity.class);
            startActivity(intent);
        }
        if (v.getId()==R.id.f2){
            Intent intent=new Intent(this,AysBroadActivity.class);
            startActivity(intent);
        }
    }
}
