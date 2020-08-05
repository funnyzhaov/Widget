package com.funnyzhao.widget.kitcomponent.biz.net.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.bean.HttpItem;
import com.funnyzhao.widget.kitcomponent.biz.net.NetworkSceneManager;

public class NewSceneAdapter extends RecyclerView.Adapter<NewSceneAdapter.SceneHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<HttpItem> mList=new ArrayList<>();
    public NewSceneAdapter(Context context){
        mContext=context;
        mInflater=LayoutInflater.from(mContext);
        mList= NetworkSceneManager.getInstance().getCachedDefaultItems();
    }

    @NonNull
    @Override
    public SceneHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=mInflater.inflate(R.layout.fz_dialog_new_scene_list_item,viewGroup,false);
        return new SceneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SceneHolder sceneHolder, int i) {
        sceneHolder.mSceneEffectName.setText(mList.get(i).getUrlEffectName());
        sceneHolder.mInputHost.setText(mList.get(i).getUrl());
        sceneHolder.mInputHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入完成后自动保存数据
                if (sceneHolder.mInputHost.getText().toString().isEmpty()){
                    return;
                }
                mList.get(i).setUrl(sceneHolder.mInputHost.getText().toString().trim());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class SceneHolder extends RecyclerView.ViewHolder{
        TextView mSceneEffectName;
        EditText mInputHost;
        public SceneHolder(@NonNull View itemView) {
            super(itemView);
            mSceneEffectName=itemView.findViewById(R.id.tv_scene_effect_name);
            mInputHost=itemView.findViewById(R.id.et_input_scene_host);
        }
    }

    public List<HttpItem> getUpdateData(){
        return mList;
    }
}
