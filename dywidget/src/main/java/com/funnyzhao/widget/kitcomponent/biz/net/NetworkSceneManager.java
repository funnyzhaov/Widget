package com.funnyzhao.widget.kitcomponent.biz.net;

import android.app.Application;
import android.content.Context;
import androidx.annotation.NonNull;

import com.didichuxing.doraemonkit.kit.AbstractKit;
import com.funnyzhao.widget.bean.HttpItem;
import com.funnyzhao.widget.bean.SceneModel;
import com.funnyzhao.widget.cache.SpHelper;
import com.funnyzhao.widget.exception.SceneException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**

 * -----------------------------------------------------------------

 * Copyright (C) 2020

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2020/8/5

 * Desc:网络切换管理类

 * -----------------------------------------------------------------

 */
public class NetworkSceneManager {
    
    private List<AbstractKit> kits=new ArrayList<>();
    private final String TAG = NetworkSceneManager.class.getSimpleName();

    private static NetworkSceneManager mInstance; //单例


    /*------------------------网络切换---------------------*/
    //modify
    private String mModifyClassName;
    private List<String> mModifyFiledNameList;

    //key
    private final String SCENE_VERSION_CODE="SCENE_VERSION_CODE";
    private final String HTTP_SCENE_KEY = "HTTP_SCENE_KEY";
    //网络场景队列key
    private final String HTTP_SCENE_QUEUE_KEY = "HTTP_SCENE_QUEUE_KEY";
    //网络场景UI显示列表Key
    private final String HTTP_SCENE_MODEL_KEY = "HTTP_SCENE_MODEL_KEY";
    //cache Scene
    private String mCacheKeyName;

    //scenes
    private HashMap<String, List<HttpItem>> mHttpManagerMap;
    private List<SceneModel> mSceneModel;
    private List<String> mFiledEffectNameList=new ArrayList<>();//Url作用名
    //net cached class
    private String mCachedUrlClassName;

    private String mCachedUrlFiledName;

    private boolean hasCachedClass=false;

    //first key
    private String mFirstKey;
    //缓存第一次取到的默认key,如果未设置指定场景，则取值
    private String mCachedFirstKey;
    //缓存默认的域名队列，只保存第一个添加到场景队列中的值，用作动态添加域名时
    private List<HttpItem> mCachedDefaultItems=new ArrayList<>();


    //场景版本号
    private int mSceneVersionCode=0;

    //渠道
    private String mChannelName;

    private NetworkSceneManager() {
    }

    /*--------------------------------public域---------------------------------------------*/

    public static NetworkSceneManager getInstance() {
        if (mInstance == null) {
            synchronized (NetworkSceneManager.class) {
                mInstance = new NetworkSceneManager();
            }
        }
        return mInstance;
    }


    /**
     * 设置当前场景的版本号
     *
     * 调用此方法的场景在于当代码初始化时配置的场景发生变化时，来增加版本号或者递减
     *
     * 如果设置的版本号与盒子内存储的版本号不同，则会清除本地缓存，按照最新的场景配置来初始化盒子网络切换
     *
     * 最佳实践：配置变化时，使用0与1来交替设置即可
     *
     * @param versionCode 如果不设置，版本号默认为0
     * @return
     */
    public void setSceneVersionCode(int versionCode){
        mSceneVersionCode=versionCode;
    }

    /**
     * 设置场景个数与修改的变量名
     *
     * @param managerClassName 管理url的类全名
     * @param filedName        每一个场景下 不同作用的变量名
     */
    public void setSceneCount(@NonNull String managerClassName,String... filedName) {
        mModifyClassName = managerClassName;
        mHttpManagerMap = new HashMap<>(4);

        mModifyFiledNameList = new ArrayList<>(filedName.length);
        for (int i = 0; i < filedName.length; i++) {
            mModifyFiledNameList.add(filedName[i]);
        }
    }

    /**
     * 设置缓存url类的信息
     *
     * @param cachedUrlClass     二次缓存url的类
     * @param cachedUrlFiledName 二次缓存url的变量名
     * @return
     */
    public void setCachedUrlClass(String cachedUrlClass, String cachedUrlFiledName) {
        mCachedUrlClassName = cachedUrlClass;
        mCachedUrlFiledName = cachedUrlFiledName;
        hasCachedClass=true;
    }

    public void addScenesUrl(String key,boolean isDefaultScene,String... url){
        if (isDefaultScene){
            mFirstKey=key;
        }
        addScenesNormalUrl(key,url);
    }

    public void addScenesUrl(String key,String... url){
        addScenesNormalUrl(key,url);
    }

