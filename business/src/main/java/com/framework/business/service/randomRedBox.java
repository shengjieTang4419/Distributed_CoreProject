package com.framework.business.service;

import com.framework.middleware.abstractUtil.DoubleUtil;
import org.apache.commons.lang3.RandomUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/7/19 11:44
 * @description：红包算法设计
 * @modified By：
 * @version: 1$
 */
public class randomRedBox {
    private static Logger logger = Logger.getLogger(randomRedBox.class);

    private static Integer num = 20; //红包数
    private static Integer pnum = 40; //人数 开多线程竞争资源，用定长LinkedBlockingQueue记录
    private static BlockingQueue<Integer> queue = new LinkedBlockingQueue(num);

    private static Double totalAmount = 100d; //金额

    public static void main(String[] args) {
        for(int i =0;i<num;i++){
            //1.获取安全上限制 平均数
            Double maxAmount = DoubleUtil.div(totalAmount,num-i,2);
            //2.获取安全范围内的红包金额 最小为0.01 最后一人为剩余红包的钱 不走随机数
            Double costAmount = getRandomNum(maxAmount);
            if(i==num-1) costAmount = totalAmount;
            //3.红包总额减少
            totalAmount = DoubleUtil.sub(totalAmount,costAmount);
            //4.输出日志打印信息
            System.out.println("第"+(i+1)+"个人抢红包,红包可抢的最大值:"+maxAmount+"  他抢了:"+costAmount+"  红包剩下： "+totalAmount);
        }
    }

    private static Double getRandomNum(Double max){
        //获取随机数 [0.01,max] 两位小数 四舍五入
       return DoubleUtil.round(Math.random() * (max + 0.01)+ 0.01,2, BigDecimal.ROUND_HALF_UP) ;
    }


}
