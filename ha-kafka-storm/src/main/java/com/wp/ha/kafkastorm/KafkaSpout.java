package com.wp.ha.kafkastorm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: wp
 * Date: 14-12-28
 * Time: 上午12:08
 * To change this template use File | Settings | File Templates.
 */
public class KafkaSpout implements IRichSpout {
    SpoutOutputCollector _collector;

//    private Properties kafkaProperties;

    private  String topic;

    private ConsumerIterator consumerIterator;

    private kafka.javaapi.consumer.ConsumerConnector consumer;

    private TopologyContext context;


    @Override
    public void open(Map conf, TopologyContext context,
                     SpoutOutputCollector collector) {
        this.context = context;
//        Properties kafkaProps = new Properties();
//        kafkaProps.put("zk.connect", "localhost:2182");
//        kafkaProps.put("zk.connectiontimeout.ms", "1000000");
//        kafkaProps.put("groupid", "group1");
//        this.kafkaProperties=kafkaProps;
        this.topic=KafkaProperties.topic;
        _collector= collector;
        consumer = kafka.consumer.Consumer.createJavaConsumerConnector(createConsumerConfig());
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, new Integer(1));
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
        consumerIterator = stream.iterator();
    }

    private static ConsumerConfig createConsumerConfig()
    {
        Properties props = new Properties();
        props.put("zookeeper.connect", KafkaProperties.zkConnect);
        props.put("group.id", KafkaProperties.groupId);
        props.put("zookeeper.session.timeout.ms", "40000");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        return new ConsumerConfig(props);
    }

    @Override
    public void nextTuple() {
        MessageAndMetadata<byte[],byte[]> msg = consumerIterator.next();
        if (msg != null) {
            byte[] bytes = msg.message();
            try {
                this._collector.emit(new Values(new String(bytes, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        Utils.sleep(10);

    }
    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("line"));
    }

    @Override
    public void close() {
        if(consumer!=null)
            consumer.shutdown();

    }
    public boolean isDistributed() {
        return false;
    }
    @Override
    public void activate() {
    }
    @Override
    public void deactivate() {
    }
    @Override
    public void ack(Object msgId) {
    }
    @Override
    public void fail(Object msgId) {
    }
    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

}
