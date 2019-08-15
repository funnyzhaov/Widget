package inc.dailyyoga.widget.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import inc.dailyyoga.widget.util.ActivityStack;

public class DyBaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStack.push(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStack.pop(this);
    }
}
