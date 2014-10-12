package com.wp.ha.zk;

import com.wp.ha.zk.common.ZkClientx;
import com.wp.ha.zk.server.ServerRunningListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: wp
 */
public class DemoRunningListener extends ServerRunningListener {
    private Logger logger = LoggerFactory.getLogger(DemoRunningListener.class);
    private ZkClientx zkclient;
    private String cluster_node;

    public    DemoRunningListener (ZkClientx zkclient,String cluster_node){
        this.zkclient=zkclient;
        this.cluster_node=cluster_node;
    }

    public void processActiveEnter() {
        try {
            //启动业务逻辑入口
        } finally {

        }
    }

    public void processActiveExit() {
        try {
            //关闭业务逻辑入口
        } finally {

        }
    }

    public void processStart() {
        try {
            if (zkclient != null) {
                initClusterServer(zkclient,cluster_node);
                zkclient.subscribeStateChanges(new IZkStateListener() {

                    public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                        logger.warn("handle State Changed : "+state.toString());
                    }

                    public void handleNewSession() throws Exception {
                        initClusterServer(zkclient,cluster_node);
                    }
                });
            }
        } finally {
        }
    }

    public void processStop() {
        try {
            if (zkclient != null) {
                releaseClusterServer(zkclient,cluster_node);
            }
        } finally {
        }
    }
}
