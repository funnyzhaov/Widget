package com.funnyzhao.widget.kitcomponent.biz.ays;

import com.funnyzhao.widget.bean.AysItem;
import com.funnyzhao.widget.kitcomponent.biz.net.NetworkSceneManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**

 * -----------------------------------------------------------------

 * Copyright (C) 2020

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2020/8/5

 * Desc:埋点事件管理类

 * -----------------------------------------------------------------

 */
public class AysManager {

    private static AysManager mInstance; //单例

    //统计
    private List<AysItem> mAysList=new ArrayList<>();

    private List<String> mFilterNameList=new ArrayList<>();

    private AysManager() {
    }


    /*--------------------------------public域---------------------------------------------*/

    public static AysManager getInstance() {
        if (mInstance == null) {
            synchronized (AysManager.class) {
                mInstance = new AysManager();
            }
        }
        return mInstance;
    }


    /**
     * 添加埋点信息
     */
    public void addAysInfo(String eventName,String info){
        if (mAysList.size()>30){
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

    public List<String> getFilterEventNameList(){
        return mFilterNameList;
    }

    public void setFilterNameList(List<String> mFilterNameList) {
        this.mFilterNameList.clear();
        this.mFilterNameList.addAll(mFilterNameList);
    }

    public void clearEventList(){
        mAysList.clear();
    }
}
