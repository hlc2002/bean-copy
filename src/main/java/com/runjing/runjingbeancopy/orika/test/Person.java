package com.runjing.runjingbeancopy.orika.test;


import com.runjing.runjingbeancopy.orika.annotation.EnableOpenFieldCopy;
import com.runjing.runjingbeancopy.orika.annotation.FieldCopyMapping;
import com.runjing.runjingbeancopy.orika.annotation.FieldListCopyMapping;
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
@EnableOpenFieldCopy(openReverseCopyMappingRule = true)
@Getter
@Setter
@ToString(callSuper = true)
public class Person extends Eat {

    @FieldCopyMapping(targetFieldName = "id", targetClass = {PersonDto.class})
    @FieldListCopyMapping(targetFieldList = {"id","personAge"},targetClassList = {PersonBo.class, PersonVo.class})
    private Integer age;

    @FieldCopyMapping(targetFieldName = "personName",targetClass = {PersonDto.class, PersonVo.class})
    private String name;

    @FieldCopyMapping(targetFieldName = "addressInfo",targetClass = {PersonBo.class})
    private Address address;
}
