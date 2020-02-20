package com.hezepeng.annotationserver;

import com.hezepeng.annotationserver.common.MongoConverter;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.*;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.Arrays;

@SpringBootApplication
public class AnnotationServerApplication {

//    @Bean
//    public MongoCustomConversions mongoCustomConversions() {
//        return new MongoCustomConversions(Arrays.asList(new MongoConverter()));
//    }
//
//    @Bean
//    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory) {
//        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
//        // Don't save _class to mongo
//        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
//        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
//        mappingConverter.setCustomConversions(mongoCustomConversions());
//        return mappingConverter;
//    }

    public static void main(String[] args) {
        SpringApplication.run(AnnotationServerApplication.class, args);
    }


}
