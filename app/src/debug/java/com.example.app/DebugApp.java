package com.example.app;

import com.funnyzhao.widget.FloatingBoxManager;

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
    @Override
    public void onCreate() {
        super.onCreate();
        FloatingBoxManager
                .getInstance()
                .setSceneVersionCode(2)
                .setSceneCount(API.class.getName(),  "BASE_URL", "BASE_H5_URL","BASE_URL_2","BASE_URL_3","BASE_URL_4")
                .addScenesUrl("测试环境", API.T_BASE_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                .addScenesUrl("正式环境", API.O_BASE_URL, API.O_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                .addScenesUrl("冬日环境", API.O_BASE_URL, API.O_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                .addScenesUrl("特殊环境", true,API.T_BASE_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                .install(this);
    }
}
