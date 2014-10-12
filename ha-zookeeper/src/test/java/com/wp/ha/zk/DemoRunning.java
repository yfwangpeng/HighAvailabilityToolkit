package com.wp.ha.zk;
import com.wp.ha.zk.common.ServerRunningData;
import com.wp.ha.zk.common.ZkClientx;
import com.wp.ha.zk.server.ServerRunning;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * Created with IntelliJ IDEA.
 * User: wp
 */
public class DemoRunning {
    private  Logger logger ;

    @Before
    public void before(){
        logger= LoggerFactory.getLogger(ServerRunning.class);
    }

    @After
    public void after(){
        try {
            Thread.sleep(30000l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void example(){
        String zkServers="192.168.216.87:2181";
        String cluster_root  = "/wp/ha";
        ZkClientx zkclient = ZkClientx.getZkClient(zkServers);
        zkclient.createPersistent(cluster_root, true);

        String application ="app1";
        Long cid = 1001l;  //server's id registered in zk
        String ip = "127.0.0.1";  //server ip
        String port="12223";  //server port
        String ZOOKEEPER_SEPARATOR= "/";
        String cluster_run = MessageFormat.format(cluster_root+ZOOKEEPER_SEPARATOR+"{0}"+ZOOKEEPER_SEPARATOR+"{1}",
                application, "running");    //running node in zk
        String cluster_node = MessageFormat.format(cluster_root+ZOOKEEPER_SEPARATOR+"{0}"+ZOOKEEPER_SEPARATOR+"{1}"+ZOOKEEPER_SEPARATOR+"{2}",
                application, "cluster", ip + ":" + port);     //cluster nodes in zk

        ServerRunningData serverData = new ServerRunningData(cid, ip + ":" + port);
        final ServerRunning runningMonitor = new ServerRunning(serverData);
        runningMonitor.setClusterRun(cluster_run);
        runningMonitor.setClusterRoot(cluster_root);
        runningMonitor.setListener(new DemoRunningListener(zkclient,cluster_node));
        if (zkclient != null) {
            runningMonitor.setZkClient(zkclient);
        }
        runningMonitor.start();

        Runtime.getRuntime().addShutdownHook(new Thread() { //jvm hook that handle something when stopping
            public void run() {
                try {
                    logger.info("stop the server");
                    if (runningMonitor.isStart()) {
                        runningMonitor.stop();
                    }

                } catch (Throwable e) {
                    logger.warn("something goes wrong when stopping  Server:\n{}",
                            ExceptionUtils.getFullStackTrace(e));
                } finally {
                    logger.info("server is down.");
                }
            }

        });


    }
}
