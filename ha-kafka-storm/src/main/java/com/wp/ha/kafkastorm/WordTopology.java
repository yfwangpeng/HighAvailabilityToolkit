package com.wp.ha.kafkastorm;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

/**
 * Created with IntelliJ IDEA.
 * User: wp
 * To change this template use File | Settings | File Templates.
 */
public class WordTopology {
    public static void main(String[] args)throws Exception{
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("word-reader",new KafkaSpout(),3);     //parallelism: 与kafka的partition数一致
        builder.setBolt("word-normalizer", new WordSplitterBolt()).shuffleGrouping("word-reader");
        builder.setBolt("word-counter", new WordCounterBolt(),2).fieldsGrouping("word-normalizer", new Fields("word"));

        Config conf = new Config();
        conf.put(Config.TOPOLOGY_MAX_SPOUT_PENDING, 1);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("Getting-Started-Toplogie", conf, builder.createTopology());
        Thread.sleep(20000);
        cluster.shutdown();
    }
}
