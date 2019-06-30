package com.framework.middleware.shareService;

import com.framework.middleware.shareBean.Channel;

public interface IChannelService {
    public Channel findChannelById(Integer i);

    public Channel testInsertRedisCache();

    public Channel queryByIdFromCache(Integer i);

    public void testMQSender();

}
