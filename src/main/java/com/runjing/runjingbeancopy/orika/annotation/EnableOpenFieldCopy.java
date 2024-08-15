package com.runjing.runjingbeancopy.orika.annotation;


import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * The interface Enable open field copy.
 *
 * @author : forestSpringH
 * @description:
 * @date : Created in 2023/9/14
 * @modified By :
 * @project: runjing-bean-copy
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public @interface EnableOpenFieldCopy {
    /**
     * 默认关闭类型拷贝映射，需要注意的是：该选项不开启则全部都失效，该注解就仅仅作为Bean的标识使用了
     *
     * @return the boolean
     */
    boolean value() default true;

    /**
     * 是否开启逆向字段映射
     *
     * @return the boolean
     */
    boolean openReverseCopyMappingRule() default false;
}
