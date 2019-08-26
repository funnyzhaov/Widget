package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import inc.dailyyoga.widget.FloatingBoxManager;

/*

 * -----------------------------------------------------------------

 * Copyright (C) 2019,funnyzhao

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/26 0026 -16:35

 * targetVersion:

 * Desc:

 * -----------------------------------------------------------------

 */
public class DebugApp extends TestApp {
    private boolean isCreatedBox;
    @Override
    public void onCreate() {
        super.onCreate();
        //无网络类保存url的信息
        FloatingBoxManager
                .getInstance()
                .setSceneCount(API.class.getName(), 2, "BASE_URL", "BASE_H5_URL")
                .addScenesUrl("测试环境", API.T_BASE_URL, API.T_BASE_H5_URL)
                .addScenesUrl("正式环境", API.O_BASE_URL, API.O_BASE_H5_URL)
                .setChangeUrlInitListener(new FloatingBoxManager.ChangeUrlInitListener() {
                    @Override
                    public void onRestartInit() {
                        //重新初始化Http
                    }
                })
                .startInitScene(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }


            @Override
            public void onActivityStarted(Activity activity) {
                if (activity.getClass().getName().equals(MainActivity.class.getName()) && !isCreatedBox) {
                    FloatingBoxManager.getInstance().createFloatingView(activity);
                    FloatingBoxManager.getInstance().showFloatingView(activity);
                    Toast.makeText(getApplicationContext(), "测试环境", Toast.LENGTH_SHORT).show();
                    isCreatedBox=true;
                }
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
