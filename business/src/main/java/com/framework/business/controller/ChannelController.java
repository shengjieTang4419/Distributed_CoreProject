package com.framework.business.controller;
import com.framework.business.mapper.ChannelMapper;
import com.framework.business.serviceImpl.ChannelServiceImpl;
import com.framework.middleware.shareBean.Channel;
import com.framework.middleware.shareService.IChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/6/22 22:31
 * @description：测试跨服务调用
 * @modified By：
 * @version: 1$
 */
@RestController
        @RequestMapping("/channel")
public class ChannelController {

    @Autowired
    @Qualifier("channelServiceImpl")
    IChannelService service;

    @RequestMapping("/finById")
    public String findById() {
        return service.findChannelById(1).toString();
    }

    @RequestMapping("/testRedisCache")
    public String testRedisCache() {
        Channel newChannel= service.testInsertRedisCache();
        return "缓存："+service.queryByIdFromCache(newChannel.getId()).toString();
    }

    @RequestMapping("/testSendMQ")
    public String testMQSender() {
        service.testMQSender();
        return "MQ已经发送";
    }


}
