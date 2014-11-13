package com.wp.ha.zk.loadbalance;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;
import java.util.Random;

/**
 * Created by yfwangpeng.
 */
public class RandomLoadBalance implements LoadBlance {

    @Override
    public String select(String zkServer) {
        ZkClient zkClient = new ZkClient(zkServer);
        List<String> serverList = zkClient.getChildren(Constant.root);
        zkClient.close();
        Random r=new Random();
        if(serverList.size()>=1){
            String server=serverList.get(r.nextInt(serverList.size()));
            return server;
        }else{
            return null;
        }

    }

}
