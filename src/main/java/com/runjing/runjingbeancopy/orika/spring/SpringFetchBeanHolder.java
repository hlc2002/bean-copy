package com.runjing.runjingbeancopy.orika.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Spring holder.
 *
 * @author : forestSpringH
 * @description: 上下文信息透传管理类
 * @date : Created in 2023/9/14
 * @modified By :
 * @project: runjing-bean-copy
 */
@Component("SpringFetchBeanHolder")
@Lazy(value = false)
public class SpringFetchBeanHolder implements ApplicationContextAware , DisposableBean {
    private static ApplicationContext serviceApplicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        serviceApplicationContext = applicationContext;
    }

    /**
     * Get bean by annotation list.
     *
     * @param annotationClazz the annotation clazz
     * @return the list
     */
    public static List<Class<?>> getBeanByAnnotation(Class<? extends Annotation> annotationClazz){
        Assert.notNull(serviceApplicationContext, "容器上下文获取失败");
        Assert.notNull(annotationClazz,"注解字节码入参为空");
        List<String> collect = Arrays.stream(serviceApplicationContext.getBeanNamesForAnnotation(annotationClazz)).collect(Collectors.toList());
        List<Class<?>> classList = new LinkedList<>();
        if (!CollectionUtils.isEmpty(collect)){
            collect.forEach(s -> classList.add(getBeanByName(s).getClass()));
        }
        return classList;
    }

    /**
     * Gets bean by type.
     *
     * @param <T>       the type parameter
     * @param beanClazz the bean clazz
     * @return the bean by type
     */
    public static <T> T getBeanByType(Class<T> beanClazz) {
        Assert.notNull(serviceApplicationContext, "容器上下文获取失败");
        return serviceApplicationContext.getBean(beanClazz);
    }


    /**
     * Get bean by name t.
     *
     * @param <T>      the type parameter
     * @param beanName the bean name
     * @return the t
     */
    public static <T> T getBeanByName(String beanName){
        Assert.notNull(serviceApplicationContext,"容器上下文获取失败");
        return (T) serviceApplicationContext.getBean(beanName);
    }

    /**
     * Get bean list by type list.
     *
     * @param <T>       the type parameter
     * @param beanClazz the bean clazz
     * @return the list
     */
    public static <T> List<T> getBeanListByType(Class<T> beanClazz){
        Assert.notNull(serviceApplicationContext,"容器上下文获取失败");
        Map<String, T> beansOfType = serviceApplicationContext.getBeansOfType(beanClazz);
        if (CollectionUtils.isEmpty(beansOfType)){
            return new LinkedList<T>();
        }
        return new ArrayList<>(beansOfType.values());
    }

    /**
     * Push event.
     *
     * @param applicationEvent the application event
     */
    public static void pushEvent(ApplicationEvent applicationEvent){
        Assert.notNull(serviceApplicationContext, "容器上下文获取失败");
        serviceApplicationContext.publishEvent(applicationEvent);
    }

    @Override
    public void destroy() throws Exception {
        serviceApplicationContext = null;
    }
}
