package com.example.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import inc.dailyyoga.widget.FloatingBoxManager;
import inc.dailyyoga.widget.bean.SceneModel;

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
        FloatingBoxManager
                .getInstance()
                .setSceneCount(API.class.getName(), 3, "BASE_URL", "BASE_H5_URL")
                .addScenesUrl("测试环境",true, API.T_BASE_URL, API.T_BASE_H5_URL)
                .addScenesUrl("正式环境", API.O_BASE_URL, API.O_BASE_H5_URL)
                .setCachedUrlClass(OHttp.class.getName(),"mBaseUrl")
                .addScene("特殊环境", "mHeader", "课程支持", "k","v",
                        API.T_BASE_URL, API.T_BASE_H5_URL)
                .addChannelName("华为") //添加渠道
                .install(this,this);

        //无网络类保存url的信息
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }


            @Override
            public void onActivityStarted(Activity activity) {
                if (activity.getClass().getName().equals(MainActivity.class.getName()) && !isCreatedBox) {
                    Toast.makeText(getApplicationContext(), "测试环境", Toast.LENGTH_SHORT).show();
                    FloatingBoxManager.getInstance().addAysInfo("售卖事件1","{ \"name\": \"programer\", \"age\": \"18\" }");
                    FloatingBoxManager.getInstance().addAysInfo("售卖事件2","{ \"name\": \"programer\", \"sie\": \"19\" }");
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
