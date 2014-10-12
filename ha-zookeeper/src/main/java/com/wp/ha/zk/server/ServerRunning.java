package com.wp.ha.zk.server;

import com.wp.ha.zk.common.*;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.exception.ZkException;
import org.I0Itec.zkclient.exception.ZkInterruptedException;
import org.I0Itec.zkclient.exception.ZkNoNodeException;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yfwangpeng.
 */
public class ServerRunning extends AbstractLifeCycle {
    private static final Logger logger       = LoggerFactory.getLogger(ServerRunning.class);

    private ServerRunningData serverData;       // 当前server节点状态信息
    private volatile ServerRunningData activeData;      // 当前实际运行的server节点状态信息
    private BooleanMutex mutex        = new BooleanMutex(false);
    private volatile boolean           release      = false;
    private ZkClientx zkClient;
    private ServerRunningListener      listener;
    private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
    private int                        delayTime    = 5;
    private IZkDataListener            dataListener;
    private String cluster_run ;
    private String cluster_root;


    public ServerRunning(ServerRunningData serverData){
        this();
        this.serverData = serverData;
    }

    public ServerRunning(){
        dataListener = new IZkDataListener() {

            public void handleDataChange(String dataPath, Object data) throws Exception {
                ServerRunningData runningData = JsonUtils.unmarshalFromByte((byte[]) data, ServerRunningData.class);
                if (!isMine(runningData.getAddress())) {
                    mutex.set(false);
                }

                if (!runningData.isActive() && isMine(runningData.getAddress())) { // 本机主动释放，且之前状态active=true
                    release = true;
                    releaseRunning();
                }

                activeData = runningData;
            }

            public void handleDataDeleted(String dataPath) throws Exception {
                mutex.set(false);
                if (!release && activeData != null && isMine(activeData.getAddress())) {    //上一次实际运行的是本机
                    initRunning();
                } else {    //上一次实际运行的不是本机，等待delayTime，间隔连接
                    delayExector.schedule(new Runnable() {
                        public void run() {
                            initRunning();
                        }
                    }, delayTime, TimeUnit.SECONDS);
                }
            }
        };

    }

    public void setZkClient(ZkClientx zkClient) {
        this.zkClient = zkClient;
    }

    public void start(){
        super.start();
        processStart();
        if (zkClient != null) {
            String path = cluster_run;
            zkClient.subscribeDataChanges(path, dataListener);
            initRunning();
        } else {
            processActiveEnter();// 没有zk，直接启动
        }
    }

    public void stop() {
        super.stop();
        if (zkClient != null) {
            String path = cluster_run;
            zkClient.unsubscribeDataChanges(path, dataListener);
            releaseRunning();
        } else {
            processActiveExit(); // 没有zk，直接启动
        }
        processStop();
    }

    private void processStart() {
        if (listener != null) {
            try {
                listener.processStart();
            } catch (Exception e) {
                logger.error("processStart failed", e);
            }
        }
    }

    private void processStop() {
        if (listener != null) {
            try {
                listener.processStop();
            } catch (Exception e) {
                logger.error("processStop failed", e);
            }
        }
    }

    public void setListener(ServerRunningListener listener) {
        this.listener = listener;
    }

    /**
     * 注册server 的running地址
     */
    private void initRunning() {
        if (!isStart()) {
            return;
        }
        String path = cluster_run;
        byte[] bytes = JsonUtils.marshalToByte(serverData); // 序列化
        try {
            mutex.set(false);
            zkClient.create(path, bytes, CreateMode.EPHEMERAL);
            activeData = serverData;
            processActiveEnter();// 触发启动业务逻辑的事件
            mutex.set(true);
        } catch (ZkNodeExistsException e) {
            bytes = zkClient.readData(path, true);
            if (bytes == null) {// 如果不存在节点，立即尝试一次
                initRunning();
            } else {
                activeData = JsonUtils.unmarshalFromByte(bytes, ServerRunningData.class);
            }
        } catch (ZkNoNodeException e) {
            zkClient.createPersistent(cluster_root, true); // 尝试创建父节点
            initRunning();
        }
    }

    private void processActiveEnter() {
        if (listener != null) {
            try {
                listener.processActiveEnter();
            } catch (Exception e) {
                logger.error("processActiveEnter failed", e);
            }
        }
    }

    /**
     * 注销server 的running地址
     * @return true or false
     */
    private boolean releaseRunning() {
        if (check()) {
            String path = cluster_run;
            zkClient.delete(path);
            mutex.set(false);
            processActiveExit();
            return true;
        }
        return false;
    }

    private void processActiveExit() {
        if (listener != null) {
            try {
                listener.processActiveExit();
            } catch (Exception e) {
                logger.error("processActiveExit failed", e);
            }
        }
    }


    /**
     * 检查当前的状态
     */
    public boolean check() {
        String path = cluster_run;
        try {
            byte[] bytes = zkClient.readData(path);
            ServerRunningData eventData = JsonUtils.unmarshalFromByte(bytes, ServerRunningData.class);
            activeData = eventData;// 更新为最新值
            boolean result = isMine(activeData.getAddress());
            if (!result) {
                logger.warn("service is running  in server node[{}] , but not in server node[{}]",
                        activeData.getCid(),
                        serverData.getCid());
            }
            return result;
        } catch (ZkNoNodeException e) {
            logger.warn("service is not run any in node");
            return false;
        } catch (ZkInterruptedException e) {
            logger.warn("check is interrupt");
            Thread.interrupted();// 清除interrupt标记
            return check();
        } catch (ZkException e) {
            logger.warn("check is failed");
            return false;
        }
    }

    private boolean isMine(String address) {
        return address.equals(serverData.getAddress());
    }

    public void setClusterRun(String cluster_run) {
        this.cluster_run = cluster_run;
    }

    public void setClusterRoot(String cluster_root) {
        this.cluster_root = cluster_root;
    }
}
