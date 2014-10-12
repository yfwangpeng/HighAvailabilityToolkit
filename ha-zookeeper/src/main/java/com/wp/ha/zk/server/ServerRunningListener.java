package com.wp.ha.zk.server;

import com.wp.ha.zk.common.ZkClientx;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * 事件监听器
 */
public abstract class ServerRunningListener {


    /**
     * do something when start
     */
    public void processStart(){};

    /**
     * do something when stop
     */
    public void processStop(){};

    public void processActiveEnter(){};

    public void processActiveExit(){};

    /**
     * register addresses for server cluster
     * @param zkClient
     * @param path
     */
    public void initClusterServer(ZkClientx zkClient,String path) {
        if (zkClient != null) {
            try {
                zkClient.createEphemeral(path);
            } catch (ZkNoNodeException e) {
                // 如果父目录不存在，则创建
                String parentDir = path.substring(0, path.lastIndexOf('/'));
                zkClient.createPersistent(parentDir, true);
                zkClient.createEphemeral(path);
            }

        }
    }

    /**
     * unregister addresses for server cluster
     * @param zkClient
     * @param path
     */
    public void releaseClusterServer(ZkClientx zkClient,String path) {
        if (zkClient != null) {
            zkClient.delete(path);
        }
    }

}
