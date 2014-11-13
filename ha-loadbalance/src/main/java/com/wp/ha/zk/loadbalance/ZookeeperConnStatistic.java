package com.wp.ha.zk.loadbalance;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by yfwangpeng.
 */
public class ZookeeperConnStatistic  {
    public static void incrementConn(String zkServer,String appServer){
        ZkClient zkClient = new ZkClient(zkServer);
        List<String> serverList = zkClient.getChildren(Constant.root+Constant.ZK_SEPARATOR+Constant.application);
        for(int i=0;i<serverList.size();i++){
            String server=serverList.get(i);
            if(server.equals(appServer)){
                if(zkClient.readData(Constant.root+Constant.ZK_SEPARATOR+Constant.application+Constant.ZK_SEPARATOR+appServer)==null){
                    zkClient.writeData(Constant.root+Constant.ZK_SEPARATOR+Constant.application+Constant.ZK_SEPARATOR+appServer, 1);
                }else{
                    int conn=(Integer)zkClient.readData(Constant.root+Constant.ZK_SEPARATOR+Constant.application+Constant.ZK_SEPARATOR+appServer);
                    zkClient.writeData(Constant.root+Constant.ZK_SEPARATOR+Constant.application+Constant.ZK_SEPARATOR+appServer, ++conn);
                }
                break;
            }
        }
        zkClient.close();
    }

    public static int getNodeConn(String zkServer,String appServer){
        ZkClient zkClient = new ZkClient(zkServer);
        List<String> serverList = zkClient.getChildren(Constant.root);
        for(int i=0;i<serverList.size();i++){
            String server=serverList.get(i);
            if(server.equals(appServer)){
                int conn=(Integer)zkClient.readData(Constant.root+Constant.ZK_SEPARATOR+appServer);
                zkClient.close();
                return conn;
            }
        }
        zkClient.close();
        return 0;
    }
}