package session.demo.bean;


import org.springframework.session.Session;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MapSessionX{

    private String id;          //sessionid
    private Map<String, Object> sessionAttrs;//session额外属性
    private long creationTime;          //session创建时间
    private long lastAccessedTime;      //最后进入时间,初始化的时候值为创建时间
    private int maxInactiveInterval;       //最大失效时间
    private boolean expired;            //是否过期

    public MapSessionX() {
        this(UUID.randomUUID().toString());
    }

    public MapSessionX(String id) {
        this.sessionAttrs = new HashMap();
        this.creationTime = System.currentTimeMillis();
        this.lastAccessedTime = this.creationTime;
        this.maxInactiveInterval = 1800;
        this.id = id;
    }

    public void setLastAccessedTime(long lastAccessedTime) {
        this.lastAccessedTime = lastAccessedTime;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public String getId() {
        return this.id;
    }

    public long getLastAccessedTime() {
        return this.lastAccessedTime;
    }

    public void setMaxInactiveIntervalInSeconds(int interval) {
        this.maxInactiveInterval = interval;
    }

    public int getMaxInactiveIntervalInSeconds() {
        return this.maxInactiveInterval;
    }

    public boolean isExpired() {
        return this.isExpired(System.currentTimeMillis());
    }

    private boolean isExpired(long now) {
        if (this.maxInactiveInterval < 0) {
            return false;
        } else {
            System.out.println(TimeUnit.SECONDS.toMillis((long)this.maxInactiveInterval));
            System.out.println(now - TimeUnit.SECONDS.toMillis((long)this.maxInactiveInterval));
            return now - TimeUnit.SECONDS.toMillis((long)this.maxInactiveInterval) >= this.lastAccessedTime;
        }
    }

    public void setExpired(boolean expired){
        this.expired = expired;
    }

    public void isExpired(boolean expired){
        this.expired = expired;
    }

    public Object getSessionAttr(String attributeName) {
        return this.sessionAttrs.get(attributeName);
    }

    public Map<String, Object> getSessionAttrs(){
        return this.sessionAttrs;
    }

    //此方法仅为反序列化时有用
    public void setAttributeNames(Set<String> attributeNames) {
    }

    public Set<String> getAttributeNames() {
        return this.sessionAttrs.keySet();
    }

    public void setAttribute(String attributeName, Object attributeValue) {
        if (attributeValue == null) {
            this.removeAttribute(attributeName);
        } else {
            this.sessionAttrs.put(attributeName, attributeValue);
        }

    }

    public void removeAttribute(String attributeName) {
        this.sessionAttrs.remove(attributeName);
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean equals(Object obj) {
        return obj instanceof Session && this.id.equals(((Session)obj).getId());
    }

    public int hashCode() {
        return this.id.hashCode();
    }

}
