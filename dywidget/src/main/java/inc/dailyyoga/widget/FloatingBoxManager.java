package inc.dailyyoga.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import inc.dailyyoga.widget.bean.AysItem;
import inc.dailyyoga.widget.bean.HeaderModel;
import inc.dailyyoga.widget.bean.HttpItem;
import inc.dailyyoga.widget.bean.SceneModel;
import inc.dailyyoga.widget.cache.SpHelper;
import inc.dailyyoga.widget.exception.SceneException;
import inc.dailyyoga.widget.view.FloatingViewManager;

/*

 * -----------------------------------------------------------------

 * Copyright (C) 2019,dailyyoga.inc

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/13 0013 11:46

 * targetVersion:

 * Desc:

 * -----------------------------------------------------------------

 */
public class FloatingBoxManager {
    private String TAG = FloatingBoxManager.class.getSimpleName();
    private Context mContext;

    private static FloatingBoxManager mInstance; //单例

    //调试盒子
    private FloatingViewManager mFloatingViewManager;
    private static Stack<FloatingViewManager> mFloatingViewStack = new Stack<>();

    /*------------------------网络切换---------------------*/
    //modify
    private String mModifyClassName;
    private List<String> mModifyFiledNameList;

    //key
    private final String HTTP_SCENE_KEY = "HTTP_SCENE_KEY";
    //cache Scene
    private String mCacheKeyName;

    //scenes
    private HashMap<String, List<HttpItem>> mHttpManagerMap;
    private List<SceneModel> mSceneModel;
    //net cached class
    private String mCachedUrlClassName;

    private String mCachedUrlFiledName;

    private boolean hasCachedClass=false;

    //first key
    private String mFirstKey;

    private List<HeaderModel> mHeaderList;
    //请求头变量名
    private String mCachedHeaderFiledName;
    //请求头生效场景名
    private String mCachedHeaderEffectSceneName;

    /*------------------------网络切换---------------------*/


    /*------------------------事件统计---------------------*/
    //统计
    private List<AysItem> mAysList=new ArrayList<>();

    /*------------------------事件统计---------------------*/

    /*------------------------渠道信息---------------------*/
    private String mChannelName;

    private FloatingBoxManager() {
    }

    public static FloatingBoxManager getInstance() {
        if (mInstance == null) {
            synchronized (FloatingBoxManager.class) {
                mInstance = new FloatingBoxManager();
            }
        }
        return mInstance;
    }

    /**
     * 设置场景个数与修改的变量名
     *
     * @param managerClassName 管理url的类全名
     * @param count            场景个数
     * @param filedName        每一个场景下 不同作用的变量名
     */
    public FloatingBoxManager setSceneCount(@NonNull String managerClassName, int count, String... filedName) {
        mModifyClassName = managerClassName;
        mHttpManagerMap = new HashMap<>(count);

        mModifyFiledNameList = new ArrayList<>(filedName.length);
        for (int i = 0; i < filedName.length; i++) {
            mModifyFiledNameList.add(filedName[i]);
        }
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
        mCachedUrlClassName = cachedUrlClass;
        mCachedUrlFiledName = cachedUrlFiledName;
        hasCachedClass=true;
        return this;
    }

    /**
     * 设置场景key--多个作用的url
     *
     * @param key 场景名
     * @param url url...
     */
    public FloatingBoxManager addScenesUrl(String key, String... url){
        List<HttpItem> list = new ArrayList<>();
        for (int j = 0; j < mModifyFiledNameList.size(); j++) {
            HttpItem httpItem = new HttpItem();
            httpItem.setUrlFiledName(mModifyFiledNameList.get(j));
            httpItem.setUrl(url[j]);
            list.add(httpItem);
        }
        mHttpManagerMap.put(key, list);
        if (mFirstKey == null) {
            mFirstKey = key;
        }

        if (mSceneModel==null){
            mSceneModel=new ArrayList<>();
        }
        SceneModel sceneModel=new SceneModel();
        sceneModel.setKey(key);
        mSceneModel.add(sceneModel);

        return this;
    }

