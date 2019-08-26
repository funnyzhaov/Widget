## Widget

测试好帮手--调试盒子

### 版本介绍

v1.0

功能：1.网络环境切换  2.埋点事件统计显示


### 使用效果

- 盒子展示

![](https://github.com/funnyzhaov/Widget/blob/master/sreenshot/1.jpg)

- 盒子面板

![](https://github.com/funnyzhaov/Widget/blob/master/sreenshot/2.jpg)

- 网络切换

![](https://github.com/funnyzhaov/Widget/blob/master/sreenshot/3.jpg)

### 如何使用

```
1.build.gradle(app) 中

implementation 'com.funnyzhao:debugbox:1.0.0'

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
                .setChangeUrlInitListener(new FloatingBoxManager.ChangeUrlInitListener() {
                    @Override
                    public void onRestartInit() {
                        //重新初始化Http
                    }
                })
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



