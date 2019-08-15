package inc.dailyyoga.widget.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.SceneManager;
import inc.dailyyoga.widget.exception.SceneException;

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
    private List<String> mKeyList=new ArrayList<>();
    private Context mContext;
    private List<SceneKey> mSceneKeyList=new ArrayList<>();
    public FloatingHttpChangeAdapter(Context context, List<String> keys){
        mContext=context;
        mInflater=LayoutInflater.from(context);
        mKeyList.clear();
        mKeyList.addAll(keys);
        String currentScene=SceneManager.getInstance().getCurrentScene();

        for (String key: mKeyList) {
            SceneKey sceneKey=new SceneKey();
            sceneKey.setKey(key);
            if (key.equals(currentScene)){
                sceneKey.setSelect(true);
            }
            mSceneKeyList.add(sceneKey);
        }
    }

    @NonNull
    @Override
    public HttpHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        HttpHolder httpHolder;
        View view=mInflater.inflate(R.layout.floating_http_change_item,viewGroup,false);
        httpHolder=new HttpHolder(view);
        return httpHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final HttpHolder httpHolder, final int position) {
        if (mSceneKeyList.get(position).isSelect()){
            httpHolder.mArrow.setVisibility(View.VISIBLE);
        }else {
            httpHolder.mArrow.setVisibility(View.GONE);
        }
        httpHolder.mScene.setText(mSceneKeyList.get(position).getKey());
        httpHolder.mChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SceneManager.getInstance().changeHttpUrlAll(mSceneKeyList.get(position).getKey());
                } catch (SceneException e) {
                    e.printStackTrace();
                }
                for (SceneKey sceneKey: mSceneKeyList) {
                    sceneKey.setSelect(false);
                }
                mSceneKeyList.get(position).setSelect(true);
                notifyDataSetChanged();
            }
        });
        httpHolder.mRestChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SceneManager.getInstance().changeHttpResetApp(mSceneKeyList.get(position).getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSceneKeyList.size();
    }

    static class HttpHolder extends RecyclerView.ViewHolder{
        private TextView mScene;
        private TextView mChange;
        private ImageView mArrow;
        private TextView mRestChange;

        public HttpHolder(@NonNull View itemView) {
            super(itemView);
            mScene=itemView.findViewById(R.id.tv_scene);
            mChange=itemView.findViewById(R.id.tv_scene_change);
            mArrow=itemView.findViewById(R.id.iv_arrow);
            mRestChange=itemView.findViewById(R.id.tv_scene_change_reset);
        }
    }
}
