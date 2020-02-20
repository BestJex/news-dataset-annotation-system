package com.hezepeng.annotationserver.conf;

/**
 * @author: Hezepeng
 * @email: hezepeng96@foxmail.com
 * @date: 2020/2/20 14:46
 */

import com.hezepeng.annotationserver.entity.Tag;
import com.hezepeng.annotationserver.util.DateTimeUtil;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Configuration
@EnableMongoAuditing
public class MongoConfiguration {

    @Autowired
    private MongoDbFactory factory;

    @Bean
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        // 这里依次加入自定义的Converter
        converters.add(new DateToZonedDateTimeConverter());
        converters.add(new ZonedDateTimeToDateConverter());
        converters.add(new StringToDateConverter());

        converters.add(new TagConverter());
        return new MongoCustomConversions(converters);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {

        MongoTemplate mongoTemplate = new MongoTemplate(factory);
        MappingMongoConverter mongoMapping = (MappingMongoConverter) mongoTemplate.getConverter();
        // tell mongodb to use the custom converters
        mongoMapping.setCustomConversions(customConversions());
        mongoMapping.afterPropertiesSet();
        return mongoTemplate;

    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        try {
            mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        } catch (NoSuchBeanDefinitionException ignore) {
        }

        // 去掉_class字段
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));

        return mappingConverter;
    }

    class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {

        @Override
        public ZonedDateTime convert(Date source) {
            return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }

    class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {

        @Override
        public Date convert(ZonedDateTime source) {

            return source == null ? null : Date.from(source.toInstant());
        }
    }

    @ReadingConverter
    class StringToDateConverter implements Converter<String, Date> {

        @Override
        public Date convert(String source) {
            System.out.println(source);
            return source == null ? null : DateTimeUtil.strToDate(source);
        }
    }

    @ReadingConverter
    class TagConverter implements Converter<String, Tag> {

        @Override
        public Tag convert(String source) {
            // 不符合规范的tag对象会自动调用这个转换器 字符数组["CHINA","USA"] -> 两个Tag对象
            System.out.println("xxx:" + source.toString());
            return new Tag(source, null);
        }
    }
}