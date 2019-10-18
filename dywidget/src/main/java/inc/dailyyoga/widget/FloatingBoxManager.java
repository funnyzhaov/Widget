package inc.dailyyoga.widget;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.didichuxing.doraemonkit.DoraemonKit;
import com.didichuxing.doraemonkit.kit.IKit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import inc.dailyyoga.widget.bean.AysItem;
import inc.dailyyoga.widget.bean.HeaderModel;
import inc.dailyyoga.widget.bean.HttpItem;
import inc.dailyyoga.widget.bean.SceneModel;
import inc.dailyyoga.widget.cache.SpHelper;
import inc.dailyyoga.widget.exception.SceneException;
import inc.dailyyoga.widget.kitcomponent.DyAysKit;
import inc.dailyyoga.widget.kitcomponent.DyEnvSwitchKit;

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
    private List<IKit> kits=new ArrayList<>();
    private String TAG = FloatingBoxManager.class.getSimpleName();
    private Context mContext;

    private static FloatingBoxManager mInstance; //单例


    /*------------------------网络切换---------------------*/
    //modify
    private String mModifyClassName;
    private List<String> mModifyFiledNameList;

    //key
    private final String HTTP_SCENE_KEY = "HTTP_SCENE_KEY";
    //网络场景队列key
    private final String HTTP_SCENE_QUEUE_KEY = "HTTP_SCENE_QUEUE_KEY";
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
    //缓存第一次取到的默认key,如果未设置指定场景，则取值
    private String mCachedFirstKey;

    private List<HeaderModel> mHeaderList;
    //请求头变量名
    private String mCachedHeaderFiledName;
    //请求头生效场景名
    private String mCachedHeaderEffectSceneName;
    //缓存默认的域名队列，只保存第一个添加到场景队列中的值，用作动态添加域名时
    private List<HttpItem> mCachedDefaultItems=new ArrayList<>();

    public List<HttpItem> getCachedDefaultItems() {
        return mCachedDefaultItems;
    }

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

    public FloatingBoxManager addScenesUrl(String key,boolean isDefaultScene,String... url){
        if (isDefaultScene){
            mFirstKey=key;
        }
        return addScenesUrl(key,url);
    }

    public FloatingBoxManager addScenesUrlSupportKv(String key,boolean isDefaultScene,String cachedHeaderFiledName,
                                           String supportEffectName,String headerKey,String headerValue,String... url){
        if (isDefaultScene){
            mFirstKey=key;
        }
        return addScene(key,cachedHeaderFiledName,supportEffectName,headerKey,headerValue,url);
    }

    public FloatingBoxManager addScenesUrlSupportHeaders(String key,boolean isDefaultScene,String cachedHeaderFiledName,String supportEffectName,
                                           List<HeaderModel> headers,String... url){
        if (isDefaultScene){
            mFirstKey=key;
        }
        return addScene(key,cachedHeaderFiledName,supportEffectName,headers,url);
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
        if (mCachedFirstKey == null) {
            mCachedFirstKey = key;
            mCachedDefaultItems=list;
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
        if (mCachedFirstKey == null) {
            mCachedFirstKey = key;
            mCachedDefaultItems=list;
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
     * 判断是否设置了缓存类
     * @return
     */
    private boolean isHasCachedClass(){
        if (mCachedUrlClassName == null || "".equals(mCachedUrlClassName)
                || mCachedUrlFiledName == null || "".equals(mCachedUrlFiledName)) {
            return false;
        }else {
            return true;
        }
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
    public void install(Context context,Application application) {
        mContext = context;
        SpHelper.initSpHelper(mContext, TAG);

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
        if (isHasCachedQueue){
            String cacheQueue = SpHelper.getSpHelper().getStringValue(HTTP_SCENE_QUEUE_KEY);
            Gson gson = new Gson();
            HashMap<String,List<HttpItem>> httpQueue = gson.fromJson(cacheQueue, new TypeToken< HashMap<String,List<HttpItem>>>() {
            }.getType());
            mHttpManagerMap=httpQueue;
        }else {
            Gson gson = new Gson();
            String cacheSaveQueue = gson.toJson(mHttpManagerMap);
            SpHelper.getSpHelper().putStringValue(HTTP_SCENE_QUEUE_KEY, cacheSaveQueue).doApply();
        }
        try {
            changeHttpUrlBase(mCacheKeyName);
        } catch (SceneException e) {
            e.printStackTrace();
        }
        kits.add(new DyEnvSwitchKit());
        kits.add(new DyAysKit());
        DoraemonKit.install(application, kits);
        DoraemonKit.disableUpload();//禁止上传信息
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
