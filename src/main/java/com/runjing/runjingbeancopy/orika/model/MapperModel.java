package com.runjing.runjingbeancopy.orika.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Mapper model.
 *
 * @author : forestSpringH
 * @description:
 * @date : Created in 2023/9/14
 * @modified By :
 * @project: runjing-bean-copy
 */
@Data
@AllArgsConstructor
public class MapperModel {
    private String mapperKey;
    private Class<?> sourceClass;
    private Class<?> targetClass;
    private String sourceFieldName;
    private String targetFieldName;
}
