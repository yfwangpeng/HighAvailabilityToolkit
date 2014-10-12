package com.wp.ha.zk.common;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * 服务端running状态信息
 */
public class ServerRunningData implements Serializable {

    private static final long serialVersionUID = 92260481691800000L;

    private Long              cid;
    private String            address;
    private boolean           active           = true;

    public ServerRunningData(){
    }

    public ServerRunningData(Long cid, String address){
        this.cid = cid;
        this.address = address;
    }

    public Long getCid() {
        return cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
    }

}
