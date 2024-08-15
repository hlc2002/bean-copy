package com.runjing.runjingbeancopy.orika.annotation;

import java.lang.annotation.*;

/**
 * The interface Field list copy mapping.
 *  支持列表映射，多字段对应关系使用
 * @author : forestSpringH
 * @description:
 * @date : Created in 2023/9/19
 * @modified By :
 * @project: runjing -bean-copy
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface FieldListCopyMapping {
    /**
     * 目标字段数组
     *
     * @return the string [ ]
     */
    String[] targetFieldList() default  {};

    /**
     * 目标拷贝类型数组，注意使用时上述的数组与类型数组要组成 key - value 的关系，必须要求value类型中含有字段key否则编译阶段报错
     *
     * @return the class [ ]
     */
    Class<?>[] targetClassList() default {};
}
