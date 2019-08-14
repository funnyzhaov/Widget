package inc.dailyyoga.widget.bean;

/*

 * -----------------------------------------------------------------

 * Copyright (C) 2019,dailyyoga.inc

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/13 0013 17:17

 * targetVersion:

 * Desc:

 * -----------------------------------------------------------------

 */
public class HttpItem {
    private String urlFiledName;
    private String url;

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
        return "urlFiledName='" + urlFiledName + '\'' +
                ", url='" + url ;
    }
}
