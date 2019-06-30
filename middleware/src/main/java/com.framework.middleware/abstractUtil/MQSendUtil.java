package com.framework.middleware.abstractUtil;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/***
 * MQ发送消息Util工具类
 * 
 * @Description
 * @Author shengjie.tang
 * @Date 2019年3月23日
 * @Version 1.0
 */
public class MQSendUtil {
	private MQSendUtil() {
	}

	private static Logger logger = Logger.getLogger(MQSendUtil.class);

	public static SendResult sendMessage(List<Message> msgs, String producerGroup, String namesrvAddr,
			String instanceName) {
		// message为null 或者 message的信息体的长度为空，则返回
		if (msgs == null || msgs.size() == 0) {
			return new SendResult();
		}
		DefaultMQProducer producer = new DefaultMQProducer(producerGroup);
		producer.setNamesrvAddr(namesrvAddr);
		producer.setInstanceName(instanceName);
		producer.setVipChannelEnabled(false);
		SendResult sendResult = new SendResult();
		try {
			producer.start();
			sendResult = producer.send(msgs);
			logger.info(sendResult.getSendStatus() + "!");
			//producer.shutdown();
		} catch (MQClientException | RemotingException | MQBrokerException | InterruptedException e) {
			logger.error(e.getMessage());
		}
		return sendResult;
	}

	/**
	 * CONSUME_FROM_FIRST_OFFSET push的消费者 返回PushConsumer，需要自定义实现Listner
	 * 
	 * @param consumerGroup
	 * @param namesrvAddr
	 * @param instance
	 * @param topic
	 * @param tag
	 * @return
	 */
	public static DefaultMQPushConsumer MessagePushConsumer(String consumerGroup, String namesrvAddr, String instance,
			String topic, String tag) {
		DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(consumerGroup);
		consumer.setNamesrvAddr(namesrvAddr);
		consumer.setInstanceName(instance);

		try {
			consumer.subscribe(topic, tag);
			consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		} catch (MQClientException e) {
			logger.error(e.getMessage());
		}
		return consumer;
	}

}
