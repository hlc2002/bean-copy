package com.runjing.runjingbeancopy.orika.service;


import com.runjing.runjingbeancopy.orika.annotation.EnableOpenFieldCopy;
import com.runjing.runjingbeancopy.orika.annotation.FieldCopyMapping;
import com.runjing.runjingbeancopy.orika.annotation.FieldListCopyMapping;
import com.runjing.runjingbeancopy.orika.core.OrikaCommonService;
import com.runjing.runjingbeancopy.orika.model.MapperModel;
import com.runjing.runjingbeancopy.orika.spring.SpringFetchBeanHolder;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The type Bean copy service.
 *
 * @author : forestSpringH
 * @description:
 * @date : Created in 2023/9/14
 * @modified By :
 * @project: runjing-bean-copy
 */
@Service
@Slf4j
@DependsOn({"SpringFetchBeanHolder"})
public class BeanCopyService extends OrikaCommonService implements DisposableBean {

    private final static Class<EnableOpenFieldCopy> ENABLE_OPEN_FIELD_MAPPER = EnableOpenFieldCopy.class;
    private final static Class<FieldCopyMapping> FIELD_COPY_MAPPER = FieldCopyMapping.class;
    private final static Class<FieldListCopyMapping> FIELD_LIST_COPY_MAPPER = FieldListCopyMapping.class;

    private final List<MapperModel> mapperModelList = new LinkedList<>();

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        List<Class<?>> beanList = SpringFetchBeanHolder.getBeanByAnnotation(ENABLE_OPEN_FIELD_MAPPER);
        register(beanList, mapperModelList);
    }

    @Override
    public void destroy() {
        mapperModelList.clear();
        System.gc();
    }

    /**
     * Register.
     *
     * @param beanCopyList    the bean copy list
     * @param mapperModelList the mapper model list
     */
    private void register(List<Class<?>> beanCopyList, List<MapperModel> mapperModelList) {
        if (CollectionUtils.isEmpty(beanCopyList)) {
            return;
        }
        beanCopyList.forEach(clazz -> {
            List<Field> collect = Arrays.asList(clazz.getDeclaredFields());
            log.info("BeanCopyService init reflect class：{} --> init reflect fields:{}", clazz.getSimpleName(), collect.stream().map(Field::getName).collect(Collectors.toList()));
            loadFieldMapperModelList(collect, clazz, mapperModelList);
        });
        Map<String, List<MapperModel>> group = groupByMapperKeyPlus(mapperModelList);
        registerByGroup(group);
    }

    /**
     * Load field mapper model list.
     *
     * @param fieldList       the field list
     * @param clazz           the clazz
     * @param mapperModelList the mapper model list
     */
    private void loadFieldMapperModelList(List<Field> fieldList, Class<?> clazz, List<MapperModel> mapperModelList) {
        if (!CollectionUtils.isEmpty(fieldList)) {
            fieldList.forEach(field -> {
                judgeFieldMapper(field, clazz, mapperModelList);
                judgeFieldListMapper(field, clazz, mapperModelList);
            });
        }
    }

    /**
     * Judge field mapper.
     *
     * @param field           the field
     * @param clazz           the clazz
     * @param mapperModelList the mapper model list
     */
    private void judgeFieldMapper(Field field, Class<?> clazz, List<MapperModel> mapperModelList) {
        if (!field.isAnnotationPresent(FIELD_COPY_MAPPER)) {
            return;
        }
        FieldCopyMapping annotation = field.getAnnotation(FIELD_COPY_MAPPER);
        String sourceFieldName = field.getName();
        String targetFieldName = annotation.targetFieldName();
        List<Class<?>> targetClazzList = Arrays.asList(annotation.targetClass());
        if (CollectionUtils.isEmpty(targetClazzList)) {
            log.error("FieldCopyMapping annotation error to be used，field name：{}", field.getName());
            throw new RuntimeException("FieldCopyMapping annotation error to be used");
        }
        targetClazzList.forEach(targetClazz -> fillMapperModelList(mapperModelList, clazz, targetClazz, sourceFieldName, targetFieldName));
    }

    /**
     * Judge field list mapper.
     *
     * @param field           the field
     * @param clazz           the clazz
     * @param mapperModelList the mapper model list
     */
    private void judgeFieldListMapper(Field field, Class<?> clazz, List<MapperModel> mapperModelList) {
        if (!field.isAnnotationPresent(FIELD_LIST_COPY_MAPPER)) {
            return;
        }
        FieldListCopyMapping annotation = field.getAnnotation(FIELD_LIST_COPY_MAPPER);
        List<String> targetFieldList = Arrays.asList(annotation.targetFieldList());
        List<Class<?>> targetClazzList = Arrays.asList(annotation.targetClassList());
        if (!Objects.equals(targetFieldList.size(), targetClazzList.size())) {
            log.error("FieldListCopyMapping annotation error to be used，field name：{}", field.getName());
            throw new RuntimeException("FieldListCopyMapping annotation error to be used");
        }
        Map<String, Class<?>> mapperMap = new HashMap<>();
        for (int i = 0; i < targetFieldList.size(); i++) {
            mapperMap.put(targetFieldList.get(i), targetClazzList.get(i));
        }
        mapperMap.forEach((key, value) -> fillMapperModelList(mapperModelList, clazz, value, field.getName(), key));
    }

    /**
     * Fill mapper model list.
     *
     * @param mapperModels    the mapper models
     * @param clazz           the clazz
     * @param targetClazz     the target clazz
     * @param sourceFieldName the source field name
     * @param targetFieldName the target field name
     */
    private void fillMapperModelList(List<MapperModel> mapperModels, Class<?> clazz, Class<?> targetClazz, String sourceFieldName, String targetFieldName) {
        if (clazz.getAnnotation(ENABLE_OPEN_FIELD_MAPPER).openReverseCopyMappingRule()) {
            MapperModel model = new MapperModel(clazz.getName() + targetClazz.getName(), clazz, targetClazz, sourceFieldName, targetFieldName);
            mapperModels.add(model);
            MapperModel reverseModel = new MapperModel(targetClazz.getName() + clazz.getName(), targetClazz, clazz, targetFieldName, sourceFieldName);
            mapperModels.add(reverseModel);
        } else {
            MapperModel model = new MapperModel(clazz.getName() + targetClazz.getName(), clazz, targetClazz, sourceFieldName, targetFieldName);
            mapperModels.add(model);
        }
    }

    /**
     * Register by group.
     *
     * @param group the group
     */
    private void registerByGroup(Map<String, List<MapperModel>> group) {
        if (CollectionUtils.isEmpty(group)) {
            return;
        }
        group.values().forEach(modelList -> {
            ClassMapBuilder<?, ?> classMapBuilder = super.classMapBuilder(modelList.get(0).getSourceClass(), modelList.get(0).getTargetClass());
            for (MapperModel model : modelList) {
                log.info("init copyRules ：{}.{} --> {}.{}", model.getSourceClass().getSimpleName(), model.getSourceFieldName(), model.getTargetClass().getSimpleName(), model.getTargetFieldName());
                super.registerClassMappingField(classMapBuilder, model.getSourceFieldName(), model.getTargetFieldName(), Objects.equals(modelList.get(modelList.size() - 1), model));
            }
        });
    }

    /**
     * Group by mapper key plus.
     *
     * @param modelList the model list
     * @return the map
     */
    private Map<String, List<MapperModel>> groupByMapperKeyPlus(@NonNull List<MapperModel> modelList) {
        return modelList.stream()
                .collect(Collectors.groupingBy(MapperModel::getMapperKey));
    }
}
