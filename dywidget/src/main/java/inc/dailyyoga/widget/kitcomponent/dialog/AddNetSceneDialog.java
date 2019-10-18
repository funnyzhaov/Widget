package inc.dailyyoga.widget.kitcomponent.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.bean.HttpItem;

public class AddNetSceneDialog {
    private Dialog mDialog;
    private Context mContext;

    public AddNetSceneDialog(Context context) {
        mContext = context;
    }

    public void showVideoDialog() {
        if (mDialog == null) {
            mDialog = new Dialog(mContext);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View contentView = LayoutInflater.from(mContext).inflate(R.layout.dy_add_net_scene_layout, null);
            mDialog.setContentView(contentView);
            mDialog.setCanceledOnTouchOutside(true);

            Window dialogWindow = mDialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialogWindow.setGravity(Gravity.CENTER);


            EditText etInputHost = mDialog.findViewById(R.id.et_input_scene_host);
            EditText etInputHostName = mDialog.findViewById(R.id.et_input_scene_name);
            TextView tvAddScene = mDialog.findViewById(R.id.tv_add_scene);
            tvAddScene.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etInputHost.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "请输入域名地址", Toast.LENGTH_SHORT).show();
                    }
                    if (etInputHostName.getText().toString().isEmpty()) {
                        Toast.makeText(mContext, "请输入域名名称", Toast.LENGTH_SHORT).show();
                    }

                    String host = etInputHost.getText().toString().trim();
                    String hostName = etInputHostName.getText().toString().trim();
                    FloatingBoxManager.getInstance().getCachedDefaultItems().get(0).setUrl(host);
                    FloatingBoxManager.getInstance().addScenesUrlDIY(hostName,FloatingBoxManager.getInstance().getCachedDefaultItems());
                    Toast.makeText(mContext, "已添加", Toast.LENGTH_SHORT).show();
                    if (mListener!=null){
                        mListener.onAddSuccess();
                    }
                    mDialog.dismiss();

                }
            });
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    etInputHost.setText("");
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
