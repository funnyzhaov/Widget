package inc.dailyyoga.widget.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.view.adapter.AysEventAdapter;

/**
 * 事件统计面板
 */
public class AysBroadActivity extends DyBaseActivity {
    private RecyclerView mEventRv;
    private AysEventAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_scene_ays_activity);
        mEventRv=findViewById(R.id.rv_event);
        mAdapter=new AysEventAdapter(this, FloatingBoxManager.getInstance().getEventList());
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mEventRv.setLayoutManager(layoutManager);
        mEventRv.setAdapter(mAdapter);
    }
}