    /**
     * 设置场景key--多个作用的url
     *
     * @param key 场景名
     * @param url url...
     */
    private void addScenesUrl(String key, boolean supportHeader,String effectName,String... url){
        List<HttpItem> list = new ArrayList<>();
        for (int j = 0; j < mModifyFiledNameList.size(); j++) {
            HttpItem httpItem = new HttpItem();
            httpItem.setUrlFiledName(mModifyFiledNameList.get(j));
            httpItem.setUrl(url[j]);
            list.add(httpItem);
        }
        mHttpManagerMap.put(key, list);
        if (mFirstKey == null) {
            mFirstKey = key;
        }


        if (mSceneModel==null){
            mSceneModel=new ArrayList<>();
        }
        SceneModel sceneModel=new SceneModel();
        sceneModel.setKey(key);
        sceneModel.setEffectName(effectName);

        if (supportHeader){
            sceneModel.setSupportHeader(true);
        }
        mSceneModel.add(sceneModel);
    }

    /**
     * 添加包含生效请求头的场景
     * @param key 场景名称
     * @param cachedHeaderFiledName 缓存Header变量名
     * @param supportEffectName 支持作用名称
     * @param headers 请求头
     * @param url 场景多url
     * @return
     */
    public FloatingBoxManager addScene(String key,String cachedHeaderFiledName,String supportEffectName,List<HeaderModel> headers,String... url){
        boolean isSupport=true;
        if (!isHasCachedClass()){
            isSupport=false;
            try {
                throw new SceneException("请调用setCachedUrlClass设置自定义网络库的信息");
            } catch (SceneException e) {
                e.printStackTrace();
            }
        }
        addScenesUrl(key,isSupport,supportEffectName,url);
        setHeaderEffectiveScene(key);
        addHeader(headers);

        return this;
    }

    /**
     * 添加包含生效请求头的场景
     * @param key 场景名称
     * @param cachedHeaderFiledName 缓存Header变量名
     * @param supportEffectName 支持作用名称
     * @param headerKey 单个请求头参数
     * @param headerValue 单个请求头值
     * @param url 场景多url
     * @return
     */
    public FloatingBoxManager addScene(String key,String cachedHeaderFiledName,String supportEffectName,String headerKey,String headerValue,String... url){

        boolean isSupport=true;
        if (!isHasCachedClass()){
            try {
                throw new SceneException("请调用setCachedUrlClass设置自定义网络库的信息");
            } catch (SceneException e) {
                e.printStackTrace();
            }
            isSupport=false;
        }
        mCachedHeaderFiledName=cachedHeaderFiledName;
        addScenesUrl(key,isSupport,supportEffectName,url);
        setHeaderEffectiveScene(key);
        addHeader(headerKey,headerValue);
        return this;
    }

    private boolean isHasCachedClass(){
        //判断是否设置了缓存类
        if (mCachedUrlClassName == null || "".equals(mCachedUrlClassName)
                || mCachedUrlFiledName == null || "".equals(mCachedUrlFiledName)) {
            return false;
        }
        return true;
    }

    /**
     * 设置header生效时机
     * @param key 场景名
     * @return
     */
    private void setHeaderEffectiveScene(String key){
        mCachedHeaderEffectSceneName=key;
    }

    /**
     * 添加请求头
     * @param key key
     * @param value value
     */
    private void addHeader(String key,String value){
        if (mHeaderList==null){
            mHeaderList=new ArrayList<>();
        }
        HeaderModel headerModel=new HeaderModel() ;
        headerModel.setHeaderName(key);
        headerModel.setHeaderValue(value);
        mHeaderList.add(headerModel);
    }

    /**
     * 添加请求头
     */
    private void addHeader(List<HeaderModel> headers){
        if (mHeaderList==null){
            mHeaderList=new ArrayList<>();
        }
        mHeaderList.addAll(headers);
    }


    /**
     * 开启初始化场景管理
     *
     * @param context app
     */
    public void startInitScene(Context context) {
        mContext = context;
        SpHelper.initSpHelper(mContext, TAG);

        //检查本地存储，如果有url,替换类的变量值
        boolean isUrlCache = SpHelper.getSpHelper().hasKey(HTTP_SCENE_KEY);
        if (isUrlCache) {
            String cacheKey = SpHelper.getSpHelper().getStringValue(HTTP_SCENE_KEY);
            mCacheKeyName = cacheKey;
            try {
                changeHttpUrlBase(cacheKey);
            } catch (SceneException e) {
                e.printStackTrace();
            }
        } else {
            //默认第一次存储第一个环境
            SpHelper.getSpHelper().putStringValue(HTTP_SCENE_KEY, mFirstKey).doCommit();
            mCacheKeyName = mFirstKey;
        }

    }

