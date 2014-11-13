package com.wp.ha.zk.loadbalance.server;

import com.wp.ha.zk.loadbalance.Constant;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.text.MessageFormat;
import java.util.List;

/**
 * Created by yfwangpeng.
 */
public class Server {
    private String zkServer ;
    private ZkClient zkclient = null;
    private IZkChildListener childListener;

    public Server(String zkServer){
        this.zkServer = zkServer;
    }

    public void init(String node,String parent_node){
        zkclient  = new ZkClient(zkServer, 1000);
        zkclient.createPersistent(Constant.root, true);
        addNode(node);
        childListener = new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
            }
        };
        zkclient.subscribeChildChanges(parent_node,childListener);
    }

    public void releaseNode(String node){
        zkclient.delete(node);
    }

    public void addNode(String node){
        try {
            zkclient.createEphemeral(node);
        }catch (Exception e){
            String parentDir = node.substring(0, node.lastIndexOf(Constant.ZK_SEPARATOR));
            zkclient.createPersistent(parentDir, true);
            zkclient.createEphemeral(node);
        }
    }

}
