package com.wp.ha.zk.loadbalance;

import com.wp.ha.zk.loadbalance.client.Client;
import org.I0Itec.zkclient.ZkClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by yfwangpeng.
 */
public class LoadbalanceClientDemo {
    @Before
    public void before(){

    }

    @After
    public void after(){

    }

    @Test
    public void example(){
        String zkServer = "127.0.0.1:2181";
        Client client = new Client(zkServer);
        ZkClient zkClient = new ZkClient(zkServer);
        client.setZkClient(zkClient);
        client.connect(zkClient);
        client.failOver();
        try {
            Thread.sleep(200000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
