## Widget

测试好帮手--调试盒子

### 能做什么

> 告别复杂烦乱的手动改环境打包，一次配置，爽到上线

网络环境切换 

> 数据同学测试埋点，告别一个事件3分钟刷网页等待的痛苦。实时查看，一组测完后，刷新网页对照即可。

埋点事件统计显示

> 性能监控：分析每个页面的卡顿、CPU、内存、线程情况;展示项目中所有SharedPreferences存储情况；系统详细信息



### API

[ API WIKI](https://github.com/funnyzhaov/Widget/wiki/%E6%A0%B8%E5%BF%83API%E8%AF%B4%E6%98%8E)

### 如何使用

last-version :  [ ![Download](https://api.bintray.com/packages/funnyzhaov/maven/boxhelper/images/download.svg?version=2.1.6) ](https://bintray.com/funnyzhaov/maven/boxhelper/2.1.6/link)


#### 1.build.gradle(app) 中

```
implementation 'com.dailyyoga:boxhelper:last-version'

```

#### 2.Application中初始化

```java
public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化
        FloatingBoxManager
                      .getInstance()
                      .setSceneCount(API.class.getName(), 3, "BASE_URL", "BASE_H5_URL")
                      .addScenesUrl("测试环境",true, API.T_BASE_URL, API.T_BASE_H5_URL)
                      .addScenesUrl("正式环境", API.O_BASE_URL, API.O_BASE_H5_URL)
                      .setCachedUrlClass(OHttp.class.getName(),"mBaseUrl") //可传入网络类保存url的信息
                      .addScenesUrlSupportKv("特殊环境","课程支持", API.T_BASE_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                      .addScenesUrlSupportKv("特殊环境","课程支持", API.T_BASE_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL, API.T_BASE_H5_URL)
                      .setChangeUrlInitListener(new FloatingBoxManager.ChangeUrlInitListener() {
                          @Override
                          public void onRestartInit(SceneModel sceneModel) {

                          }

                         @Override
                         public void onSpecialSceneOpen(boolean open) {
                           Toast.makeText(getApplicationContext(), "当前"+open, Toast.LENGTH_SHORT).show();
                         }
                     })
                      .addChannelName("华为") //添加渠道
                      .install(this,this);

        //事件统计
        FloatingBoxManager.getInstance().addAysInfo("eventName","aysInfo");

    }
}

```


#### 3.最新版API

```java
    
    //执行初始化
    void install(Context context,Application application);

    /**
     * @param managerClassName 管理url的类全名
     * @param count            场景个数
     * @param filedName        每一个场景下 不同作用的变量名
     */
    setSceneCount(@NonNull String managerClassName, int count, String... filedName)
    
     /**
      * 设置缓存url类的信息,调用此方法，可以立即修改网络库中的base url
      *
      * @param cachedUrlClass     二次缓存url的类
      * @param cachedUrlFiledName 二次缓存url的变量名
      * @return
      */
    setCachedUrlClass(String cachedUrlClass, String cachedUrlFiledName) 

    /**
     * 添加渠道信息
     * @param name
     * @return
     */
    addChannelName(String name)

    /**
     * @param eventName 事件名称
     * @param info 事件信息
     */
    void addAysInfo(String eventName,String info)

     /**
     * 添加场景下URl的作用名称
     * @param name 与添加的url一一对应
     * @return
     */
     addScenesUrlEffectName(String... name);

    /* 添加特殊作用支持的场景
     * @param key 场景名
     * @param supportEffectName 特殊作用名
     * @param url url...
     * @return
     */
    addScenesUrlSupportKv(String key,String supportEffectName,String... url)

    //需要调用此方法注册监听
     setChangeUrlInitListener(new FloatingBoxManager.ChangeUrlInitListener() {
                    @Override
                    public void onRestartInit(SceneModel sceneModel) {
                     //切换网络场景回调，返回场景信息，若hock不生效，可单独处理网络切换
                    }

                    @Override
                    public void onSpecialSceneOpen(boolean open) {
                     //特殊场景回调 打开或关闭
                       
                    }
                })

    /**
     * 添加场景
     * @param key 场景名称
     * @param url 场景多url
     * @return
     */ 
     addScenesUrl(String key,String... url)

    /**
     * 添加场景
     * @param key 场景名称
     * @param isDefaultScene 是否为默认场景
     * @param url 场景多url
     * @return
     */ 

     addScenesUrl(String key,boolean isDefaultScene,String... url)
     
```

  

