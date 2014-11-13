package com.wp.ha.zk.loadbalance.client;

import com.wp.ha.zk.loadbalance.RoundRobinLoadBalance;
import com.wp.ha.zk.loadbalance.ZookeeperConnStatistic;
import org.I0Itec.zkclient.ZkClient;

import java.util.Random;

/**
 * Created by yfwangpeng.
 */
public class Client  extends ClusterClient {
    private String runServer;
    private String zkServer ;

    public Client(String zkServer){
        this.zkServer= zkServer;
    }

    @Override
    public void connect(ZkClient zkClient) {
        while (true) {
            try {
                RoundRobinLoadBalance loadBlance = new RoundRobinLoadBalance();
                String server = loadBlance.select(zkServer);
                if (server != null) {
                    String ip = server.split(":")[0];
                    int port = Integer.parseInt(server.split(":")[1]);
                    runServer = server;
                    join("127.0.0.1:" + new Random().nextInt(5000));
                    ZookeeperConnStatistic.incrementConn(zkServer, runServer);
                    break;
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {

                }
                connect(zkClient);
            }
        }
    }

    @Override
    public String getRunServer() {
        return runServer;
    }

}
