package inc.dailyyoga.widget.bean;


public class AysItem {
    private String aysTime;
    private String aysEventName;
    private String aysEventInfo;
    public AysItem() {
    }

    public AysItem(String aysEventName) {
        this.aysEventName = aysEventName;
    }

    public String getAysTime() {
        return aysTime;
    }

    public void setAysTime(String aysTime) {
        this.aysTime = aysTime;
    }

    public String getAysEventName() {
        return aysEventName;
    }

    public void setAysEventName(String aysEventName) {
        this.aysEventName = aysEventName;
    }

    public String getAysEventInfo() {
        return aysEventInfo;
    }

    public void setAysEventInfo(String aysEventInfo) {
        this.aysEventInfo = aysEventInfo;
    }

    @Override
    public String toString() {
        return "AysItem{" +
                "aysEventName='" + aysEventName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AysItem aysItem = (AysItem) o;
        return aysEventName.equals(aysItem.aysEventName);
    }

}
