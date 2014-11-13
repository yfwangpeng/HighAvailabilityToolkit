package com.wp.ha.zk.loadbalance.client;

import com.wp.ha.zk.loadbalance.Constant;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by yfwangpeng.
 */
public abstract class ClusterClient {
    public abstract void connect(ZkClient zkClient);
    public abstract String getRunServer();
    private ZkClient zkClient;

    public void setZkClient(ZkClient zkClient){
        this.zkClient=zkClient;
    }

    public void failOver() {
        zkClient.subscribeChildChanges(Constant.root+Constant.ZK_SEPARATOR+Constant.application, new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List currentChilds) throws Exception {
                boolean has = false;
                for (int i = 0; i < currentChilds.size(); i++) {
                    if (getRunServer().equals(currentChilds.get(i))) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    connect(zkClient);
                }

            }
        });
    }

    public void join(String client){
        if(!zkClient.exists(Constant.client)){
            zkClient.createPersistent(Constant.client);
        }
        if(!zkClient.exists(Constant.client+Constant.ZK_SEPARATOR+client)){
            zkClient.createEphemeral(Constant.client+Constant.ZK_SEPARATOR+client);
        }
    }

    public void leave(String client){
        if(zkClient.exists(Constant.client+Constant.ZK_SEPARATOR+client)){
            zkClient.delete(Constant.client+Constant.ZK_SEPARATOR+client);
        }
        zkClient.close();
    }
}