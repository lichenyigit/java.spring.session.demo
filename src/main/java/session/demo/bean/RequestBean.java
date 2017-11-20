package session.demo.bean;

import com.alibaba.fastjson.JSONObject;

public class RequestBean {

    private Integer maxInactiveInterval;//session有效期，单位为秒

    private JSONObject object;

    public Integer getMaxInactiveInterval() {
        return maxInactiveInterval;
    }

    public void setMaxInactiveInterval(int maxInactiveInterval) {
        this.maxInactiveInterval = maxInactiveInterval;
    }

    public JSONObject getObject() {
        return object;
    }

    public void setObject(JSONObject jsonObject) {
        this.object = jsonObject;
    }
}