    /**
     * 添加特殊作用支持的场景
     * @param key 场景名
     * @param supportEffectName 特殊作用名
     * @param url url...
     * @return
     */
    public void addScenesUrlSupportKv(String key,String supportEffectName,String... url){
         addScene(key,supportEffectName,url);
    }


    /**
     * 添加场景下URl的作用名称
     * @param name
     * @return
     */
    public void addScenesUrlEffectName(String... name){
        mFiledEffectNameList.addAll(Arrays.asList(name));
    }

    /**
     * 设置场景key--多个作用的url
     *
     * @param key 场景名
     * @param listItem
     */
    public void addScenesUrlDIY(String key, List<HttpItem> listItem){
        for (int j = 0; j < mModifyFiledNameList.size(); j++) {
            listItem.get(0).setUrlFiledName(mModifyFiledNameList.get(j));
        }
        mHttpManagerMap.put(key, listItem);

        if (mSceneModel==null){
            mSceneModel=new ArrayList<>();
        }
        SceneModel sceneModel=new SceneModel();
        sceneModel.setKey(key);
        sceneModel.setCanRemove(true);
        mSceneModel.add(sceneModel);
    }

    /**
     * 移除场景
     * @param key
     */
    public void removeScenesUrl(String key){
        //移除UI显示中的场景
        int sceneModelRemoveIndex=-1;
        for (int i=0;i<mSceneModel.size();i++) {
            if (mSceneModel.get(i).getKey().equals(key)){
                sceneModelRemoveIndex=i;
                break;
            }
        }
        if (sceneModelRemoveIndex>=0){
            mSceneModel.remove(sceneModelRemoveIndex);
        }
        //移除本地队列场景
        mHttpManagerMap.remove(key);
    }

    /**
     * 开启初始化场景管理
     *
     * @param context app
     */
    public void install(Context context,Application application) {
        SpHelper.initSpHelper(context, TAG);
        //版本检查
        if (SpHelper.getSpHelper().hasKey(SCENE_VERSION_CODE)){
            int oldVersionCode=SpHelper.getSpHelper().getIntValue(SCENE_VERSION_CODE);
            if (oldVersionCode!=mSceneVersionCode){
                SpHelper.getSpHelper().putIntValue(SCENE_VERSION_CODE,mSceneVersionCode);
                SpHelper.getSpHelper().clearKey(HTTP_SCENE_QUEUE_KEY).doCommit();
                SpHelper.getSpHelper().clearKey(HTTP_SCENE_MODEL_KEY).doCommit();
                SpHelper.getSpHelper().clearKey(HTTP_SCENE_KEY).doCommit();

                SpHelper.getSpHelper().putIntValue(SCENE_VERSION_CODE,mSceneVersionCode).doCommit();
            }
        }else {
            SpHelper.getSpHelper().putIntValue(SCENE_VERSION_CODE,mSceneVersionCode).doCommit();
        }



        //检查本地存储，如果有url,替换类的变量值
        boolean isUrlCache = SpHelper.getSpHelper().hasKey(HTTP_SCENE_KEY);
        if (isUrlCache) {
            String cacheKey = SpHelper.getSpHelper().getStringValue(HTTP_SCENE_KEY);
            mCacheKeyName = cacheKey;
        } else {
            if (mFirstKey==null){
                mFirstKey=mCachedFirstKey;
            }
            SpHelper.getSpHelper().putStringValue(HTTP_SCENE_KEY, mFirstKey).doCommit();
            mCacheKeyName = mFirstKey;
        }

        boolean isHasCachedQueue=SpHelper.getSpHelper().hasKey(HTTP_SCENE_QUEUE_KEY);
        boolean isHasCachedSceneModel=SpHelper.getSpHelper().hasKey(HTTP_SCENE_MODEL_KEY);
        if (isHasCachedQueue && isHasCachedSceneModel){
            String cacheQueue = SpHelper.getSpHelper().getStringValue(HTTP_SCENE_QUEUE_KEY);
            String cachedSM=SpHelper.getSpHelper().getStringValue(HTTP_SCENE_MODEL_KEY);
            Gson gson = new Gson();
            HashMap<String,List<HttpItem>> httpQueue = gson.fromJson(cacheQueue, new TypeToken< HashMap<String,List<HttpItem>>>() {
            }.getType());
            mHttpManagerMap=httpQueue;
            List<SceneModel> sceneMl=gson.fromJson(cachedSM,new TypeToken<List<SceneModel>>(){}.getType());
            mSceneModel=sceneMl;
        }else {
            Gson gson = new Gson();
            String cacheSaveQueue = gson.toJson(mHttpManagerMap);
            SpHelper.getSpHelper().putStringValue(HTTP_SCENE_QUEUE_KEY, cacheSaveQueue).doApply();
            String cachedSceneModel=gson.toJson(mSceneModel);
            SpHelper.getSpHelper().putStringValue(HTTP_SCENE_MODEL_KEY,cachedSceneModel).doApply();
        }
        try {
            changeHttpUrlBase(mCacheKeyName);
        } catch (SceneException e) {
            e.printStackTrace();
        }


        //添加作用名称
        if (!mFiledEffectNameList.isEmpty()) {
            for (int i = 0; i < mCachedDefaultItems.size(); i++) {
                mCachedDefaultItems.get(i).setUrlEffectName(mFiledEffectNameList.get(i));
            }
        }else {
            for (int i = 0; i < mCachedDefaultItems.size(); i++) {
                mCachedDefaultItems.get(i).setUrlEffectName("默认域名"+(i+1));
            }
        }
    }

