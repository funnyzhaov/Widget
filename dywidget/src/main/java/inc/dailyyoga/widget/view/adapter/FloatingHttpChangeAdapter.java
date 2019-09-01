package inc.dailyyoga.widget.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.bean.SceneModel;

/*

 * -----------------------------------------------------------------

 * Copyright (C) 2019,dailyyoga.inc

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/13 0013 17:49

 * targetVersion:

 * Desc:

 * -----------------------------------------------------------------

 */
public class FloatingHttpChangeAdapter extends RecyclerView.Adapter<FloatingHttpChangeAdapter.HttpHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<SceneModel> mSceneList = new ArrayList<>();

    public FloatingHttpChangeAdapter(Context context, List<SceneModel> scenes) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mSceneList.clear();
        mSceneList.addAll(scenes);
        String currentScene = FloatingBoxManager.getInstance().getCurrentScene();

        for (SceneModel sceneModel : mSceneList) {
            if (sceneModel.getKey().equals(currentScene)) {
                sceneModel.setSelect(true);
                break;
            }
        }
    }

    @NonNull
    @Override
    public HttpHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        HttpHolder httpHolder;
        View view = mInflater.inflate(R.layout.floating_http_change_item, viewGroup, false);
        httpHolder = new HttpHolder(view);
        return httpHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HttpHolder httpHolder, final int position) {
        if (mSceneList.get(position).isSelect()) {
            httpHolder.mArrow.setVisibility(View.VISIBLE);
        } else {
            httpHolder.mArrow.setVisibility(View.GONE);
        }
        httpHolder.mScene.setText(mSceneList.get(position).getKey());
        httpHolder.mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingBoxManager.getInstance().changeHttpUrl(mSceneList.get(position).getKey());
                Toast.makeText(mContext, "切换至" + mSceneList.get(position).getKey(), Toast.LENGTH_SHORT).show();

                for (SceneModel sceneModel : mSceneList) {
                    sceneModel.setSelect(false);
                }
                mSceneList.get(position).setSelect(true);
                notifyDataSetChanged();
            }
        });
        if (mSceneList.get(position).isSupportHeader()){
            httpHolder.mHeaderEffect.setVisibility(View.VISIBLE);
        }else {
            httpHolder.mHeaderEffect.setVisibility(View.GONE);
            return;
        }

        if (!mSceneList.get(position).isOpenHeader()){
            httpHolder.mHeaderEffect.setText("未开启 ["+mSceneList.get(position).getEffectName()+"] 支持");
        }else {
            httpHolder.mHeaderEffect.setText("已开启 ["+mSceneList.get(position).getEffectName()+"] 支持");
        }
        httpHolder.mHeaderEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //开启支持
                if (mSceneList.get(position).isSupportHeader()){
                    if (!mSceneList.get(position).isOpenHeader()){
                        FloatingBoxManager.getInstance().openHeaderEffect();
                        mSceneList.get(position).setOpenHeader(true);
                        notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSceneList.size();
    }

    static class HttpHolder extends RecyclerView.ViewHolder {
        private TextView mScene;
        private TextView mChange;
        private ImageView mArrow;
        private TextView mHeaderEffect;//请求头作用支持

        public HttpHolder(@NonNull View itemView) {
            super(itemView);
            mScene = itemView.findViewById(R.id.tv_scene);
            mChange = itemView.findViewById(R.id.tv_scene_change);
            mArrow = itemView.findViewById(R.id.iv_arrow);
            mHeaderEffect=itemView.findViewById(R.id.tv_open_header_effect);
        }
    }
}
