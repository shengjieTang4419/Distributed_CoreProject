package com.framework.manage.controller;

import com.framework.manage.mapper.UserMapper;
import com.framework.middleware.abstractUtil.MQSendUtil;
import com.framework.middleware.shareBean.Channel;
import com.framework.middleware.shareService.IChannelService;
import org.apache.ibatis.logging.log4j.Log4jImpl;
import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/6/19 23:30
 * @description：Usercontroller测试
 * @modified By：
 * @version: 1$
 */
@RestController
    @RequestMapping("/user")
public class UserController {

    private static Logger logger = Logger.getLogger(UserController.class);


    @Value("${rocketmq.consumer.customerGroup}")
    private String conumerGroupName;

    @Value("${rocketmq.consumer.namesrvAddr}")
    private String conumerNamesrvAddr;

    @Value("${rocketmq.consumer.instance}")
    private String conumerInstance;

    @Value("${rocketmq.consumer.topics}")
    private String conumerTopics;

    @Value("${rocketmq.consumer.tag}")
    private String conumerTag;
    @Autowired
    UserMapper mapper;
    @RequestMapping("/finById")
    public String findById() {
        return mapper.findById(1).toString();
    }

    @RequestMapping("/dubboTest")
    public String dubboTest(){
        ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext(new String[] { "consumer.xml" });
        ac.start();
        IChannelService service = (IChannelService) ac.getBean("exposeChannelService");
        Channel channel = service.findChannelById(1);
       return "在Manage工程中："+channel.toString();
    };

    @RequestMapping("/consumerMQTest")
    public String consumerMQTest(){
        DefaultMQPushConsumer messagePushConsumer = MQSendUtil.MessagePushConsumer(conumerGroupName, conumerNamesrvAddr,
                conumerInstance, conumerTopics, conumerTag);
        messagePushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (Message msg : msgs) {
                    byte[] messageBody = msg.getBody();
                    logger.info(messageBody);
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        try {
            messagePushConsumer.start();
        } catch (MQClientException e) {
            logger.error(e.getMessage());
        }
        return "";
    }



}
