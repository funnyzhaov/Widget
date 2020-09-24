package com.funnyzhao.widget.kitcomponent.biz.net.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.bean.SceneModel;
import com.funnyzhao.widget.kitcomponent.biz.net.NetworkSceneManager;
import com.funnyzhao.widget.kitcomponent.dialog.DeleteSceneHintDialog;

/**

 * -----------------------------------------------------------------

 * Copyright (C) 2020

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/13 0013 17:49

 * Desc:

 * -----------------------------------------------------------------

 */
public class FloatingHttpChangeAdapter extends RecyclerView.Adapter<FloatingHttpChangeAdapter.HttpHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<SceneModel> mSceneList = new ArrayList<>();
    private DeleteSceneHintDialog mSceneDeleteDialog;

    public FloatingHttpChangeAdapter(Context context, List<SceneModel> scenes) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mSceneDeleteDialog=new DeleteSceneHintDialog(mContext);
        mSceneDeleteDialog.setRemoveSceneListener(new DeleteSceneHintDialog.RemoveSceneListener() {
            @Override
            public void onRemoveSuccess() {
                updateDataRemove(NetworkSceneManager.getInstance().getSceneModelArray());
            }
        });
        updateData(scenes);
    }

    public void updateData(List<SceneModel> scenes){
        mSceneList.clear();
        mSceneList.addAll(scenes);
        updateDataCommon();
    }

    public void updateDataRemove(List<SceneModel> scenes){
        mSceneList.clear();
        mSceneList.addAll(scenes);
        updateDataCommon();
    }

    private void updateDataCommon(){
        String currentScene = NetworkSceneManager.getInstance().getCurrentScene();
        for (SceneModel sceneModel : mSceneList) {
            if (sceneModel.getKey().equals(currentScene)) {
                sceneModel.setSelect(true);
                break;
            }
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HttpHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        HttpHolder httpHolder;
        View view = mInflater.inflate(R.layout.fz_floating_http_change_item, viewGroup, false);
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
        httpHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkSceneManager.getInstance().changeHttpUrl(mSceneList.get(position).getKey());
                Toast.makeText(mContext, "切换至" + mSceneList.get(position).getKey(), Toast.LENGTH_SHORT).show();

                for (SceneModel sceneModel : mSceneList) {
                    sceneModel.setSelect(false);
                }
                mSceneList.get(position).setSelect(true);
                NetworkSceneManager.getInstance().updateSceneModelData(mSceneList);
                notifyDataSetChanged();
            }
        });
        //长按删除功能，若当前环境在应用中，请先切换环境后再删除，否则直接删除
        httpHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!mSceneList.get(position).isCanRemove()){
                    Toast.makeText(mContext, "当前环境不可删除", Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (mSceneList.get(position).isSelect()){
                    Toast.makeText(mContext, "当前环境应用中，请切换后删除", Toast.LENGTH_SHORT).show();
                }else {
                    mSceneDeleteDialog.showVideoDialog(mSceneList.get(position).getKey());
                }
                return false;
            }
        });
        if (mSceneList.get(position).isSupportHeader()){
            httpHolder.mHeaderEffect.setVisibility(View.VISIBLE);
            if (mSceneList.get(position).isOpenHeader()){
                httpHolder.mHeaderEffect.setText("开启状态： ["+mSceneList.get(position).getEffectName()+"]");
                NetworkSceneManager.getInstance().openHeaderEffect(true);
            }else {
                httpHolder.mHeaderEffect.setText("关闭状态： ["+mSceneList.get(position).getEffectName()+"]");
                NetworkSceneManager.getInstance().openHeaderEffect(false);
            }
        }else {
            httpHolder.mHeaderEffect.setVisibility(View.GONE);
            return;
        }

        httpHolder.mHeaderEffect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mSceneList.get(position).isOpenHeader()){
                    mSceneList.get(position).setOpenHeader(true);
                    httpHolder.mHeaderEffect.setText("开启状态： ["+mSceneList.get(position).getEffectName()+"]");
                    NetworkSceneManager.getInstance().openHeaderEffect(true);
                    NetworkSceneManager.getInstance().updateSceneModelData(mSceneList);
                    notifyDataSetChanged();
                }else {
                    mSceneList.get(position).setOpenHeader(false);
                    httpHolder.mHeaderEffect.setText("关闭状态： ["+mSceneList.get(position).getEffectName()+"]");
                    NetworkSceneManager.getInstance().openHeaderEffect(false);
                    NetworkSceneManager.getInstance().updateSceneModelData(mSceneList);
                    notifyDataSetChanged();
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
        private ImageView mArrow;
        private TextView mHeaderEffect;//请求头作用支持

        public HttpHolder(@NonNull View itemView) {
            super(itemView);
            mScene = itemView.findViewById(R.id.tv_scene);
            mArrow = itemView.findViewById(R.id.iv_arrow);
            mHeaderEffect=itemView.findViewById(R.id.tv_open_header_effect);
        }
    }
}
