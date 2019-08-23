package com.example.app;

import android.app.Application;

import inc.dailyyoga.widget.SceneManager;

public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //无网络类保存url的信息
        SceneManager
                .getInstance()
                .setSceneCount(API.class.getName(),2,"BASE_URL","BASE_H5_URL")
                .addScenesUrl("测试环境",API.T_BASE_URL,API.T_BASE_H5_URL)
                .addScenesUrl("正式环境",API.O_BASE_URL,API.O_BASE_H5_URL)
                .setChangeUrlInitListener(new SceneManager.ChangeUrlInitListener() {
                    @Override
                    public void onRestartInit() {
                        //重新初始化Http
                    }
                })
                .startInitScene(this);
        //可传入网络类保存url的信息
        SceneManager
                .getInstance()
                .setSceneCount(API.class.getName(),2,"BASE_URL","BASE_H5_URL")
                .addScenesUrl("测试环境",API.T_BASE_URL,API.T_BASE_H5_URL)
                .addScenesUrl("正式环境",API.O_BASE_URL,API.O_BASE_H5_URL)
                .setCachedUrlClass(OHttp.class.getName(),"mBaseUrl")
                .startInitScene(this);

        //在埋点方法统一入口处调用
        SceneManager.getInstance().addAysInfo("eventName","aysInfo");

    }
}
