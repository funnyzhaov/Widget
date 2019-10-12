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

last-version :  [ ![Download](https://api.bintray.com/packages/funnyzhaov/maven/boxhelper/images/download.svg?version=2.0.0) ](https://bintray.com/funnyzhaov/maven/boxhelper/2.0.0/link)


#### 1.build.gradle(app) 中

```
implementation 'com.dailyyoga:boxhelper:last-version'

```

#### 2.Application中初始化

```
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
                      .addScene("特殊环境", "mHeader", "课程支持", "k","v",
                              API.T_BASE_URL, API.T_BASE_H5_URL)
                      .addChannelName("华为") //添加渠道
                      .setChangeUrlInitListener(new FloatingBoxManager.ChangeUrlInitListener() {
                          @Override
                          public void onRestartInit(SceneModel sceneModel) {
                              Log.d("HHHOOO",sceneModel.toString());
                          }
                      })
                      .install(this,this);

        //事件统计
        FloatingBoxManager.getInstance().addAysInfo("eventName","aysInfo");

    }
}

```


#### 3.更多API

依赖后，可查看源码

