package inc.dailyyoga.widget.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.view.adapter.AysEventAdapter;

/**
 * 事件统计面板
 */
public class AysBroadActivity extends DyBaseActivity {
    private RecyclerView mEventRv;
    private AysEventAdapter mAdapter;
    private ImageView mDyIvBack;
    ImageView mClear;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_scene_ays_activity);
        mEventRv=findViewById(R.id.rv_event);
        mClear=findViewById(R.id.tv_clear);
        mDyIvBack=findViewById(R.id.dy_iv_back);

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
    }
}