    /*本地化存储网络队列---------------*/

    public void updateSceneQueue(){
        Gson gson = new Gson();
        String cacheSaveQueue = gson.toJson(mHttpManagerMap);
        SpHelper.getSpHelper().putStringValue(HTTP_SCENE_QUEUE_KEY, cacheSaveQueue).doApply();
        String cachedSceneModel=gson.toJson(mSceneModel);
        SpHelper.getSpHelper().putStringValue(HTTP_SCENE_MODEL_KEY,cachedSceneModel).doApply();
    }

    public String getCurrentScene() {
        return mCacheKeyName;
    }

    public List<SceneModel> getSceneModelArray(){
        String cachedSM=SpHelper.getSpHelper().getStringValue(HTTP_SCENE_MODEL_KEY);
        Gson gson = new Gson();
        List<SceneModel> sceneMl=gson.fromJson(cachedSM,new TypeToken<List<SceneModel>>(){}.getType());
        return sceneMl;
    }

    public void updateSceneModelData(List<SceneModel> sceneModels){
        Gson gson = new Gson();
        mSceneModel=sceneModels;
        String cachedSceneModel=gson.toJson(mSceneModel);
        SpHelper.getSpHelper().putStringValue(HTTP_SCENE_MODEL_KEY,cachedSceneModel).doApply();
    }

    /**
     * 添加渠道信息
     * @param name
     * @return
     */
    public void addChannelName(String name){
        mChannelName=name;
    }

    public String getChannelName(){
        return mChannelName;
    }
    /**
     * 开启请求头作用生效
     */
    public void openHeaderEffect(boolean open){
        if (mUrlInitListener!= null){
            mUrlInitListener.onSpecialSceneOpen(open);
        }
    }


    /**
     * 改变url
     * @param sceneKey 场景Key
     */
    public void changeHttpUrl(String sceneKey) {

        if (hasCachedClass){
            try {
                changeHttpBaseUrlRuntime(sceneKey);
            } catch (SceneException e) {
                e.printStackTrace();
            }
        }else {
            try {
                changeHttpUrlBase(sceneKey);
            } catch (SceneException e) {
                e.printStackTrace();
            }
        }
        if (mUrlInitListener!=null){
            SceneModel sceneModel=new SceneModel();
            sceneModel.setKey(sceneKey);
            mUrlInitListener.onRestartInit(mSceneModel.get(mSceneModel.indexOf(sceneModel)));
        }
        //修改存储的场景，重新初始化应用生效或执行自定义生效方法
        SpHelper.getSpHelper().putStringValue(HTTP_SCENE_KEY, sceneKey).doCommit();
        mCacheKeyName=sceneKey;

    }

    /**
     * 设置网络初始化监听
     * @param listener
     * @return
     */
    public void setChangeUrlInitListener(NetworkSceneManager.ChangeUrlInitListener listener){
        mUrlInitListener=listener;
    }

    public List<HttpItem> getCachedDefaultItems() {
        return mCachedDefaultItems;
    }


    /*--------------------------------私有域---------------------------------------------*/


