package com.wp.ha.kafkastorm;

/**
 * Created with IntelliJ IDEA.
 * User: wp
 * To change this template use File | Settings | File Templates.
 */
public interface KafkaProperties {
    final static String zkConnect = "127.0.0.1:2181";
    final static String groupId = "hammal_group";
    final static String topic = "hammal_topic";
    final static String kafkaServerURL = "127.0.0.1";
    final static int kafkaServerPort = 9092;
    final static int kafkaProducerBufferSize = 64 * 1024;
    final static int connectionTimeOut = 20000;
    final static int reconnectInterval = 10000;
    final static String topic2 = "topic2";
    final static String topic3 = "topic3";
    final static String clientId = "SimpleConsumerDemoClient";
}
