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

import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.R;

public class DeleteSceneHintDialog {
    private Dialog mDialog;
    private Context mContext;
    public DeleteSceneHintDialog(Context context){
        mContext=context;
    }
    public void showVideoDialog(String key) {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView= LayoutInflater.from(mContext).inflate(R.layout.dy_dialog_hint_delete_layout,null);
        mDialog.setContentView(contentView);
        mDialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setGravity(Gravity.CENTER);

        TextView tvRemove=mDialog.findViewById(R.id.tv_remove);
        TextView tvCancel=mDialog.findViewById(R.id.tv_cancel);
        tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingBoxManager.getInstance().removeScenesUrl(key);
                Toast.makeText(mContext, "已移除 "+key+" 环境", Toast.LENGTH_SHORT).show();
                FloatingBoxManager.getInstance().updateSceneQueue();
                if (mListener!=null){
                    mListener.onRemoveSuccess();
                }
                mDialog.dismiss();
            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }

    public interface RemoveSceneListener {
        /**
         * 移除成功
         */
        void onRemoveSuccess();
    }

    public RemoveSceneListener mListener;

    public void setRemoveSceneListener(RemoveSceneListener listener){
        mListener=listener;
    }
}
