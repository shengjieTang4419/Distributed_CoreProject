package com.framework.business.serviceImpl;

import com.framework.business.mapper.ChannelMapper;
import com.framework.middleware.abstractUtil.MQSendUtil;
import com.framework.middleware.shareBean.Channel;
import com.framework.middleware.shareService.IChannelService;
import org.apache.log4j.Logger;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/6/23 13:47
 * @description：Channel 具体实现类
 * @modified By：
 * @version: 1$
 */
@Service
public class ChannelServiceImpl implements IChannelService {

    private static Logger logger = Logger.getLogger(String.valueOf(ChannelServiceImpl.class));

    @Value("${rocketmq.producer.producerGroup}")
    private String producerGroupName;

    @Value("${testValuSet}")
    private  String testValue;

    @Value("${rocketmq.producer.namesrvAddr}")
    private String nameServerAddress;

    @Value("${rocketmq.producer.instance}")
    private String producerName;

    @Autowired
    ChannelMapper mapper;

    @Override
    public Channel findChannelById(Integer i) {
        logger.info("Do others somethings......");
        return mapper.findById(i);
    }

    @Cacheable(value = "channel", key = "'id'")
    @Override
    public Channel testInsertRedisCache() {
        logger.info("开始测试插入Redis缓存......");
        Channel newChannel = new Channel();
        newChannel.setId(1);
        newChannel.setCode("888");
        newChannel.setName("测试渠道");
        return newChannel;
    }

    @Cacheable(value = "channel", key = "'id'")
    @Override
    public Channel queryByIdFromCache(Integer channelId) {
        logger.info("开始测试查询Redis缓存......");
        return null;
    }

    @Override
    public void testMQSender() {
        System.out.println(testValue);
        List<Message> msgs = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            msgs.add(new Message("TestTopic0623", "Tag001", (new Date() + " Hello RocketMQ ,QuickStart" + i).getBytes()));
        }
        //send
        MQSendUtil.sendMessage(msgs,producerGroupName,nameServerAddress,producerName);
    }

}
