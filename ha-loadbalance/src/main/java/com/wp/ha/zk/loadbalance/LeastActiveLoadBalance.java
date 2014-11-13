package com.wp.ha.zk.loadbalance;

import org.I0Itec.zkclient.ZkClient;

import java.util.List;

/**
 * Created by yfwangpeng.
 */
public class LeastActiveLoadBalance  implements LoadBlance {

    @Override
    public String select(String zkServer) {
        ZkClient zkClient = new ZkClient(zkServer);
        List<String> serverList = zkClient.getChildren(Constant.root);

        String tempServer = null;
        int tempConn = -1;
        for (int i = 0; i < serverList.size(); i++) {
            String server = serverList.get(i);
            if (zkClient.readData(Constant.root + Constant.ZK_SEPARATOR + server) != null) {
                int connNum = (Integer)zkClient.readData(Constant.root + Constant.ZK_SEPARATOR + server);
                if (tempConn == -1) {
                    tempServer = server;
                    tempConn = connNum;
                }
                if (connNum < tempConn) {
                    tempServer = server;
                    tempConn = connNum;
                }
            }else{
                zkClient.close();
                return server;
            }
        }
        zkClient.close();
        if (tempServer != null && !tempServer.equals("")) {
            return tempServer;
        }

        return null;
    }

}