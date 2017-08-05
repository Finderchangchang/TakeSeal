package dw.take.seal.model;

import java.io.Serializable;

/**
 * 字典
 * Created by Administrator on 2017/8/4.
 */

public class CodeModel implements Serializable {
    private String Key;
    private String Value;
    private String Parameter;
    private boolean is_check;

    public boolean is_check() {
        return is_check;
    }

    public void setIs_check(boolean is_check) {
        this.is_check = is_check;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String parameter) {
        Parameter = parameter;
    }
}
