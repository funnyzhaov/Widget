package inc.dailyyoga.widget.bean;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class SceneModel {
    private String key;
    private boolean supportHeader;//是否支持修改Header
    private boolean isOpenHeader;//是否开启
    private boolean isSelect;
    private String effectName;
    private boolean isCanRemove=false;

    public boolean isCanRemove() {
        return isCanRemove;
    }

    public void setCanRemove(boolean canRemove) {
        isCanRemove = canRemove;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isSupportHeader() {
        return supportHeader;
    }

    public void setSupportHeader(boolean supportHeader) {
        this.supportHeader = supportHeader;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isOpenHeader() {
        return isOpenHeader;
    }

    public void setOpenHeader(boolean openHeader) {
        isOpenHeader = openHeader;
    }

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    @Override
    public String toString() {
        return "SceneModel{" +
                "key='" + key + '\'' +
                ", supportHeader=" + supportHeader +
                ", isOpenHeader=" + isOpenHeader +
                ", isSelect=" + isSelect +
                ", effectName='" + effectName + '\'' +
                ", isCanRemove=" + isCanRemove +
                '}';
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SceneModel that = (SceneModel) o;
        return Objects.equals(key, that.key);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
