package inc.dailyyoga.widget.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.SceneManager;
import inc.dailyyoga.widget.view.adapter.FloatingHttpChangeAdapter;

public class NetChangeBroadActivity extends DyBaseActivity {

    //网络组件
    private RecyclerView mHttpChangeView;
    private FloatingHttpChangeAdapter mHttpChangeAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dy_scene_net_change_layout);
        mHttpChangeView=findViewById(R.id.change_http_rv);
        initHttpAdapter();
    }

    private void initHttpAdapter(){
        List<String> keyList= new ArrayList<>(SceneManager.getInstance().getHttpMap().keySet());
        mHttpChangeAdapter=new FloatingHttpChangeAdapter(this,keyList);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHttpChangeView.setLayoutManager(layoutManager);
        mHttpChangeView.setAdapter(mHttpChangeAdapter);
    }

}
