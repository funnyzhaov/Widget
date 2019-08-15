package inc.dailyyoga.widget;

import android.app.Activity;
import android.content.Context;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
public class SceneManager {
    private String TAG = SceneManager.class.getSimpleName();
    private Context mContext;

    private static SceneManager mInstance; //单例

    //Air View
    private FloatingViewManager mFloatingViewManager;

    //modify
    private String mModifyClassName;
    private List<String> mModifyFiledNameList;

    //key
    private final String HTTP_SCENE_KEY = "HTTP_SCENE_KEY";
    //cache Scene
    private String mCacheKeyName;

    //scenes
    private HashMap<String, List<HttpItem>> mHttpManagerMap;

    //net cached class
    private String mCachedUrlClassName;

    private String mCachedUrlFiledName;

    //first key
    private String mFirstKey;


    private SceneManager() {
    }

    public static SceneManager getInstance() {
        if (mInstance == null) {
            synchronized (SceneManager.class) {
                mInstance = new SceneManager();
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
    public SceneManager setSceneCount(String managerClassName, int count, String... filedName) {
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
     * @param cachedUrlClass 二次缓存url的类
     * @param cachedUrlFiledName 二次缓存url的变量名
     * @return
     */
    public SceneManager setCachedUrlClass(String cachedUrlClass, String cachedUrlFiledName) {
        mCachedUrlClassName = cachedUrlClass;
        mCachedUrlFiledName = cachedUrlFiledName;
        return this;
    }

    /**
     * 设置场景key--多个作用的url
     *
     * @param key 场景名
     * @param url url...
     */
    public SceneManager addScenesUrl(String key, String... url) throws SceneException {
        if (mModifyClassName == null) {
            throw new SceneException("请确保在APP中执行了setSceneCount");
        }
        List<HttpItem> list = new ArrayList<>();
        for (int j = 0; j < mModifyFiledNameList.size(); j++) {
            HttpItem httpItem = new HttpItem();
            httpItem.setUrlFiledName(mModifyFiledNameList.get(j));
            httpItem.setUrl(url[j]);
            list.add(httpItem);
        }
        mHttpManagerMap.put(key, list);
        if (mFirstKey==null){
            mFirstKey=key;
        }
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
        }else {
            //默认第一次存储第一个环境
            SpHelper.getSpHelper().putStringValue(HTTP_SCENE_KEY,mFirstKey).doCommit();
            mCacheKeyName = mFirstKey;
        }
    }

    public String getCurrentScene() {
        return mCacheKeyName;
    }


    public HashMap<String, List<HttpItem>> getHttpMap() {
        return mHttpManagerMap;
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
     * 改变url 全景生效
     * @param sceneKey 场景Key
     */
    public void changeHttpUrlAll(String sceneKey) throws SceneException {
        if (mCachedUrlClassName==null || "".equals(mCachedUrlClassName)
                || mCachedUrlFiledName==null || "".equals(mCachedUrlFiledName)){
            throw new SceneException("请调用setCachedUrlClass设置自定义网络库的信息");
        }
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
            urlFiled.set(object, httpManagerGroup.get(0).getUrl()+"/");
            changeHttpUrlBase(sceneKey);
            SpHelper.getSpHelper().putStringValue(HTTP_SCENE_KEY, sceneKey).doCommit();
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
        } catch (SceneException e){
            e.printStackTrace();
        }

    }

    /**
     * 改变url 重启后生效
     *
     * @param sceneKey 场景key
     */
    public void changeHttpResetApp(String sceneKey) {
        SpHelper.getSpHelper().putStringValue(HTTP_SCENE_KEY, sceneKey).doCommit();
        System.exit(0);
    }

    /**
     * 初始化FloatingView
     *
     * @param baseActivity
     * @return
     */
    public void createFloatingView(Activity baseActivity) {
        //浮窗View
        if (mFloatingViewManager == null) {
            mFloatingViewManager = new FloatingViewManager(baseActivity);
        }
    }

    /**
     * 初始化FloatingView
     *
     * @param baseActivity
     * @return
     */
    public void createFloatingView(Activity baseActivity, int location) {
        //浮窗View
        if (mFloatingViewManager == null) {
            mFloatingViewManager = new FloatingViewManager(baseActivity);
            mFloatingViewManager.setFloatingGravity(location);

        }
    }

    /**
     * 显示Floating
     */
    public void showFloatingView() {
        if (mFloatingViewManager == null) {
            return;
        }
        mFloatingViewManager.showFloating();
    }

    public void hideFloatingView() {
        if (mFloatingViewManager == null) {
            return;
        }
        mFloatingViewManager.hideFloating();
    }


    /**
     * 设置Floating位置
     *
     * @param location
     */
    public void setFloatingGravity(int location) {
        if (mFloatingViewManager == null) {
            return;
        }
        mFloatingViewManager.setFloatingGravity(location);
    }


}
