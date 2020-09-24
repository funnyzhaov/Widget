package com.funnyzhao.widget.kitcomponent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.bean.HttpItem;
import com.funnyzhao.widget.kitcomponent.biz.net.NetworkSceneManager;
import com.funnyzhao.widget.kitcomponent.biz.net.adapter.NewSceneAdapter;

public class AddNetSceneDialog {
    private Dialog mDialog;
    private Context mContext;
    private NewSceneAdapter mSceneAdapter;

    public AddNetSceneDialog(Context context) {
        mContext = context;
        mSceneAdapter=new NewSceneAdapter(context);
    }

    public void showVideoDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.fz_add_net_scene_layout, null);
            mDialog.setContentView(contentView);
            mDialog.setCanceledOnTouchOutside(true);

            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setGravity(Gravity.CENTER);

            //列表
            RecyclerView mSceneList=mDialog.findViewById(R.id.rv_scene_list);
            LinearLayoutManager layoutManager=new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mSceneList.setLayoutManager(layoutManager);
            mSceneList.setAdapter(mSceneAdapter);

            EditText etInputHostName = mDialog.findViewById(R.id.et_input_scene_name);
            TextView tvAddScene = mDialog.findViewById(R.id.tv_add_scene);
            tvAddScene.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etInputHostName.getText().toString().trim().isEmpty()) {
                        Toast.makeText(mContext, "请输入域名名称", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String hostName = etInputHostName.getText().toString().trim();
                    //获取最新的场景内容列表
                    List<HttpItem> httpItemList=mSceneAdapter.getUpdateData();
                    NetworkSceneManager.getInstance().addScenesUrlDIY(hostName,httpItemList);

                    Toast.makeText(mContext, "已添加", Toast.LENGTH_SHORT).show();
                    NetworkSceneManager.getInstance().updateSceneQueue();
                    if (mListener!=null){
                        mListener.onAddSuccess();
                    }
                    mDialog.dismiss();

                }
            });
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    etInputHostName.setText("");
                }
            });
        }
        mDialog.show();
    }

    public interface AddNetSceneListener {
        /**
         * 添加成功
         */
        void onAddSuccess();
    }

    public AddNetSceneListener mListener;

    public void setAddNetSceneListener(AddNetSceneListener listener){
        mListener=listener;
    }

}
