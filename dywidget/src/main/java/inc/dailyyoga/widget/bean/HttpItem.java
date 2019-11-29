package inc.dailyyoga.widget.bean;

/*

 * -----------------------------------------------------------------

 * Copyright (C) 2019,dailyyoga.inc

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/13 0013 17:17

 * targetVersion:

 * Desc:修改 2019.10.25

 * -----------------------------------------------------------------

 */
public class HttpItem {
    private String urlFiledName; //修改的变量名
    private String url;          //地址
    private String urlEffectName="默认域名";//作用名称

    public String getUrlEffectName() {
        return urlEffectName;
    }

    public void setUrlEffectName(String urlEffectName) {
        this.urlEffectName = urlEffectName;
    }

    public String getUrlFiledName() {
        return urlFiledName;
    }

    public void setUrlFiledName(String urlFiledName) {
        this.urlFiledName = urlFiledName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HttpItem{" +
                "urlFiledName='" + urlFiledName + '\'' +
                ", url='" + url + '\'' +
                ", urlEffectName='" + urlEffectName + '\'' +
                '}';
    }
}
