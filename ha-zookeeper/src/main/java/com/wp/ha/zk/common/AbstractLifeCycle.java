package com.wp.ha.zk.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yfwangpeng.
 */
public abstract class AbstractLifeCycle  {
    protected Logger logger = LoggerFactory.getLogger(AbstractLifeCycle.class);
    protected volatile boolean running = false; // 是否处于运行中

    public boolean isStart() {
        return running;
    }

    public void start() {
        if (running) {
            logger.error(this.getClass().getName() + " has startup , don't repeat start");
        }
        running = true;
    }

    public void stop() {
        if (!running) {
            logger.error(this.getClass().getName() + " has startup , don't repeat start");
        }
        running = false;
    }

}