    /**
     * 设置场景key--多个作用的url
     *
     * @param key 场景名
     * @param url url...
     */
    private void addScenesNormalUrl(String key,String... url){
        if (mHttpManagerMap.containsKey(key)){
            mHttpManagerMap.remove(key);
            SceneModel removeScene=new SceneModel();
            removeScene.setKey(key);
            mSceneModel.remove(removeScene);
        }

        List<HttpItem> list = new ArrayList<>();
        for (int j = 0; j < mModifyFiledNameList.size(); j++) {
            HttpItem httpItem = new HttpItem();
            httpItem.setUrlFiledName(mModifyFiledNameList.get(j));
            httpItem.setUrl(url[j]);
            list.add(httpItem);
        }
        mHttpManagerMap.put(key, list);
        if (mCachedFirstKey == null) {
            mCachedFirstKey = key;
            for (HttpItem item: list) {
                HttpItem cachedItem=new HttpItem();
                cachedItem.setUrl(item.getUrl());
                cachedItem.setUrlFiledName(item.getUrlFiledName());
                mCachedDefaultItems.add(cachedItem);
            }
        }

        if (mSceneModel==null){
            mSceneModel=new ArrayList<>();
        }
        SceneModel sceneModel=new SceneModel();
        sceneModel.setKey(key);
        mSceneModel.add(sceneModel);

    }

    /**
     * 添加包含生效请求头的场景
     * @param key 场景名称
     * @param supportEffectName 支持作用名称
     * @param url 场景多url
     * @return
     */
    private void addScene(String key,String supportEffectName,String... url){
        boolean isSupport=true;
        addScenesUrlSupport(key,isSupport,supportEffectName,url);
    }

    /**
     * 设置场景key--多个作用的url
     *
     * @param key 场景名
     * @param url url...
     */
    private void addScenesUrlSupport(String key, boolean supportHeader,String effectName,String... url){
        List<HttpItem> list = new ArrayList<>();
        for (int j = 0; j < mModifyFiledNameList.size(); j++) {
            HttpItem httpItem = new HttpItem();
            httpItem.setUrlFiledName(mModifyFiledNameList.get(j));
            httpItem.setUrl(url[j]);
            list.add(httpItem);
        }
        mHttpManagerMap.put(key, list);
        if (mCachedFirstKey == null) {
            mCachedFirstKey = key;
            mCachedDefaultItems=list;
        }


        if (mSceneModel==null){
            mSceneModel=new ArrayList<>();
        }
        SceneModel sceneModel=new SceneModel();
        sceneModel.setKey(key);
        if (supportHeader){
            sceneModel.setSupportHeader(true);
            sceneModel.setEffectName(effectName);
        }
        mSceneModel.add(sceneModel);
    }

    /**
     * 改变url 初始管理类生效
     *
     * @param sceneKey 场景Key
     */
    private void changeHttpUrlBase(String sceneKey) throws SceneException {
        List<HttpItem> httpManagerGroup = mHttpManagerMap.get(sceneKey);
        if (httpManagerGroup == null || httpManagerGroup.size() == 0) {
            throw new SceneException("HttpManager数据异常，请检查初始化方法是否传入了null");
        }
        for (HttpItem item : httpManagerGroup) {
            String urlFiledName = item.getUrlFiledName();
            String url = item.getUrl();
            try {
                Class clz = Class.forName(mModifyClassName);
                Object object = clz.newInstance();
                Field urlFiled = clz.getDeclaredField(urlFiledName);
                urlFiled.setAccessible(true);
                urlFiled.set(object, url);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }

    }


    private void changeHttpBaseUrlRuntime(String sceneKey) throws SceneException{
        //判断是否设置了缓存类
        if (mCachedUrlClassName == null || "".equals(mCachedUrlClassName)
                || mCachedUrlFiledName == null || "".equals(mCachedUrlFiledName)) {
            throw new SceneException("请调用setCachedUrlClass设置自定义网络库的信息");
        }
        //修改缓存类
        List<HttpItem> httpManagerGroup = mHttpManagerMap.get(sceneKey);
        if (httpManagerGroup == null || httpManagerGroup.size() == 0) {
            throw new SceneException("HttpManager数据异常，请检查初始化方法是否传入了null");
        }
        try {
            Class clz = Class.forName(mCachedUrlClassName);
            Constructor constructor = clz.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object object = constructor.newInstance();
            Field urlFiled = clz.getDeclaredField(mCachedUrlFiledName);
            urlFiled.setAccessible(true);
            if (httpManagerGroup.get(0).getUrl().lastIndexOf("/")==httpManagerGroup.get(0).getUrl().length()-1){
                urlFiled.set(object, httpManagerGroup.get(0).getUrl());
            }else {
                urlFiled.set(object, httpManagerGroup.get(0).getUrl() + "/");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    public interface ChangeUrlInitListener{
        void onRestartInit(SceneModel sceneModel);
        void onSpecialSceneOpen(boolean open);
    }
    private NetworkSceneManager.ChangeUrlInitListener mUrlInitListener;
}
