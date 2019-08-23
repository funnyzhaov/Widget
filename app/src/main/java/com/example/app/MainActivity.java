package com.example.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import inc.dailyyoga.widget.SceneManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //在需要创建调试盒子的地方
        SceneManager.getInstance().createFloatingView(this);
        SceneManager.getInstance().showFloatingView(this);
    }
}