    public String getCurrentScene() {
        return mCacheKeyName;
    }

    public HashMap<String, List<HttpItem>> getHttpMap() {
        return mHttpManagerMap;
    }

    public List<SceneModel> getSceneModelArray(){
        return mSceneModel;
    }

    /**
     * 添加渠道信息
     * @param name
     * @return
     */
    public FloatingBoxManager addChannelName(String name){
        mChannelName=name;
        return this;
    }

    public String getChannelName(){
        return mChannelName;
    }
    /**
     * 开启请求头作用生效
     */
    public void openHeaderEffect(){
        Class clz = null;
        try {
            clz = Class.forName(mCachedUrlClassName);
            Field urlFiled = clz.getDeclaredField(mCachedHeaderFiledName);
            Class headerClass=urlFiled.getType();
            Object headerObj=headerClass.newInstance();
            Log.d("HHHOOO",headerClass.getName());
            Method[] methods = headerClass.getDeclaredMethods();
            for (Method m : methods) {
                if (m.getName().equals("put")){
                    for (HeaderModel headerModel: mHeaderList) {
                        m.invoke(headerObj,headerModel.getHeaderName(),headerModel.getHeaderValue());
                    }
                    break;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
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

    /**
     * 改变url
     * @param sceneKey 场景Key
     */
    public void changeHttpUrl(String sceneKey) {

        if (hasCachedClass){
            try {
                changeHttpUrlAll(sceneKey);
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

    private void changeHttpUrlAll(String sceneKey) throws SceneException{
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
    }
    private ChangeUrlInitListener mUrlInitListener;

    /**
     * 设置网络初始化监听
     * @param listener
     * @return
     */
    public FloatingBoxManager setChangeUrlInitListener(ChangeUrlInitListener listener){
        mUrlInitListener=listener;
        return this;
    }



    /**
     * 初始化FloatingView
     *
     * @param baseActivity
     * @return
     */
    public void createFloatingView(Activity baseActivity) {
        //浮窗View
        mFloatingViewManager = new FloatingViewManager(baseActivity);
        mFloatingViewManager.setFloatingGravity(0);
        mFloatingViewStack.push(mFloatingViewManager);
    }

    /**
     * 初始化FloatingView
     *
     * @param baseActivity
     * @return
     */
    public void createFloatingView(Activity baseActivity, int location) {
        //浮窗View
        mFloatingViewManager = new FloatingViewManager(baseActivity);
        mFloatingViewManager.setFloatingGravity(location);
        mFloatingViewStack.push(mFloatingViewManager);
    }

    /**
     * 显示Floating
     */
    public void showFloatingView(Activity activity) {
        for (FloatingViewManager floatingViewManager : mFloatingViewStack) {
            if (floatingViewManager.getMActivity().getClass().getName().equals(activity.getClass().getName())) {
                floatingViewManager.showFloating();
            }
        }
    }

    public void hideFloatingView(Activity activity) {
        Iterator<FloatingViewManager> iterator=mFloatingViewStack.iterator();
        while (iterator.hasNext()){
            FloatingViewManager floatingViewManager=iterator.next();
            if (floatingViewManager.getMActivity().getClass().getName().equals(activity.getClass().getName())) {
                floatingViewManager.hideFloating();
                iterator.remove();
            }
        }
    }

    public void hideFloatingView(String activityClassName) {

        Iterator<FloatingViewManager> iterator=mFloatingViewStack.iterator();
        while (iterator.hasNext()){
            FloatingViewManager floatingViewManager=iterator.next();
            if (floatingViewManager.getMActivity().getClass().getName().equals(activityClassName)) {
                floatingViewManager.hideFloating();
                iterator.remove();
            }
        }
    }

    /**
     * 添加埋点信息
     */
    public void addAysInfo(String eventName,String info){
        if (mAysList.size()>10){
            mAysList.remove(0);
        }
        //存储事件时间  名称 信息 到事件缓存中
        AysItem aysItem=new AysItem();

        long currentTime = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("HH时mm分ss秒");
        Date date = new Date(currentTime);
        String sim = formatter.format(date);

        aysItem.setAysTime(sim);
        aysItem.setAysEventName(eventName);
        aysItem.setAysEventInfo(info);
        mAysList.add(aysItem);
    }

    public List<AysItem> getEventList(){
        return mAysList;
    }

    public void clearEventList(){
        mAysList.clear();
    }

}
