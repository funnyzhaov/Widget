package inc.dailyyoga.widget.kitcomponent;

import android.content.Context;
import android.content.Intent;

import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.IKit;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.kitcomponent.biz.AysBroadActivity;

/**
 * 事件统计组件
 */
public class DyAysKit implements IKit {
    @Override
    public int getCategory() {
        //业务组件
        return Category.BIZ;
    }

    @Override
    public int getName() {
        return R.string.dy_kit_biz_ays;
    }

    @Override
    public int getIcon() {
        return R.drawable.dy_scene_ays_icon;
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