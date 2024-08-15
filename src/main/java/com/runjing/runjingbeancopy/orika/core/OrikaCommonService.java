package com.runjing.runjingbeancopy.orika.core;

import lombok.SneakyThrows;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.converter.BidirectionalConverter;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.ClassMapBuilder;
import ma.glasnost.orika.metadata.Type;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author : huanglinchun
 * @description:
 * @date : Created in 2023/10/12
 * @modified By: huanglinchun
 * @project: runjing-bean-copy
 */
@Service
@Lazy
public class OrikaCommonService {

    private final static MapperFactory MAPPER_FACTORY = initFactory();
    private final static MapperFacade MAPPER_FACADE =  MAPPER_FACTORY.getMapperFacade();

    public <S,T> ClassMapBuilder<?, ?> classMapBuilder(Class<S> sourceClass, Class<T> targetClass){
        return MAPPER_FACTORY.classMap(sourceClass,targetClass);
    }

    public <S,T> ClassMapBuilder<?, ?> classMapBuilder(Class<S> sourceClass, Class<T> targetClass,Boolean isTypeDeepCopy){
        return isTypeDeepCopy ? MAPPER_FACTORY.classMap(sourceClass,targetClass).customize(new CustomMapper<S, T>() {
            @SneakyThrows
            @Override
            public void mapAtoB(S s, T t, MappingContext context) {
                super.mapAtoB(s, t, context);
            }
        }): MAPPER_FACTORY.classMap(sourceClass,targetClass);
    }

    public void registerClassMappingField(ClassMapBuilder<?,?> classMapBuilder,String source,String target,Boolean isCloseClassMap){
        if (!StringUtils.hasLength(source) || !StringUtils.hasLength(target)){
            throw new IllegalArgumentException("register field name exception！");
        }
        if (isCloseClassMap){
            classMapBuilder.field(source,target).byDefault().register();
        }else{
            classMapBuilder.field(source,target);
        }

    }

    public <S,T> T copy(S source,Class<T> targetClass){
        return MAPPER_FACADE.map(source,targetClass);
    }

    public <S,T> void copy(S source,T target){
        MAPPER_FACADE.map(source,target);
    }

    public <S,T> List<T> copyByList(Collection<S> sourceCollection,Class<T> targetClass){
        if (CollectionUtils.isEmpty(sourceCollection)){
            return new LinkedList<>();
        }
        return MAPPER_FACADE.mapAsList(sourceCollection,targetClass);
    }

    private static MapperFactory initFactory() {
        // mapNulls 表示 原对象中的null不会拷贝到目标对象
        MapperFactory factory = new DefaultMapperFactory.Builder().mapNulls(false).build();
        factory.getConverterFactory().registerConverter(new DateToString());
        factory.getConverterFactory().registerConverter(new TimestampToString());
        return factory;
    }

    static class DateToString extends BidirectionalConverter<Date, String> {
        @Override
        public String convertTo(Date date, Type<String> type, MappingContext mappingContext) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }

        @SneakyThrows
        @Override
        public Date convertFrom(String s, Type<Date> type, MappingContext mappingContext) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.parse(s);
        }
    }

    static class TimestampToString extends BidirectionalConverter<Timestamp, String> {
        @Override
        public String convertTo(Timestamp date, Type<String> type, MappingContext mappingContext) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return format.format(date);
        }

        @SneakyThrows
        @Override
        public Timestamp convertFrom(String s, Type<Timestamp> type, MappingContext mappingContext) {
            return null;
        }
    }
}
