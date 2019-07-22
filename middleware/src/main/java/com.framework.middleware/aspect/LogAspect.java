package com.framework.middleware.aspect;

import com.framework.middleware.abstractMapping.BusinessMetaMapping;
import com.framework.middleware.abstractMapping.BusinessOperationMapping;
import com.framework.middleware.abstractUtil.CollectionUtil;
import com.framework.middleware.annotation.RefBusinessMeta;
import com.framework.middleware.annotation.RefBusinessOperation;
import com.framework.middleware.shareBean.Operation;
import com.framework.middleware.shareMapper.OperationMapper;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author ：shengjie.tang
 * @date ：Created in 2019/7/19 15:28
 * @description：日志切片类 记录插入 修改 删除的记录
 * 定义规则，更新(doUpdate)，插入(doInsert)，删除(doDelete)
 * 查询为 findById query queryPage  我迟早重写Mybatis的那个插件
 * @modified By：
 * @version: 1$
 */
@Aspect
@Component
public class LogAspect {
    private static Logger logger = Logger.getLogger(LogAspect.class);
    //第一个* 表示返回类型不限制 第二个*所有mapper包下的类，第三个do*表示拦截的方法(..)参数不限制
    private final String POINT_CUT = "execution(* com.framework..mapper.*.do*(..))";

    /**声明code**/
    private final static String CODE = "code";
    private final static String BILLNO = "billNo";

    @Pointcut(POINT_CUT)
    public void pointCut(){}

    @Autowired
    OperationMapper mapper;

    /**
     * 切点方法返回后执行
     *     如果第一个参数为JoinPoint，则第二个参数为返回值的信息
     *     如果第一个参数不为JoinPoint，则第一个参数为returning中对应的参数
     *     returning：限定了只有目标方法返回值与通知方法参数类型匹配时才能执行后置返回通知，否则不执行，
     *     参数为Object类型将匹配任何目标返回值
     */
    @AfterReturning(value = "pointCut()",returning = "result")
    public void doAfter(JoinPoint joinPoint, Object result){
        //因为切的是mapper 我理解为基础数据操作类，那么他的操作(DML)的入参 应该只有两种List<Bean>和Bean。如果不是这种格式请放到IService层内。注意粒子化
        Object[] args = joinPoint.getArgs();
        List<String> billNos = initBillNos(args);

        //获取横切的方法
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method signatureMethod = signature.getMethod();
        //获取横切Bean对象名称
        String clazzName = signatureMethod.getDeclaringClass().getName();
        clazzName = clazzName.substring(clazzName.lastIndexOf(".")+1,clazzName.length());
        String methodName = signatureMethod.getName();
        //初始化对象
        String metaName = "";
        String operationName = "";
        //获取配置化注解
        RefBusinessMeta refBusinessMetaAnnotioan = signatureMethod.getDeclaringClass().getAnnotation(RefBusinessMeta.class);
        RefBusinessOperation methodAnnotation = signatureMethod.getAnnotation(RefBusinessOperation.class);
        //映射名称
        if(refBusinessMetaAnnotioan!=null) { metaName = refBusinessMetaAnnotioan.value();}
        else {metaName = BusinessMetaMapping.matchBusinessMeta(clazzName);}
        if(methodAnnotation!=null){operationName = methodAnnotation.value();}
        else{operationName = BusinessOperationMapping.matchOperation(methodName);}
        //创建生成
        fillOperation(billNos,metaName,operationName).forEach(n ->  mapper.insertSelective(n));
    }

    /**
     *  组装操作对象集合
     * @param billNos
     * @param metaName
     * @param operationName
     * @return
     */
    private  static  List<Operation> fillOperation(List<String> billNos,String metaName,String operationName){
        List<Operation> operations = new ArrayList<>();
        Operation operation;
        for(String billNo : billNos){
            operation = new Operation();
            operation.setBillno(billNo);
            operation.setBilltype(metaName);
            operation.setOperation(operationName);
            operation.setOperationtime(new Date());
            //操作人 在Session会话中获取  这里暂时拿不到
            operation.setOperationbyid(1);
            operation.setOperationbycode("000");
            operation.setOperationbyname("测试用户");
            operations.add(operation);
        }
        return operations;
    }


    /***
     * 初始化billNos
     * @param args
     * @return
     */
    private static List<String> initBillNos(Object[] args){
        Object arg;
        List<String> billNos = new ArrayList();
        if(CollectionUtil.isNotEmpty(args)){
            arg =  args[0];
            if(arg instanceof Collection){
                for(Object e : (Collection)arg){
                    billNos.add(getBillNo(arg.getClass(),e));
                }
            }else{
                billNos.add(getBillNo(arg.getClass(),arg));
            }
        }
        return billNos;
    }

    /**
     * 获取业务对象的编号 code或者billNo
     * @return
     */
    private static String getBillNo(Class clazz,Object bean) {
        Field billNoField = null;
        try {
            billNoField = clazz.getDeclaredField(BILLNO);
            billNoField.setAccessible(true);
            return String.valueOf(billNoField.get(bean));
        } catch (NoSuchFieldException|IllegalAccessException e) {
        }

        Field codeField = null;
        try {
            codeField = clazz.getDeclaredField(CODE);
            codeField.setAccessible(true);
            return String.valueOf(codeField.get(bean));
        } catch (NoSuchFieldException|IllegalAccessException e) {
        }
        return null;
    }

}
