package com.example.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import inc.dailyyoga.widget.FloatingBoxManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在需要创建调试盒子的地方
        FloatingBoxManager.getInstance().createFloatingView(this);
        FloatingBoxManager.getInstance().showFloatingView(this);
    }
}
