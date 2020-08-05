package com.funnyzhao.widget.kitcomponent;

import android.content.Context;
import android.content.Intent;

import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.didichuxing.doraemonkit.kit.Category;

import com.funnyzhao.widget.R;
import com.funnyzhao.widget.kitcomponent.biz.ays.AysBroadActivity;

/**
 * 事件统计组件
 */
public class DyAysKit extends AbstractKit {
    @Override
    public int getCategory() {
        //业务组件
        return Category.BIZ;
    }

    @Override
    public int getName() {
        return R.string.fz_kit_biz_ays;
    }

    @Override
    public int getIcon() {
        return R.drawable.fz_scene_ays_icon;
    }

    @Override
    public void onClick(Context context) {
        Intent intent = new Intent(context, AysBroadActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
