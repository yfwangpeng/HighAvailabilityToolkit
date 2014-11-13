package com.wp.ha.zk.loadbalance;

import com.wp.ha.zk.loadbalance.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.MessageFormat;

/**
 * Created by yfwangpeng.
 */
public class LoadbalanceServerDemo {

    @Before
    public void before(){

    }

    @After
    public void after(){

    }

    @Test
    public void example(){
        final Server server = new Server("127.0.0.1:2181");
        String app =Constant.application;
        String ip = "127.0.0.1";
        String port="12221";
        String parent_node =  MessageFormat.format(Constant.root + Constant.ZK_SEPARATOR + "{0}", app);
        final String node = MessageFormat.format(Constant.root + Constant.ZK_SEPARATOR + "{0}" + Constant.ZK_SEPARATOR + "{1}",
                app, ip + ":" + port);
        server.init(node,parent_node);
        try {
            Thread.sleep(60000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                server.releaseNode(node);
            }
        });
    }
}
