package com.wp.ha.zk.common;

/**
 * client running状态信息
 *
 */
public class ClientRunningData {

    private short   clientId;
    private String  address;
    private boolean active = true;

    public ClientRunningData(short   clientId,String  address ){
        this.clientId=clientId;
        this.address=address;
    }

    public short getClientId() {
        return clientId;
    }

    public void setClientId(short clientId) {
        this.clientId = clientId;
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

}
