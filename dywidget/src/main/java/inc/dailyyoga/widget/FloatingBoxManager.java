package inc.dailyyoga.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseArray;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import inc.dailyyoga.widget.bean.AysItem;
import inc.dailyyoga.widget.bean.HttpItem;
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
    //场景key
    private List<String> mKeyArray;

    //net cached class
    private String mCachedUrlClassName;

    private String mCachedUrlFiledName;

    private boolean hasCachedClass=false;

    //first key
    private String mFirstKey;

    private HashMap<String,String> mHeaderMap;
    //请求头变量名
    private String mCachedHeaderFiledName;
    //请求头生效场景名
    private String mCachedHeaderEffectSceneName;
    /*------------------------网络切换---------------------*/


    /*------------------------事件统计---------------------*/
    //统计
    private List<AysItem> mAysList=new ArrayList<>();

    /*------------------------事件统计---------------------*/


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

        if (mKeyArray==null){
            mKeyArray=new ArrayList<>();
        }
        mKeyArray.add(key);
        return this;
    }

    /**
     * 设置header生效时机
     * @param key 场景名
     * @return
     */
    public FloatingBoxManager setHeaderEffectiveScene(String key){
        mCachedHeaderEffectSceneName=key;
        return this;
    }

    /**
     * 添加请求头
     * @param key key
     * @param value value
     */
    public FloatingBoxManager addHeader(String cachedHeaderFiledName,String key,String value){
        if (mHeaderMap==null){
            mHeaderMap=new HashMap<>();
        }
        mHeaderMap.put(key,value);
        return this;
    }

    /**
     * 设置请求头变量名
     * @param cachedHeaderFiledName 缓存类中保存header的变量
     * @return
     */
    public FloatingBoxManager setHeaderCachedFiledName(String cachedHeaderFiledName ){
        mCachedHeaderFiledName=cachedHeaderFiledName;
        return this;
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

    public List<String> getSceneKeyArray(){
        return mKeyArray;
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
                if (mUrlInitListener!=null){
                    mUrlInitListener.onRestartInit();
                }
            } catch (SceneException e) {
                e.printStackTrace();
            }
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
        void onRestartInit();
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

}
