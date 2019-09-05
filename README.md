## Widget

测试好帮手--调试盒子

### 版本介绍



功能：1.网络环境切换  2.埋点事件统计显示

### 如何使用

last-version :  [ ![Download](https://api.bintray.com/packages/funnyzhaov/maven/boxhelper/images/download.svg?version=1.6.3) ](https://bintray.com/funnyzhaov/maven/boxhelper/1.6.3/link)

```
1.build.gradle(app) 中

implementation 'com.dailyyoga:boxhelper:last-version'


2.Application中初始化
public class TestApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //无网络类保存url的信息
        FloatingBoxManager
                .getInstance()
                .setSceneCount(API.class.getName(),2,"BASE_URL","BASE_H5_URL")
                .addScenesUrl("测试环境",API.T_BASE_URL,API.T_BASE_H5_URL)
                .addScenesUrl("正式环境",API.O_BASE_URL,API.O_BASE_H5_URL)
                .setCachedUrlClass(OHttp.class.getName(),"mBaseUrl")//添加url缓存类信息
                .addScene("特殊环境", "mHeader", "课程支持", "k","v",
                                        API.T_BASE_URL, API.T_BASE_H5_URL)//场景支持请求头更改
                 .setChangeUrlInitListener(new FloatingBoxManager.ChangeUrlInitListener() {
                            @Override
                            public void onRestartInit(SceneModel sceneModel) {
                            //切换场景后初始化你的操作
                            }
                 })
                .addChannelName("华为") //添加渠道
                .startInitScene(this);

        //可传入网络类保存url的信息
        FloatingBoxManager
                .getInstance()
                .setSceneCount(API.class.getName(),2,"BASE_URL","BASE_H5_URL")
                .addScenesUrl("测试环境",API.T_BASE_URL,API.T_BASE_H5_URL)
                .addScenesUrl("正式环境",API.O_BASE_URL,API.O_BASE_H5_URL)
                .setCachedUrlClass(OHttp.class.getName(),"mBaseUrl")
                .startInitScene(this);

        //在埋点方法统一入口处调用
        FloatingBoxManager.getInstance().addAysInfo("eventName","aysInfo");

    }
}

3.创建调试盒子在任意页面
 
        FloatingBoxManager.getInstance().createFloatingView(this);
        FloatingBoxManager.getInstance().showFloatingView(this);
```



