package com.runjing.runjingbeancopy.orika.test;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author : forestSpringH
 * @description:
 * @date : Created in 2023/9/14
 * @modified By:
 * @project: runjing-bean-copy
 */
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@ToString(callSuper = true)
public class PersonBo extends Eat{
    private Integer id;
    private String name;
    private Address addressInfo;
}
