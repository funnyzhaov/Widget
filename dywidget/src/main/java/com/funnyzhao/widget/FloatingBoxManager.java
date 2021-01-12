package com.funnyzhao.widget;

import android.app.Application;
import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.aop.DokitPluginConfig;
import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.funnyzhao.widget.kitcomponent.BaseKit;
import com.funnyzhao.widget.kitcomponent.biz.ays.AysManager;
import com.funnyzhao.widget.kitcomponent.biz.net.NetworkSceneManager;

import java.util.ArrayList;
import java.util.List;

import com.funnyzhao.widget.kitcomponent.DyAysKit;
import com.funnyzhao.widget.kitcomponent.DyEnvSwitchKit;

/**
 * Copyright (C) 2020
 * <p>
 * Author: funnyzhao
 * <p>
 * 研发盒子入口类
 */
public class FloatingBoxManager {

    private List<AbstractKit> kits = new ArrayList<>();
    private static FloatingBoxManager mInstance; //单例

    private FloatingBoxManager() {
    }



    /*--------------------------------public域---------------------------------------------*/

    public static FloatingBoxManager getInstance() {
        if (mInstance == null) {
            synchronized (FloatingBoxManager.class) {
                mInstance = new FloatingBoxManager();
            }
        }
        return mInstance;
    }


    /**
     * 设置当前场景的版本号
     * <p>
     * 调用此方法的场景在于当代码初始化时配置的场景发生变化时，来增加版本号或者递减
     * <p>
     * 如果设置的版本号与盒子内存储的版本号不同，则会清除本地缓存，按照最新的场景配置来初始化盒子网络切换
     * <p>
     * 最佳实践：配置变化时，使用0与1来交替设置即可
     *
     * @param versionCode 如果不设置，版本号默认为0
     * @return
     */
    public FloatingBoxManager setSceneVersionCode(int versionCode) {
        NetworkSceneManager.getInstance().setSceneVersionCode(versionCode);
        return this;
    }

    /**
     * 设置场景个数与修改的变量名
     *
     * @param managerClassName 管理url的类全名
     * @param filedName        每一个场景下 不同作用的变量名
     */
    public FloatingBoxManager setSceneCount(@NonNull String managerClassName, String... filedName) {
        NetworkSceneManager.getInstance().setSceneCount(managerClassName, filedName);
        return this;
    }

    /**
     * 设置缓存url类的信息
     *
     * @param cachedUrlClass     二次缓存url的类
     * @param cachedUrlFiledName 二次缓存url的变量名
     * @return
     */
    public FloatingBoxManager setCachedUrlClass(String cachedUrlClass, String cachedUrlFiledName) {
        NetworkSceneManager.getInstance().setCachedUrlClass(cachedUrlClass, cachedUrlFiledName);
        return this;
    }

    public FloatingBoxManager addScenesUrl(String key, boolean isDefaultScene, String... url) {

        NetworkSceneManager.getInstance().addScenesUrl(key, isDefaultScene, url);
        return this;
    }

    public FloatingBoxManager addScenesUrl(String key, String... url) {
        NetworkSceneManager.getInstance().addScenesUrl(key, url);
        return this;
    }

    /**
     * 添加特殊作用支持的场景
     *
     * @param key               场景名
     * @param supportEffectName 特殊作用名
     * @param url               url...
     * @return
     */
    public FloatingBoxManager addScenesUrlSupportKv(String key, String supportEffectName, String... url) {
        NetworkSceneManager.getInstance().addScenesUrlSupportKv(key, supportEffectName, url);
        return this;
    }


    /**
     * 添加场景下URl的作用名称
     *
     * @param name
     * @return
     */
    public FloatingBoxManager addScenesUrlEffectName(String... name) {
        NetworkSceneManager.getInstance().addScenesUrlEffectName(name);
        return this;
    }


    /**
     * 开启初始化场景管理
     */
    public void install(Application application) {
        NetworkSceneManager.getInstance().install(application.getApplicationContext(), application);
        if (kits.size()==0){
            kits.add(new DyEnvSwitchKit());
            kits.add(new DyAysKit());
        }

        DoraemonKit.disableUpload();//禁止上传信息
        DoraemonKit.setDebug(true);
        DokitPluginConfig.SWITCH_BIG_IMG=true;
        DokitPluginConfig.SWITCH_METHOD=true;
        DoraemonKit.install(application, kits, "");
    }

    /**
     * 添加自定义组件
     * @param kit
     */
    public void addKit(BaseKit kit){
        kits.add(new DyEnvSwitchKit());
        kits.add(new DyAysKit());
        kits.add(kit);
    }


    /**
     * 添加渠道信息
     *
     * @param name
     * @return
     */
    public FloatingBoxManager addChannelName(String name) {
        NetworkSceneManager.getInstance().addChannelName(name);
        return this;
    }


    /**
     * 设置网络初始化监听
     *
     * @param listener
     * @return
     */
    public FloatingBoxManager setChangeUrlInitListener(NetworkSceneManager.ChangeUrlInitListener listener) {
        NetworkSceneManager.getInstance().setChangeUrlInitListener(listener);
        return this;
    }

    /**
     * 添加埋点信息
     */
    public void addAysInfo(String eventName, String info) {
        AysManager.getInstance().addAysInfo(eventName, info);
    }


}
