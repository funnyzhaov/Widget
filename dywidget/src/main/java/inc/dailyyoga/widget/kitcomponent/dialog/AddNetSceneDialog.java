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

import inc.dailyyoga.widget.R;

public class AddNetSceneDialog {
    private Dialog mDialog;
    private Context mContext;
    public AddNetSceneDialog(Context context){
        mContext=context;
    }
    public void showVideoDialog() {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View contentView= LayoutInflater.from(mContext).inflate(R.layout.dy_add_net_scene_layout,null);
        mDialog.setContentView(contentView);
        mDialog.setCanceledOnTouchOutside(true);

        Window dialogWindow = mDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height=WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setGravity(Gravity.CENTER);


        EditText etInput=mDialog.findViewById(R.id.et_input_scene_host);

        mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {

            }
        });
        mDialog.show();
    }

}
