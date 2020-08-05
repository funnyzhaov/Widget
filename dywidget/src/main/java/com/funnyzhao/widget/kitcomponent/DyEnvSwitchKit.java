package com.funnyzhao.widget.kitcomponent;

import android.content.Context;
import android.content.Intent;

import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.kitcomponent.biz.net.NetChangeBroadActivity;

/**
 * 网络切换组件
 */
public class DyEnvSwitchKit extends AbstractKit {
    @Override
    public int getCategory() {
        //业务组件
        return Category.BIZ;
    }

    @Override
    public int getName() {
        return R.string.fz_kit_biz_env_switch;
    }

    @Override
    public int getIcon() {
        return R.drawable.fz_scene_net_icon;
    }

    @Override
    public void onClick(Context context) {
        Intent intent = new Intent(context, NetChangeBroadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
