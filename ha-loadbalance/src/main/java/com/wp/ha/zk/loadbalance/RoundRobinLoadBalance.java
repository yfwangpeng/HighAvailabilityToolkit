package com.wp.ha.zk.loadbalance;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by yfwangpeng.
 */
public class RoundRobinLoadBalance implements LoadBlance {

    @Override
    public String select(String zkServer) {
        ZkClient zkClient = new ZkClient(zkServer);
        List<String> serverList = zkClient.getChildren(Constant.root+Constant.ZK_SEPARATOR+Constant.application);
        int round=0;
        if(!zkClient.exists(Constant.round)){
            zkClient.createPersistent(Constant.round);
            zkClient.writeData(Constant.round, 0);
        }else{
            round=(Integer)zkClient.readData(Constant.round);
            zkClient.writeData(Constant.round, ++round);
        }
        zkClient.close();
        if (serverList != null && serverList.size() > 0) {
            return serverList.get(round % serverList.size());
        } else {
            return null;
        }

    }

}
