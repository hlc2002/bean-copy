package com.runjing.runjingbeancopy.orika.annotation;


import java.lang.annotation.*;

/**
 * The interface Field copy mapping.
 *
 * @author : forestSpringH
 * @description: 字段映射注解
 * @date : Created in 2023/9/14
 * @modified By :
 * @project: runjing-bean-copy
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldCopyMapping {
    /**
     * 标识拷贝目标字段名称
     *
     * @return the string
     */
    String targetFieldName() default "";

    /**
     * 标识拷贝目标类型数组，需注意拷贝目标类型需含有字段名称为targetFieldName()值的字段
     *
     * @return the class [ ]
     */
    Class<?>[] targetClass() default {};
}
