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
                .setSceneCount(API.class.getName(), 3, "BASE_URL", "BASE_H5_URL","BASE_URL_2","BASE_URL_3","BASE_URL_4")
                .addScenesUrlEffectName("主域名","H5域名","niha","wode ","jkaixin")
                .addScenesUrl("测试环境",true, API.T_BASE_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                .addScenesUrl("正式环境", API.O_BASE_URL, API.O_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                .addScenesUrlSupportKv("特殊环境","课程支持", API.T_BASE_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                .setChangeUrlInitListener(new FloatingBoxManager.ChangeUrlInitListener() {
                    @Override
                    public void onRestartInit(SceneModel sceneModel) {

                    }

                    @Override
                    public void onSpecialSceneOpen(boolean open) {
//                        Toast.makeText(getApplicationContext(), "当前"+open, Toast.LENGTH_SHORT).show();
                    }
                })
                .addChannelName("华为") //添加渠道
                .install(this,this);
        FloatingBoxManager.getInstance().addAysInfo("clik_h2o","niaho dsasfas");
        FloatingBoxManager.getInstance().addAysInfo("clik_h2o","niaho 2452");
        FloatingBoxManager.getInstance().addAysInfo("tianxia","tianxia 22");
        FloatingBoxManager.getInstance().addAysInfo("happy","haaa 22ds");
        FloatingBoxManager.getInstance().addAysInfo("sad","sad 222");
        FloatingBoxManager.getInstance().addAysInfo("tianxia","tianxia 11");
        FloatingBoxManager.getInstance().addAysInfo("clik_h2o","niaho 27882");
        FloatingBoxManager.getInstance().addAysInfo("happy","haaa 22");
    }
}
