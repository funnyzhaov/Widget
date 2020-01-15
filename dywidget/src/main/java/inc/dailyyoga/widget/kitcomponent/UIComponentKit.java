package inc.dailyyoga.widget.kitcomponent;

import android.content.Context;
import android.content.Intent;

import com.didichuxing.doraemonkit.kit.Category;
import com.didichuxing.doraemonkit.kit.IKit;

import inc.dailyyoga.widget.R;
import inc.dailyyoga.widget.kitcomponent.biz.UIComponentWelcomeActivity;

public class UIComponentKit implements IKit {
    @Override
    public int getCategory() {
        return Category.BIZ;
    }

    @Override
    public int getName() {
        return R.string.dy_kit_biz_uic;
    }

    @Override
    public int getIcon() {
        return R.drawable.dy_ui_cop_juice;
    }

    @Override
    public void onClick(Context context) {
        Intent intent = new Intent(context, UIComponentWelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void onAppInit(Context context) {

    }
}
